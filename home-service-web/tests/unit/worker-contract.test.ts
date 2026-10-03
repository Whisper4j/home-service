import { expect, it } from 'vitest'
import { MockEngine } from '../../src/mock/engine'
import { createDatabase } from '../../src/mock/database'
import type { Schema } from '../../src/api/types'
import { quoteBounds, quoteReason, suggestedPrice } from '../../src/utils/quote'
const now = Date.parse('2026-10-03T09:00:00+08:00')
async function setup() {
  const engine = new MockEngine(createDatabase(now), () => now)
  const login = async (role: 'worker' | 'customer') =>
    (
      (await engine.handle(`${role}Login`, { body: { username: role, password: 'Demo12345' } }))
        .data as Schema['LoginVO']
    ).accessToken
  return { engine, token: await login('worker'), customer: await login('customer') }
}
it('新报价向上取合法最低值，空区间不可优惠；历史快照仍按原锚点', async () => {
  const range = { minimumOfferPrice: '132.50', standardPrice: '160.00' }
  expect(quoteBounds(range)).toEqual({ low: 13500, high: 15500 })
  expect(suggestedPrice(range)).toBe('145.00')
  expect(quoteReason('137.50', range)).not.toBe('')
  expect(quoteReason('137.50', range, 'MINIMUM_ANCHORED')).toBe('')
  expect(suggestedPrice({ ...range, standardPrice: '135.00' })).toBe('')
  const { engine, customer } = await setup()
  engine.db.skus[0].minimumOfferPrice = '132.50'
  const body = {
    skuId: '301',
    addressId: engine.db.addresses[0].id,
    bookingType: 'OFFER',
    startTime: '2026-10-04T09:00:00+08:00',
    offerPrice: '137.50',
  }
  await expect(
    engine.handle('createOrder', { token: customer, body, idempotencyKey: 'illegal_quote' }),
  ).rejects.toMatchObject({ code: 'PRICE_OUT_OF_RANGE' })
  const result = await engine.handle('createOrder', {
    token: customer,
    body: { ...body, offerPrice: '135.00' },
    idempotencyKey: 'valid_quote',
  })
  expect(result.data).toMatchObject({ offerPriceRule: 'MULTIPLE_OF_FIVE', currentPrice: '135.00' })
})
it('全量排序在分页前完成，统计独立于页面并使用关闭时间', async () => {
  const { engine, token } = await setup()
  const rows = engine.db.orders.map((s) => s.order).filter((o) => o.workerId === '201')
  const target = rows.at(-1)!
  target.status = 'IN_SERVICE'
  const first = (await engine.handle('workerListOrders', { token, query: { pageSize: 1 } }))
    .data as Schema['OrderPageDTO']
  expect(first.list[0].id).toBe(target.id)
  const complete = rows.filter((o) => o.status === 'COMPLETED')
  complete[0].closedAt = '2026-10-01T01:00:00+08:00'
  const stats = (await engine.handle('getWorkerStatistics', { token }))
    .data as Schema['WorkerStatisticsVO']
  expect(stats.totalCompletedCount).toBe(complete.length)
  expect(stats.monthCompletedCount).toBe(
    complete.filter((o) => o.closedAt?.startsWith('2026-10')).length,
  )
  expect(stats.totalBookedMinutes).toBe(complete.reduce((n, o) => n + o.service.durationMinutes, 0))
})
it('抢单池稳定排序与月份区间保留服务后缓冲，请假返回冲突订单', async () => {
  const { engine, token } = await setup()
  const second = engine.db.orders[2].order
  second.offerPublishedAt = '2026-10-03T08:00:00+08:00'
  const offers = (await engine.handle('listEligibleOffers', { token, query: { pageSize: 1 } }))
    .data as Schema['OfferPageDTO']
  expect(offers.list[0].id).toBe(second.id)
  await engine.handle('claimOffer', {
    token,
    id: second.id,
    body: { expectedPrice: '130.00', priceVersion: 1 },
    idempotencyKey: 'claim_calendar',
  })
  const calendar = (
    await engine.handle('getWorkerCalendar', { token, query: { month: '2026-10' } })
  ).data as Schema['WorkerCalendarVO']
  const day = calendar.days.find((d) => second.startTime.startsWith(d.date))!
  expect(day.status).toBe('ARRANGED')
  expect(day.segments.filter((s) => s.status === 'SERVICE')).toHaveLength(1)
  expect(day.segments.find((s) => s.status === 'BUFFER')).toMatchObject({
    startTime: second.endTime,
    endTime: second.bufferEndTime,
    orderId: second.id,
  })
  await expect(
    engine.handle('createLeave', {
      token,
      body: { startTime: second.endTime, endTime: second.bufferEndTime, reason: '冲突演示' },
      idempotencyKey: 'leave_conflict',
    }),
  ).rejects.toMatchObject({ code: 'SCHEDULE_CONFLICT', data: { conflictingOrderIds: [second.id] } })
})
it('人员只允许修改本人电话，幂等重试不影响平台字段', async () => {
  const { engine, token, customer } = await setup()
  const request = { token, body: { phone: '13800000999' }, idempotencyKey: 'phone_update' }
  const result = await engine.handle('updateWorkerContact', request)
  expect(await engine.handle('updateWorkerContact', request)).toEqual(result)
  expect(engine.db.accounts.find((a) => a.id === '201')?.phone).toBe('13800000999')
  await expect(
    engine.handle('updateWorkerContact', { ...request, token: customer }),
  ).rejects.toMatchObject({ code: 'FORBIDDEN' })
  await expect(
    engine.handle('updateWorkerContact', {
      ...request,
      body: { phone: '13800000999', dispatchEnabled: true },
    }),
  ).rejects.toMatchObject({ code: 'VALIDATION_ERROR' })
})
