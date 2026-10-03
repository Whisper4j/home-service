import { beforeEach, describe, expect, it } from 'vitest'
import { MockEngine } from '../../src/mock/engine'
import { createDatabase } from '../../src/mock/database'
import type { OperationId, Schema } from '../../src/api/types'
import { routes } from '../../src/api/generated/routes'
import { iso, HOUR, DAY, cents, money } from '../../src/utils/format'
import { netPaid } from '../../src/mock/orders'

const now = Date.parse('2026-10-03T09:00:00+08:00')
let engine: MockEngine
let customer: string, worker: string, worker2: string, admin: string
let counter = 0
const key = () => `test_key_${++counter}`
async function login(role: 'customer' | 'worker' | 'admin', username = role as string) {
  return (
    (await engine.handle(`${role}Login`, { body: { username, password: 'Demo12345' } }))
      .data as Schema['LoginVO']
  ).accessToken
}
async function call(
  op: OperationId,
  token: string,
  body?: unknown,
  id?: string,
  idempotencyKey = key(),
) {
  return (await engine.handle(op, { token, body, id, idempotencyKey })).data
}
beforeEach(async () => {
  engine = new MockEngine(createDatabase(now), () => now)
  customer = await login('customer')
  worker = await login('worker')
  worker2 = await login('worker', 'worker2')
  admin = await login('admin')
})

describe('契约与基础页面', () => {
  it('三端所有 GET 操作均有可验证的成功响应', async () => {
    await call('claimOffer', worker, { expectedPrice: '130.00', priceVersion: 1 }, '10002')
    const tokens = { customer, worker, admin }
    for (const [id, route] of Object.entries(routes)) {
      if (route.method !== 'GET' || route.binary) continue
      const role = route.path.split('/')[1] as keyof typeof tokens
      const query =
        route.query === 'SlotQuery'
          ? { date: '2026-10-04' }
          : route.query === 'WorkerMonthQuery'
            ? { month: '2026-10' }
            : {}
      const resourceId =
        id === 'getCustomerSku' ? '301' : id === 'getAdminWorkerSlots' ? '201' : '10002'
      const response = await engine.handle(id as OperationId, {
        token: tokens[role],
        id: resourceId,
        query,
      })
      expect(response.code, id).toBe('SUCCESS')
    }
  })
  it('分页默认20条、空页与上限', async () => {
    const first = (await engine.handle('customerListOrders', { token: customer }))
      .data as Schema['OrderPageDTO']
    expect(first.list).toHaveLength(20)
    expect(first.total).toBe(27)
    expect(first.pages).toBe(2)
    const empty = (
      await engine.handle('customerListOrders', { token: customer, query: { pageNo: 99 } })
    ).data as Schema['OrderPageDTO']
    expect(empty.list).toHaveLength(0)
    expect(empty.total).toBe(27)
    await expect(
      engine.handle('customerListOrders', { token: customer, query: { pageSize: 101 } }),
    ).rejects.toMatchObject({ code: 'VALIDATION_ERROR' })
    await expect(
      engine.handle('customerListOrders', {
        token: customer,
        query: { from: '2026-10-05', to: '2026-10-03' },
      }),
    ).rejects.toMatchObject({ code: 'VALIDATION_ERROR' })
  })
  it('金额使用整数分；拒绝科学计数、小数精度错误', () => {
    expect(money(cents('0.10') + cents('0.20'))).toBe('0.30')
    expect(() => cents('1e2')).toThrow()
    expect(() => cents('10.1')).toThrow()
  })
})

