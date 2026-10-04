import { expect, it } from 'vitest'
import { MockEngine } from '../../src/mock/engine'
import { createDatabase } from '../../src/mock/database'
import type { Schema } from '../../src/api/types'
import { compareIds } from '../../src/mock/worker'
const now = Date.parse('2026-10-04T09:00:00+08:00')
async function setup() {
  const engine = new MockEngine(createDatabase(now), () => now)
  const login = async (role: 'worker' | 'customer') =>
    (
      (await engine.handle(`${role}Login`, { body: { username: role, password: 'Demo12345' } }))
        .data as Schema['LoginVO']
    ).accessToken
  return { engine, worker: await login('worker'), customer: await login('customer') }
}
it('入池发布时间全量升序后分页，大整数ID精确排序，调价不改发布时间', async () => {
  const { engine, worker, customer } = await setup()
  const seed = structuredClone(engine.db.orders[1])
  engine.db.orders = []
  const ids = ['90071992547409930', '90071992547409929', '9999999999999999999']
  for (const [i, id] of ids.entries()) {
    const stored = structuredClone(seed)
    stored.order.id = id
    stored.order.offerPublishedAt =
      i === 2 ? '2026-10-04T08:00:00+08:00' : '2026-10-04T08:30:00+08:00'
    stored.order.offerDeadline = i === 2 ? '2026-10-04T11:00:00+08:00' : '2026-10-04T10:00:00+08:00'
    engine.db.orders.push(stored)
    engine.db.histories[id] = structuredClone(engine.db.histories['10002'])
  }
  const page = async (pageNo: number) =>
    (await engine.handle('listEligibleOffers', { token: worker, query: { pageNo, pageSize: 1 } }))
      .data as Schema['OfferPageDTO']
  expect((await page(1)).list[0].id).toBe(ids[2])
  expect((await page(2)).list[0].id).toBe(ids[1])
  expect((await page(3)).list[0].id).toBe(ids[0])
  expect(compareIds(ids[1], ids[0])).toBe(-1)
  await engine.handle('changeOffer', {
    token: customer,
    id: ids[2],
    body: {
      expectedPrice: '130.00',
      newPrice: '135.00',
      priceVersion: 1,
      confirmSimulatedPayment: true,
    },
    idempotencyKey: 'price_same_published',
  })
  expect((await page(1)).list[0]).toMatchObject({
    id: ids[2],
    publishedAt: '2026-10-04T08:00:00+08:00',
    currentPrice: '135.00',
  })
  await expect(
    engine.handle('listEligibleOffers', { token: worker, query: { sort: 'LATEST' } }),
  ).rejects.toMatchObject({ code: 'VALIDATION_ERROR' })
})
it('客户不写坐标，系统坐标仅随位置改变失效，历史快照不变且空坐标可预约', async () => {
  const { engine, customer } = await setup()
  const address = engine.db.addresses[0]
  address.longitude = 113.25
  address.latitude = 23.15
  const {
    id,
    customerId: _customerId,
    latitude: _latitude,
    longitude: _longitude,
    ...dto
  } = address
  const before = structuredClone(engine.db.orders[0].order.address)
  const update = async (body: object, key: string) =>
    (await engine.handle('updateAddress', { token: customer, id, body, idempotencyKey: key }))
      .data as Schema['AddressVO']
  expect(
    await update({ ...dto, contactName: '新联系人', isDefault: true }, 'contact_only'),
  ).toMatchObject({ latitude: 23.15, longitude: 113.25 })
  expect(await update({ ...dto, detail: '新位置' }, 'new_location')).toMatchObject({
    latitude: null,
    longitude: null,
  })
  await expect(
    update({ ...dto, latitude: 0, longitude: 0 }, 'coordinates_rejected'),
  ).rejects.toMatchObject({ code: 'VALIDATION_ERROR' })
  const created = (
    await engine.handle('createOrder', {
      token: customer,
      body: {
        skuId: '301',
        addressId: id,
        bookingType: 'STANDARD',
        startTime: '2026-10-05T09:00:00+08:00',
      },
      idempotencyKey: 'null_location_booking',
    })
  ).data as Schema['OrderVO']
  expect(created.address).toMatchObject({ longitude: null, latitude: null, detail: '新位置' })
  await update({ ...dto, detail: '再次修改' }, 'later_location')
  expect(created.address.detail).toBe('新位置')
  expect(engine.db.orders.find((row) => row.order.id === '10001')!.order.address).toEqual(before)
})
it('创建待支付不会入池，正式支付时间成为发布时间，幂等支付和调价不重置', async () => {
  const { engine, customer, worker } = await setup()
  const created = (
    await engine.handle('createOrder', {
      token: customer,
      body: {
        skuId: '301',
        addressId: '401',
        bookingType: 'OFFER',
        startTime: '2026-10-05T09:00:00+08:00',
        offerPrice: '140.00',
      },
      idempotencyKey: 'publication_create',
    })
  ).data as Schema['OrderVO']
  expect(created.offerPublishedAt).toBeUndefined()
  expect(
    (
      (await engine.handle('listEligibleOffers', { token: worker })).data as Schema['OfferPageDTO']
    ).list.some((o) => o.id === created.id),
  ).toBe(false)
  engine.db.clockOffset += 5 * 60_000
  const request = { token: customer, id: created.id, idempotencyKey: 'publication_pay' }
  const paid = (await engine.handle('payOrder', request)).data as Schema['OrderVO']
  expect(paid.offerPublishedAt).not.toBe(paid.createdAt)
  expect(paid.offerPublishedAt).toBe('2026-10-04T09:05:00+08:00')
  engine.db.clockOffset += 60_000
  expect((await engine.handle('payOrder', request)).data).toEqual(paid)
})
