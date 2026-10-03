import { beforeEach, expect, it } from 'vitest'
import { MockEngine } from '../../src/mock/engine'
import { createDatabase } from '../../src/mock/database'
import type { Schema, OperationId } from '../../src/api/types'
import { HOUR, iso } from '../../src/utils/format'
const now = Date.parse('2026-10-03T09:00:00+08:00')
let engine: MockEngine, customer: string, other: string, worker: string, admin: string
let sequence = 0
const key = () => `customer_test_${++sequence}`
async function login(role: 'customer' | 'worker' | 'admin', username = role as string) {
  return (
    (await engine.handle(`${role}Login`, { body: { username, password: 'Demo12345' } }))
      .data as Schema['LoginVO']
  ).accessToken
}
async function write(operation: OperationId, token: string, body?: unknown, id?: string) {
  return (await engine.handle(operation, { token, body, id, idempotencyKey: key() })).data
}
const png = () =>
  new File([Uint8Array.from([137, 80, 78, 71, 13, 10, 26, 10])], 'scene.png', { type: 'image/png' })
beforeEach(async () => {
  engine = new MockEngine(createDatabase(now), () => now)
  customer = await login('customer')
  other = await login('customer', 'customer2')
  worker = await login('worker')
  admin = await login('admin')
})
it('固定入口明确绑定、下架或删除不回退，新增目录不改变入口', async () => {
  const entries = () =>
    engine.handle('listClientEntries').then((r) => r.data as Schema['ClientEntryVO'][])
  expect((await entries()).find((e) => e.code === 'DAILY_2H')?.sku?.id).toBe('301')
  engine.db.skus.reverse()
  engine.db.skus.find((s) => s.id === '301')!.status = 'OFF_SHELF'
  expect((await entries()).find((e) => e.code === 'DAILY_2H')).toMatchObject({ available: false })
  await write('createAdminCategory', admin, { name: '额外分类', sort: 3, status: 'ON_SHELF' })
  expect(await entries()).toHaveLength(13)
  const {
    id: _id,
    categoryId: _c,
    categoryName: _cn,
    itemName: _i,
    ...dto
  } = engine.db.skus.find((s) => s.id === '302')!
  await expect(
    write('updateAdminSku', admin, { ...dto, clientEntryCode: 'DAILY_2H' }, '302'),
  ).rejects.toMatchObject({ code: 'CONFIG_CONFLICT' })
})
it('个人资料、地址联系人、本次联系人分离，地址和 SKU 编辑不修改历史附件', async () => {
  const image = (
    await engine.handle('uploadSceneImage', { token: customer, file: png(), idempotencyKey: key() })
  ).data as Schema['SceneImageVO']
  const order = (await write('createOrder', customer, {
    skuId: '301',
    addressId: '401',
    bookingType: 'STANDARD',
    startTime: iso(now + 24 * HOUR),
    contactName: '家人',
    contactPhone: '13800000009',
    sceneImageIds: [image.id],
  })) as Schema['OrderVO']
  await write('updateCustomerProfile', customer, { displayName: '新称呼', phone: '13800000008' })
  expect(engine.db.addresses[0].contactName).toBe('演示联系人')
  expect(order.contactName).toBe('家人')
  expect(order.address.contactPhone).toBe('13800000001')
  engine.db.addresses[0].contactName = '新地址联系人'
  engine.db.skus[0].name = '新名称'
  expect(engine.db.orders[0].order.sceneImages).toEqual([image])
  await expect(write('deleteSceneImage', customer, undefined, image.id)).rejects.toMatchObject({
    code: 'RESOURCE_IN_USE',
  })
  await expect(
    write('updateCustomerProfile', worker, { displayName: '越权', phone: '13800000008' }),
  ).rejects.toMatchObject({ code: 'FORBIDDEN' })
})
it('图片幂等、文件与数量限制、所有权及接单前后动态资格校验', async () => {
  const upload = { token: customer, file: png(), idempotencyKey: key() }
  const image = (await engine.handle('uploadSceneImage', upload)).data as Schema['SceneImageVO']
  expect((await engine.handle('uploadSceneImage', upload)).data).toEqual(image)
  await expect(
    engine.handle('uploadSceneImage', {
      ...upload,
      file: new File([new Uint8Array(6 * 1024 * 1024)], 'big.png', { type: 'image/png' }),
    }),
  ).rejects.toMatchObject({ code: 'IMAGE_TOO_LARGE' })
  await expect(
    engine.handle('uploadSceneImage', {
      ...upload,
      file: new File(['bad'], 'bad.svg', { type: 'image/svg+xml' }),
    }),
  ).rejects.toMatchObject({ code: 'IMAGE_TYPE_UNSUPPORTED' })
  await expect(
    engine.handle('customerGetSceneImage', { token: other, id: image.id }),
  ).rejects.toMatchObject({ code: 'NOT_FOUND' })
  const body = {
    skuId: '301',
    addressId: '401',
    bookingType: 'OFFER',
    offerPrice: '145.00',
    startTime: iso(now + 24 * HOUR),
    sceneImageIds: [image.id],
  }
  await expect(
    write('createOrder', customer, { ...body, sceneImageIds: Array(4).fill(image.id) }),
  ).rejects.toMatchObject({ code: 'VALIDATION_ERROR' })
  await expect(
    write('createOrder', customer, { ...body, contactName: '单独字段' }),
  ).rejects.toMatchObject({ code: 'VALIDATION_ERROR' })
  const order = (await write('createOrder', customer, body)) as Schema['OrderVO']
  await write('payOrder', customer, undefined, order.id)
  const request = { token: worker, id: image.id, query: { orderId: order.id } }
  expect((await engine.handle('workerGetSceneImage', request)).data).toEqual(image)
  const offers = (await engine.handle('listEligibleOffers', { token: worker }))
    .data as Schema['OfferPageDTO']
  const offer = offers.list.find((o) => o.id === order.id)!
  expect(offer.sceneImages).toEqual([image])
  expect(offer).not.toHaveProperty('contactPhone')
  expect(offer).not.toHaveProperty('address')
  engine.db.workers[0].dispatchEnabled = false
  await expect(engine.handle('workerGetSceneImage', request)).rejects.toMatchObject({
    code: 'NOT_FOUND',
  })
  expect((await engine.handle('adminGetSceneImage', { ...request, token: admin })).data).toEqual(
    image,
  )
  await expect(
    engine.handle('adminGetSceneImage', { token: admin, id: image.id }),
  ).rejects.toMatchObject({ code: 'VALIDATION_ERROR' })
  await write('cancelCustomerOrder', customer, { reason: '取消' }, order.id)
  engine.db.workers[0].dispatchEnabled = true
  await expect(engine.handle('workerGetSceneImage', request)).rejects.toMatchObject({
    code: 'NOT_FOUND',
  })
})
it('多状态筛选保持精确状态且与单状态互斥，默认地址始终唯一', async () => {
  const page = (
    await engine.handle('customerListOrders', {
      token: customer,
      query: { statuses: ['COMPLETED', 'CANCELLED'], pageSize: 100 },
    })
  ).data as Schema['OrderPageDTO']
  expect(page.total).toBe(24)
  await expect(
    engine.handle('customerListOrders', {
      token: customer,
      query: { statuses: ['COMPLETED'], status: 'CANCELLED' },
    }),
  ).rejects.toMatchObject({ code: 'VALIDATION_ERROR' })
  const { id: _id, customerId: _customer, ...dto } = engine.db.addresses[0]
  const added = (await write('createAddress', customer, {
    ...dto,
    detail: '第二地址',
    isDefault: true,
  })) as Schema['AddressVO']
  await write('updateAddress', customer, { ...dto, isDefault: false }, added.id)
  expect(engine.db.addresses.filter((a) => a.isDefault)).toHaveLength(1)
  await write('deleteAddress', customer, undefined, '401')
  expect(engine.db.addresses[0].isDefault).toBe(true)
})