describe('权限、幂等与隐私', () => {
  it('角色错误、越权订单与开始码均拒绝', async () => {
    const other = await login('customer', 'customer2')
    await expect(engine.handle('adminListOrders', { token: customer })).rejects.toMatchObject({
      code: 'FORBIDDEN',
    })
    await expect(
      engine.handle('customerGetOrder', { token: other, id: '10001' }),
    ).rejects.toMatchObject({ code: 'NOT_FOUND' })
    await expect(
      engine.handle('getStartCode', { token: worker, id: '10002' }),
    ).rejects.toMatchObject({ code: 'FORBIDDEN' })
    await expect(
      engine.handle('workerLogin', { body: { username: 'customer', password: 'Demo12345' } }),
    ).rejects.toMatchObject({ code: 'INVALID_CREDENTIALS' })
  })
  it('池中无敏感信息，仅返回符合条件的订单', async () => {
    const offers = (await engine.handle('listEligibleOffers', { token: worker }))
      .data as Schema['OfferPageDTO']
    expect(offers.total).toBe(2)
    for (const offer of offers.list) {
      expect(offer).not.toHaveProperty('address')
      expect(offer).not.toHaveProperty('customerId')
      expect(offer).not.toHaveProperty('startCode')
    }
    const repair = await login('worker', 'repair')
    expect(
      (
        (await engine.handle('listEligibleOffers', { token: repair }))
          .data as Schema['OfferPageDTO']
      ).total,
    ).toBe(0)
  })
  it('同Key重放不重复扣款，不同载荷返回冲突', async () => {
    const paymentKey = key()
    const result = await call('payOrder', customer, undefined, '10001', paymentKey)
    expect(await call('payOrder', customer, undefined, '10001', paymentKey)).toEqual(result)
    expect(engine.db.histories['10001'].payments).toHaveLength(1)
    const offerKey = key(),
      dto = {
        newPrice: '135.00',
        expectedPrice: '130.00',
        priceVersion: 1,
        confirmSimulatedPayment: true,
      }
    await call('changeOffer', customer, dto, '10002', offerKey)
    await expect(
      call('changeOffer', customer, { ...dto, newPrice: '140.00' }, '10002', offerKey),
    ).rejects.toMatchObject({ code: 'IDEMPOTENCY_CONFLICT' })
  })
  it('账号禁用后旧令牌请求被拒绝，管理员不能禁用自己', async () => {
    await call('setAccountStatus', admin, { status: 'DISABLED' }, '101')
    await expect(engine.handle('customerListOrders', { token: customer })).rejects.toMatchObject({
      code: 'ACCOUNT_DISABLED',
    })
    await expect(
      call('setAccountStatus', admin, { status: 'DISABLED' }, '1'),
    ).rejects.toMatchObject({ code: 'FORBIDDEN' })
  })
})

