import type { Schema } from '../api/types'
import { fail } from '../api/errors'
import { dateOf, DAY, HOUR, iso, SLOT } from '../utils/format'
import type { MockContext } from './context'

export function isWorking(context: MockContext, workerId: string, time: number): boolean {
  const template = context.db.schedules[workerId]
  if (!template?.configured) return false
  const local = new Date(time + 8 * HOUR)
  const weekday = local.getUTCDay() || 7
  const start = iso(time).slice(11, 16)
  const end = iso(time + SLOT).slice(11, 16)
  return (
    !template.restWeekdays.includes(weekday) &&
    template.intervals.some((i) => start >= i.start && end <= i.end)
  )
}
export function slotAt(context: MockContext, workerId: string, time: number): Schema['SlotVO'] {
  const slot: Schema['SlotVO'] = {
    startTime: iso(time),
    endTime: iso(time + SLOT),
    status: isWorking(context, workerId, time) ? 'AVAILABLE' : 'NON_WORKING',
  }
  const assigned = context.db.orders.find(
    ({ order: o }) =>
      o.workerId === workerId &&
      o.status !== 'CANCELLED' &&
      time >= Date.parse(o.startTime) &&
      time < Date.parse(o.bufferEndTime),
  )?.order
  if (assigned) {
    const assignment = context.db.histories[assigned.id].assignments.find(
      (a) => a.status !== 'RELEASED',
    )
    if (assignment)
      return {
        ...slot,
        status: time < Date.parse(assigned.endTime) ? 'SERVICE' : 'BUFFER',
        bookingType: assigned.bookingType,
        assignmentId: assignment.id,
        orderId: assigned.id,
      }
  }
  if (
    context.db.leaves.some(
      (l) =>
        l.workerId === workerId &&
        l.status === 'ACTIVE' &&
        time >= Date.parse(l.startTime) &&
        time < Date.parse(l.endTime),
    )
  )
    slot.status = 'LEAVE'
  return slot
}
export function daySlots(context: MockContext, workerId: string, date: string): Schema['SlotVO'][] {
  if (date < dateOf(context.now) || date >= dateOf(context.now + 30 * DAY))
    fail('BOOKING_WINDOW_INVALID', '请选择未来30天内的日期', 422)
  const start = Date.parse(`${date}T08:00:00+08:00`)
  return Array.from({ length: 28 }, (_, i) => slotAt(context, workerId, start + i * SLOT))
}
export function canAssign(
  context: MockContext,
  worker: Schema['WorkerVO'],
  order: Schema['OrderVO'],
): boolean {
  if (
    context.db.accounts.find((a) => a.id === worker.accountId)?.status !== 'ENABLED' ||
    !worker.dispatchEnabled ||
    worker.cityCode !== order.address.cityCode ||
    !order.service.skillIds.every((s) => worker.skillIds.includes(s))
  )
    return false
  for (let t = Date.parse(order.startTime); t < Date.parse(order.bufferEndTime); t += SLOT)
    if (slotAt(context, worker.id, t).status !== 'AVAILABLE') return false
  return true
}
export function updateSchedule(
  context: MockContext,
  workerId: string,
  dto: Schema['ScheduleDTO'],
): Schema['ScheduleVO'] {
  const intervals = [...dto.intervals].sort((a, b) => a.start.localeCompare(b.start))
  intervals.forEach((i, index) => {
    if (
      i.start < '08:00' ||
      i.end > '22:00' ||
      i.start >= i.end ||
      (index > 0 && intervals[index - 1].end > i.start)
    )
      fail('VALIDATION_ERROR', '工作区间须在08:00—22:00内且不重叠', 400)
  })
  const previous = context.db.schedules[workerId]
  context.db.schedules[workerId] = { configured: true, intervals, restWeekdays: dto.restWeekdays }
  for (const { order } of context.db.orders) {
    if (
      order.workerId !== workerId ||
      order.status === 'CANCELLED' ||
      Date.parse(order.bufferEndTime) <= context.now
    )
      continue
    for (
      let t = Math.max(Date.parse(order.startTime), Math.floor(context.now / SLOT) * SLOT);
      t < Date.parse(order.bufferEndTime);
      t += SLOT
    ) {
      if (!isWorking(context, workerId, t)) {
        context.db.schedules[workerId] = previous
        fail('SCHEDULE_CONFLICT', `排班变更影响订单 ${order.id} 的服务或缓冲时间`, 409, {
          conflictingOrderIds: [order.id],
        })
      }
    }
  }
  return context.db.schedules[workerId]
}
export function createLeave(
  context: MockContext,
  workerId: string,
  dto: Schema['LeaveDTO'],
): Schema['LeaveVO'] {
  const start = Date.parse(dto.startTime),
    end = Date.parse(dto.endTime)
  if (
    start % SLOT ||
    end % SLOT ||
    start < context.now + 2 * HOUR ||
    start >= end ||
    dateOf(end - 1) >= dateOf(context.now + 30 * DAY)
  )
    fail('BOOKING_WINDOW_INVALID', '请假须半小时对齐、至少提前2小时，且在未来30天窗口内', 422)
  for (let t = start; t < end; t += SLOT) {
    const slot = slotAt(context, workerId, t),
      status = slot.status
    if (status === 'SERVICE' || status === 'BUFFER' || status === 'LEAVE')
      fail(
        'SCHEDULE_CONFLICT',
        slot.orderId ? `请假与订单 ${slot.orderId} 的服务或缓冲冲突` : '请假与已有请假冲突',
        409,
        slot.orderId ? { conflictingOrderIds: [slot.orderId] } : {},
      )
  }
  const leave: Schema['LeaveVO'] = { id: context.nextId(), ...dto, status: 'ACTIVE' }
  context.db.leaves.unshift({ ...leave, workerId })
  return leave
}
