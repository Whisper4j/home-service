import type { Schema } from '../api/types'
import type { MockContext } from './context'
import { dateOf, DAY, iso } from '../utils/format'
import { daySlots } from './scheduling'
const priority: Partial<Record<Schema['OrderStatus'], number>> = {
  IN_SERVICE: 0,
  ARRIVED: 1,
  DEPARTED: 2,
  PENDING_SERVICE: 3,
  PENDING_CONFIRMATION: 4,
  COMPLETED: 5,
  CANCELLED: 5,
}
export function compareWorkerOrders(a: Schema['OrderVO'], b: Schema['OrderVO']): number {
  const rank = (priority[a.status] ?? 6) - (priority[b.status] ?? 6)
  return (
    rank ||
    (priority[a.status] === 5
      ? (b.closedAt || '').localeCompare(a.closedAt || '')
      : a.startTime.localeCompare(b.startTime)) ||
    compareIds(a.id, b.id)
  )
}
export function compareIds(a: string, b: string) {
  return BigInt(a) < BigInt(b) ? -1 : BigInt(a) === BigInt(b) ? 0 : 1
}
export function workerStatistics(
  context: MockContext,
  workerId: string,
): Schema['WorkerStatisticsVO'] {
  const today = dateOf(context.now),
    month = today.slice(0, 7)
  const orders = context.db.orders.map((s) => s.order).filter((o) => o.workerId === workerId)
  const completed = orders.filter((o) => o.status === 'COMPLETED'),
    monthly = completed.filter((o) => o.closedAt?.startsWith(month))
  return {
    asOf: iso(context.now),
    today,
    month,
    todayPendingCount: orders.filter(
      (o) => o.status === 'PENDING_SERVICE' && o.startTime.startsWith(today),
    ).length,
    monthCompletedCount: monthly.length,
    totalCompletedCount: completed.length,
    monthBookedMinutes: monthly.reduce((n, o) => n + o.service.durationMinutes, 0),
    totalBookedMinutes: completed.reduce((n, o) => n + o.service.durationMinutes, 0),
  }
}
export function workerCalendar(
  context: MockContext,
  workerId: string,
  month: string,
): Schema['WorkerCalendarVO'] {
  const schedule = context.db.schedules[workerId],
    days: Schema['CalendarDayVO'][] = []
  for (let i = 0; i < 30; i++) {
    const date = dateOf(context.now + i * DAY)
    if (!date.startsWith(month)) continue
    const segments: Schema['SlotVO'][] = []
    for (const slot of daySlots(context, workerId, date)) {
      const last = segments.at(-1)
      if (
        last &&
        last.status === slot.status &&
        last.orderId === slot.orderId &&
        last.assignmentId === slot.assignmentId &&
        last.bookingType === slot.bookingType &&
        last.endTime === slot.startTime
      )
        last.endTime = slot.endTime
      else segments.push({ ...slot })
    }
    const status = !schedule.configured
      ? 'UNCONFIGURED'
      : segments.some((s) => ['SERVICE', 'BUFFER'].includes(s.status))
        ? 'ARRANGED'
        : segments.some((s) => s.status === 'LEAVE')
          ? 'LEAVE'
          : segments.some((s) => s.status === 'AVAILABLE')
            ? 'AVAILABLE'
            : 'REST'
    days.push({ date, status, segments })
  }
  return { from: dateOf(context.now), to: dateOf(context.now + 29 * DAY), schedule, days }
}
