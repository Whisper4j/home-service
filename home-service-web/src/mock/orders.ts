import type { Schema } from '../api/types'
import { fail } from '../api/errors'
import { cents, dateOf, DAY, HOUR, iso, money, SLOT } from '../utils/format'
import type { MockContext } from './context'
import { serviceSnapshot } from './database'
import { canAssign } from './scheduling'

export function addPayment(
  context: MockContext,
  order: Schema['OrderVO'],
  type: Schema['PaymentType'],
  amount: string,
  actorId?: string,
): void {
  const id = context.nextId()
  context.db.histories[order.id].payments.push({
    id,
    orderId: order.id,
    businessNo: `${type}_${id}`,
    type,
    amount,
    createdAt: iso(context.now),
  })
  if (type === 'PARTIAL_REFUND' || type === 'FULL_REFUND')
    context.audit(actorId, type, order.id, `模拟退款 ${amount} 元，业务号 ${type}_${id}`)
}
export function netPaid(context: MockContext, orderId: string): number {
  return context.db.histories[orderId].payments.reduce(
    (total, p) => total + (p.type === 'PAYMENT' || p.type === 'TOP_UP' ? 1 : -1) * cents(p.amount),
    0,
  )
}
export function cancelOrder(
  context: MockContext,
  order: Schema['OrderVO'],
  reason: string,
  actorId?: string,
): void {
  const net = netPaid(context, order.id)
  if (net > 0) {
    addPayment(context, order, 'FULL_REFUND', money(net), actorId)
    order.paymentStatus = 'REFUNDED'
  }
  order.status = 'CANCELLED'
  order.cancellationReason = reason
  if (order.dispatchStatus === 'PENDING') order.dispatchStatus = 'FAILED'
  context.db.histories[order.id].assignments.forEach((a) => {
    if (a.status === 'ACTIVE') a.status = 'RELEASED'
  })
  context.event('ORDER_CLOSED', order, reason)
}
export function assignOrder(
  context: MockContext,
  order: Schema['OrderVO'],
  worker: Schema['WorkerVO'],
): void {
  if (!canAssign(context, worker, order)) fail('SLOT_CONFLICT', '人员时间槽已变化，请刷新后重试')
  order.workerId = worker.id
  order.workerName = worker.displayName
  order.dealPrice = order.currentPrice
  order.status = 'PENDING_SERVICE'
  if (order.bookingType === 'STANDARD') order.dispatchStatus = 'SUCCEEDED'
  context.db.histories[order.id].assignments.push({
    id: context.nextId(),
    orderId: order.id,
    workerId: worker.id,
    workerName: worker.displayName,
    bookingType: order.bookingType,
    status: 'ACTIVE',
    assignedAt: iso(context.now),
  })
  context.event(order.bookingType === 'STANDARD' ? 'DISPATCH_SUCCEEDED' : 'ORDER_CLAIMED', order)
}
function workload(context: MockContext, workerId: string, day: string): [number, number] {
  const orders = context.db.orders
    .map((s) => s.order)
    .filter(
      (o) => o.workerId === workerId && o.startTime.startsWith(day) && o.status !== 'CANCELLED',
    )
  return [orders.reduce((n, o) => n + o.service.durationMinutes, 0), orders.length]
}
export function tryDispatch(context: MockContext, order: Schema['OrderVO']): void {
  const candidates = [...context.db.workers].sort((a, b) => {
    const x = workload(context, a.id, order.startTime.slice(0, 10)),
      y = workload(context, b.id, order.startTime.slice(0, 10))
    return x[0] - y[0] || x[1] - y[1] || (BigInt(a.id) < BigInt(b.id) ? -1 : 1)
  })
  let assigned = false
  for (const worker of candidates.slice(0, 10)) {
    const valid = canAssign(context, worker, order)
    const [serviceMinutes, orderCount] = workload(context, worker.id, order.startTime.slice(0, 10))
    context.db.attempts.unshift({
      id: context.nextId(),
      orderId: order.id,
      workerId: worker.id,
      result: valid ? 'ASSIGNED' : 'INELIGIBLE',
      reason: valid
        ? '资格与全部服务/缓冲槽符合，按负载排序分配'
        : '账号、派单开关、技能、城市或完整时间槽不符合',
      serviceMinutes,
      orderCount,
      createdAt: iso(context.now),
    })
    if (valid) {
      assignOrder(context, order, worker)
      assigned = true
      break
    }
  }
  if (!assigned && candidates.length === 0)
    context.db.attempts.unshift({
      id: context.nextId(),
      orderId: order.id,
      result: 'NO_CANDIDATE',
      reason: '平台暂无服务人员',
      createdAt: iso(context.now),
    })
  context.db.orders.find((o) => o.order.id === order.id)!.lastDispatchAt = context.now
}
export function runDueJobs(context: MockContext): void {
  for (const stored of context.db.orders) {
    const o = stored.order
    if (o.status === 'PENDING_PAYMENT' && context.now >= Date.parse(o.paymentDeadline))
      cancelOrder(context, o, '15分钟内未支付，自动取消')
    else if (o.status === 'WAITING_ACCEPTANCE' && context.now >= Date.parse(o.offerDeadline!))
      cancelOrder(context, o, '优惠接单截止，自动全额退款')
    else if (o.status === 'WAITING_DISPATCH') {
      if (context.now >= Date.parse(o.dispatchDeadline!)) {
        cancelOrder(context, o, '等待派单超过5分钟，自动全额退款')
        context.event('DISPATCH_FAILED', o, o.cancellationReason)
      } else if (context.now - (stored.lastDispatchAt || 0) >= 30_000) tryDispatch(context, o)
    } else if (
      o.status === 'PENDING_CONFIRMATION' &&
      context.now >= Date.parse(o.confirmationDeadline!)
    )
      completeOrder(context, o)
  }
}
export function completeOrder(context: MockContext, order: Schema['OrderVO']): void {
  order.status = 'COMPLETED'
  context.db.histories[order.id].assignments.forEach((a) => {
    if (a.status === 'ACTIVE') a.status = 'FINISHED'
  })
  context.event('ORDER_STATUS_CHANGED', order)
}
export function createOrder(
  context: MockContext,
  accountId: string,
  dto: Schema['CreateOrderDTO'],
): Schema['OrderVO'] {
  const sku = context.db.skus.find((s) => s.id === dto.skuId)
  if (
    !sku ||
    sku.status !== 'ON_SHELF' ||
    context.db.items.find((i) => i.id === sku.itemId)?.status !== 'ON_SHELF' ||
    context.db.categories.find((c) => c.id === sku.categoryId)?.status !== 'ON_SHELF'
  )
    fail('CATALOG_UNAVAILABLE', '该服务已下架或不存在', 422)
  const address = context.db.addresses.find(
    (a) => a.id === dto.addressId && a.customerId === accountId,
  )
  if (!address) fail('NOT_FOUND', '地址不存在', 404)
  const start = Date.parse(dto.startTime),
    end = start + sku.durationMinutes * 60_000,
    bufferEnd = end + (dto.bookingType === 'STANDARD' ? 2 : 1) * HOUR
  if (
    start % SLOT ||
    start < context.now + context.db.settings.earliestHours * HOUR ||
    start > context.now + context.db.settings.latestDays * DAY ||
    iso(start).slice(11, 16) < '08:00' ||
    dateOf(start) !== dateOf(bufferEnd) ||
    iso(bufferEnd).slice(11, 16) > '22:00'
  )
    fail(
      'BOOKING_WINDOW_INVALID',
      '预约须半小时对齐且在预约窗口内，服务及尾部缓冲须完整落在08:00—22:00',
      422,
    )
  if (dto.bookingType === 'OFFER' && (!sku.supportsOffer || start < context.now + 12 * HOUR))
    fail('OFFER_NOT_SUPPORTED', '本规格或预约提前量不支持优惠预约', 422)
  if (dto.bookingType === 'STANDARD' && dto.offerPrice !== undefined)
    fail('VALIDATION_ERROR', '标准预约不接受优惠报价', 400)
  if (
    dto.bookingType === 'OFFER' &&
    (!dto.offerPrice ||
      cents(dto.offerPrice) < cents(sku.minimumOfferPrice) ||
      cents(dto.offerPrice) >= cents(sku.standardPrice) ||
      (cents(dto.offerPrice) - cents(sku.minimumOfferPrice)) % 500)
  )
    fail('PRICE_OUT_OF_RANGE', '报价须在最低价至标准价之间，按最低价起每5元递增且低于标准价', 422)
  const { id: _id, customerId: _customerId, ...addressSnapshot } = address
  const order: Schema['OrderVO'] = {
    id: context.nextId(),
    customerId: accountId,
    skuId: sku.id,
    bookingType: dto.bookingType,
    status: 'PENDING_PAYMENT',
    paymentStatus: 'UNPAID',
    dispatchStatus: 'NOT_REQUIRED',
    service: serviceSnapshot(sku),
    address: structuredClone(addressSnapshot),
    startTime: iso(start),
    endTime: iso(end),
    bufferEndTime: iso(bufferEnd),
    createdAt: iso(context.now),
    paymentDeadline: iso(context.now + HOUR / 4),
    currentPrice: dto.bookingType === 'OFFER' ? dto.offerPrice! : sku.standardPrice,
    priceVersion: 1,
    remark: dto.remark || '',
    reviewed: false,
  }
  context.db.orders.unshift({
    order,
    startCode: String(100000 + Math.floor(Math.random() * 900000)),
  })
  context.db.histories[order.id] = { payments: [], priceHistory: [], assignments: [] }
  return order
}
export function payOrder(context: MockContext, order: Schema['OrderVO']): Schema['OrderVO'] {
  if (order.status !== 'PENDING_PAYMENT') fail('STATE_CONFLICT', '当前订单不能支付')
  addPayment(context, order, 'PAYMENT', order.currentPrice)
  order.paymentStatus = 'PAID'
  if (order.bookingType === 'STANDARD') {
    order.status = 'WAITING_DISPATCH'
    order.dispatchStatus = 'PENDING'
    order.dispatchDeadline = iso(context.now + 5 * 60_000)
    tryDispatch(context, order)
  } else {
    order.status = 'WAITING_ACCEPTANCE'
    order.offerDeadline = iso(
      Math.min(context.now + 2 * HOUR, Date.parse(order.startTime) - 6 * HOUR),
    )
    context.event('OFFER_CREATED', order)
  }
  return order
}
export function assertPrice(
  order: Schema['OrderVO'],
  expectedPrice: string,
  version: number,
): void {
  if (order.currentPrice !== expectedPrice || order.priceVersion !== version)
    fail('PRICE_CHANGED', '报价已变化，请查看最新价格并重新确认', 409, {
      currentPrice: order.currentPrice,
      priceVersion: order.priceVersion,
    })
}
export function changeOffer(
  context: MockContext,
  order: Schema['OrderVO'],
  dto: Schema['ChangeOfferDTO'],
): Schema['OrderVO'] {
  if (order.status !== 'WAITING_ACCEPTANCE')
    fail('STATE_CONFLICT', '仅未接单且未截止的优惠订单可调价')
  assertPrice(order, dto.expectedPrice, dto.priceVersion)
  const diff = cents(dto.newPrice) - cents(order.currentPrice)
  if (
    !diff ||
    diff % 500 ||
    cents(dto.newPrice) < cents(order.service.minimumOfferPrice) ||
    cents(dto.newPrice) >= cents(order.service.standardPrice)
  )
    fail('PRICE_OUT_OF_RANGE', '加减价须为5元倍数，且保持在订单快照的优惠范围内', 422)
  if (diff > 0 && !dto.confirmSimulatedPayment)
    fail('VALIDATION_ERROR', '请确认模拟补差后再加价', 400)
  addPayment(
    context,
    order,
    diff > 0 ? 'TOP_UP' : 'PARTIAL_REFUND',
    money(Math.abs(diff)),
    order.customerId,
  )
  context.db.histories[order.id].priceHistory.push({
    id: context.nextId(),
    orderId: order.id,
    previousPrice: order.currentPrice,
    newPrice: dto.newPrice,
    priceVersion: order.priceVersion + 1,
    createdAt: iso(context.now),
  })
  order.currentPrice = dto.newPrice
  order.priceVersion++
  if (diff < 0) order.paymentStatus = 'PARTIALLY_REFUNDED'
  context.event('OFFER_PRICE_CHANGED', order)
  return order
}
export function offerView(order: Schema['OrderVO']): Schema['OfferVO'] {
  return {
    id: order.id,
    skuName: order.service.skuName,
    districtName: order.address.districtName,
    cityCode: order.address.cityCode,
    durationMinutes: order.service.durationMinutes,
    startTime: order.startTime,
    endTime: order.endTime,
    bufferEndTime: order.bufferEndTime,
    currentPrice: order.currentPrice,
    priceVersion: order.priceVersion,
    offerDeadline: order.offerDeadline!,
    description: order.service.description,
    included: order.service.included,
    excluded: order.service.excluded,
    customerSuppliesParts: order.service.customerSuppliesParts,
  }
}