describe('标准预约及优惠预约', () => {
  it('标准派单成功，完整占用服务120分钟和缓冲120分钟', async () => {
    const order = (await call('payOrder', customer, undefined, '10001')) as Schema['OrderVO']
    expect(order.status).toBe('PENDING_SERVICE')
    expect(order.dispatchStatus).toBe('SUCCEEDED')
    const slots = (
      await engine.handle('getAdminWorkerSlots', {
        token: admin,
        id: order.workerId,
        query: { date: '2026-10-04' },
      })
    ).data as Schema['SlotVO'][]
    expect(slots.filter((s) => s.status === 'SERVICE')).toHaveLength(4)
    expect(slots.filter((s) => s.status === 'BUFFER')).toHaveLength(4)
    await call('cancelCustomerOrder', customer, { reason: '预约调整' }, order.id)
    const freed = (
      await engine.handle('getAdminWorkerSlots', {
        token: admin,
        id: order.workerId,
        query: { date: '2026-10-04' },
      })
    ).data as Schema['SlotVO'][]
    expect(freed.every((s) => s.status === 'AVAILABLE')).toBe(true)
    expect(netPaid(engine, order.id)).toBe(0)
    expect(engine.db.histories[order.id].assignments[0].status).toBe('RELEASED')
  })
  it('加价、降价、过期版本抢单及成交锁价', async () => {
    await call(
      'changeOffer',
      customer,
      {
        newPrice: '140.00',
        expectedPrice: '130.00',
        priceVersion: 1,
        confirmSimulatedPayment: true,
      },
      '10002',
    )
    await expect(
      call('claimOffer', worker, { expectedPrice: '130.00', priceVersion: 1 }, '10002'),
    ).rejects.toMatchObject({
      code: 'PRICE_CHANGED',
      data: { currentPrice: '140.00', priceVersion: 2 },
    })
    await call(
      'changeOffer',
      customer,
      {
        newPrice: '135.00',
        expectedPrice: '140.00',
        priceVersion: 2,
        confirmSimulatedPayment: false,
      },
      '10002',
    )
    expect(netPaid(engine, '10002')).toBe(13500)
    expect(engine.db.histories['10002'].priceHistory.map((h) => h.priceVersion)).toEqual([2, 3])
    const assigned = (await call(
      'claimOffer',
      worker,
      { expectedPrice: '135.00', priceVersion: 3 },
      '10002',
    )) as Schema['OrderVO']
    expect(assigned.dealPrice).toBe('135.00')
    await expect(
      call(
        'changeOffer',
        customer,
        {
          newPrice: '140.00',
          expectedPrice: '135.00',
          priceVersion: 3,
          confirmSimulatedPayment: true,
        },
        '10002',
      ),
    ).rejects.toMatchObject({ code: 'STATE_CONFLICT' })
  })
  it('多人抢同单只有一人成功，不留下双重分配', async () => {
    const results = await Promise.allSettled([
      call('claimOffer', worker, { expectedPrice: '130.00', priceVersion: 1 }, '10002'),
      call('claimOffer', worker2, { expectedPrice: '130.00', priceVersion: 1 }, '10002'),
    ])
    expect(results.filter((r) => r.status === 'fulfilled')).toHaveLength(1)
    expect(engine.db.histories['10002'].assignments).toHaveLength(1)
  })
  it('不同订单争用同一人员的缓冲槽会拒绝，且不留下部分占用', async () => {
    await call('claimOffer', worker, { expectedPrice: '130.00', priceVersion: 1 }, '10002')
    const order = (await call('createOrder', customer, {
      skuId: '301',
      addressId: '401',
      bookingType: 'OFFER',
      startTime: '2026-10-04T11:00:00+08:00',
      offerPrice: '130.00',
    })) as Schema['OrderVO']
    await call('payOrder', customer, undefined, order.id)
    await expect(
      call('claimOffer', worker, { expectedPrice: '130.00', priceVersion: 1 }, order.id),
    ).rejects.toMatchObject({ code: 'WORKER_INELIGIBLE' })
    expect(engine.db.histories[order.id].assignments).toHaveLength(0)
    await call('claimOffer', worker2, { expectedPrice: '130.00', priceVersion: 1 }, order.id)
  })
  it('报价边界与缺少补差确认不产生流水', async () => {
    for (const newPrice of ['125.00', '160.00', '132.00'])
      await expect(
        call(
          'changeOffer',
          customer,
          { newPrice, expectedPrice: '130.00', priceVersion: 1, confirmSimulatedPayment: true },
          '10002',
        ),
      ).rejects.toMatchObject({ code: 'PRICE_OUT_OF_RANGE' })
    await expect(
      call(
        'changeOffer',
        customer,
        {
          newPrice: '135.00',
          expectedPrice: '130.00',
          priceVersion: 1,
          confirmSimulatedPayment: false,
        },
        '10002',
      ),
    ).rejects.toMatchObject({ code: 'VALIDATION_ERROR' })
    expect(engine.db.histories['10002'].payments).toHaveLength(1)
    expect(engine.db.histories['10002'].priceHistory).toHaveLength(0)
  })
  it('预约提前量、对齐、营业尾部缓冲与维修优惠限制', async () => {
    const dto = {
      skuId: '301',
      addressId: '401',
      bookingType: 'STANDARD',
      startTime: '2026-10-04T09:00:00+08:00',
    }
    for (const startTime of [
      '2026-10-03T10:30:00+08:00',
      '2026-10-04T09:15:00+08:00',
      '2026-10-04T19:00:00+08:00',
      '2026-10-11T09:00:00+08:00',
    ])
      await expect(call('createOrder', customer, { ...dto, startTime })).rejects.toMatchObject({
        code: 'BOOKING_WINDOW_INVALID',
      })
    await expect(
      call('createOrder', customer, {
        ...dto,
        skuId: '306',
        bookingType: 'OFFER',
        offerPrice: '130.00',
      }),
    ).rejects.toMatchObject({ code: 'OFFER_NOT_SUPPORTED' })
    await expect(
      call('createOrder', customer, {
        ...dto,
        bookingType: 'OFFER',
        offerPrice: '130.00',
        startTime: '2026-10-03T12:00:00+08:00',
      }),
    ).rejects.toMatchObject({ code: 'OFFER_NOT_SUPPORTED' })
    expect(
      (
        (await call('createOrder', customer, {
          ...dto,
          startTime: iso(now + 2 * HOUR),
        })) as Schema['OrderVO']
      ).status,
    ).toBe('PENDING_PAYMENT')
  })
})

describe('排班、履约与到期任务', () => {
  it('缩短排班、固定休息日及请假不能破坏已分配服务或缓冲', async () => {
    await call('claimOffer', worker, { expectedPrice: '130.00', priceVersion: 1 }, '10002')
    await expect(
      call('updateSchedule', worker, {
        intervals: [{ start: '08:00', end: '11:00' }],
        restWeekdays: [],
      }),
    ).rejects.toMatchObject({ code: 'SCHEDULE_CONFLICT' })
    await expect(
      call('updateSchedule', worker, {
        intervals: [{ start: '08:00', end: '22:00' }],
        restWeekdays: [7],
      }),
    ).rejects.toMatchObject({ code: 'SCHEDULE_CONFLICT' })
    await expect(
      call('createLeave', worker, {
        startTime: '2026-10-04T11:00:00+08:00',
        endTime: '2026-10-04T11:30:00+08:00',
        reason: '缓冲冲突',
      }),
    ).rejects.toMatchObject({ code: 'SCHEDULE_CONFLICT' })
    expect(engine.db.schedules['201'].intervals[0].end).toBe('22:00')
    expect(engine.db.leaves).toHaveLength(0)
  })
  it('拒绝重叠区间，合法请假可撤销，过去请假不能撤销', async () => {
    await expect(
      call('updateSchedule', worker, {
        intervals: [
          { start: '08:00', end: '12:00' },
          { start: '11:30', end: '18:00' },
        ],
        restWeekdays: [],
      }),
    ).rejects.toMatchObject({ code: 'VALIDATION_ERROR' })
    const dto = {
      startTime: '2026-10-04T15:00:00+08:00',
      endTime: '2026-10-04T16:00:00+08:00',
      reason: '演示请假',
    }
    const leave = (await call('createLeave', worker, dto)) as Schema['LeaveVO']
    await call('cancelLeave', worker, undefined, leave.id)
    expect(engine.db.leaves[0].status).toBe('CANCELLED')
    const second = (await call('createLeave', worker, dto)) as Schema['LeaveVO']
    engine.db.clockOffset = DAY + 6 * HOUR
    await expect(call('cancelLeave', worker, undefined, second.id)).rejects.toMatchObject({
      code: 'STATE_CONFLICT',
    })
  })
  it('按序履约、开始码、客户确认和仅一次评价', async () => {
    await call('claimOffer', worker, { expectedPrice: '130.00', priceVersion: 1 }, '10002')
    await expect(call('arriveOrder', worker, undefined, '10002')).rejects.toMatchObject({
      code: 'STATE_CONFLICT',
    })
    await call('departOrder', worker, undefined, '10002')
    await expect(
      call('cancelCustomerOrder', customer, { reason: '不能取消' }, '10002'),
    ).rejects.toMatchObject({ code: 'STATE_CONFLICT' })
    await call('arriveOrder', worker, undefined, '10002')
    await expect(
      call('startOrder', worker, { startCode: '000000' }, '10002'),
    ).rejects.toMatchObject({ code: 'START_CODE_INVALID' })
    const code = (await engine.handle('getStartCode', { token: customer, id: '10002' }))
      .data as Schema['StartCodeVO']
    await expect(call('startOrder', worker, code, '10002')).rejects.toMatchObject({
      code: 'BOOKING_WINDOW_INVALID',
    })
    engine.db.clockOffset = DAY
    await call('startOrder', worker, code, '10002')
    await call('finishOrder', worker, undefined, '10002')
    await call('confirmOrder', customer, undefined, '10002')
    await call(
      'createReview',
      customer,
      { score: 5, tags: ['PUNCTUAL'], content: '准时完成' },
      '10002',
    )
    await expect(
      call('createReview', customer, { score: 4, tags: [], content: '' }, '10002'),
    ).rejects.toMatchObject({ code: 'REVIEW_EXISTS' })
  })
  it('15分钟支付截止、5分钟派单截止和2小时优惠截止准确退款', async () => {
    engine.db.workers.forEach((w) => {
      w.dispatchEnabled = false
    })
    const waiting = (await call('payOrder', customer, undefined, '10001')) as Schema['OrderVO']
    expect(waiting.status).toBe('WAITING_DISPATCH')
    engine.db.clockOffset = 5 * 60_000
    await engine.handle('customerListOrders', { token: customer })
    expect(engine.db.orders.find((s) => s.order.id === '10001')!.order.dispatchStatus).toBe(
      'FAILED',
    )
    expect(netPaid(engine, '10001')).toBe(0)
    engine.db.clockOffset = 2 * HOUR
    await expect(
      call('claimOffer', worker, { expectedPrice: '130.00', priceVersion: 1 }, '10002'),
    ).rejects.toMatchObject({ code: 'OFFER_CLOSED' })
    expect(netPaid(engine, '10002')).toBe(0)
    const created = (await call('createOrder', customer, {
      skuId: '301',
      addressId: '401',
      bookingType: 'STANDARD',
      startTime: '2026-10-04T09:00:00+08:00',
    })) as Schema['OrderVO']
    engine.db.clockOffset += 15 * 60_000
    await expect(call('payOrder', customer, undefined, created.id)).rejects.toMatchObject({
      code: 'STATE_CONFLICT',
    })
    expect(engine.db.histories[created.id].payments).toHaveLength(0)
  })
  it('24小时自动确认完成且不重复流水', async () => {
    await call('claimOffer', worker, { expectedPrice: '130.00', priceVersion: 1 }, '10002')
    await call('departOrder', worker, undefined, '10002')
    await call('arriveOrder', worker, undefined, '10002')
    const code = engine.db.orders.find((s) => s.order.id === '10002')!.startCode
    engine.db.clockOffset = DAY
    await call('startOrder', worker, { startCode: code }, '10002')
    await call('finishOrder', worker, undefined, '10002')
    engine.db.clockOffset += DAY
    const renewed = await login('customer')
    const order = (await engine.handle('customerGetOrder', { token: renewed, id: '10002' }))
      .data as Schema['OrderVO']
    expect(order.status).toBe('COMPLETED')
    expect(engine.db.histories['10002'].payments).toHaveLength(1)
  })
})
