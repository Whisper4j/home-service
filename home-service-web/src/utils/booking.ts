import type { Schema } from '../api/types'
import { DAY, HOUR, iso } from './format'
export { suggestedPrice, quoteReason } from './quote'
import { quoteBounds } from './quote'
export function timeReason(
  start: number,
  now: number,
  duration: number,
  mode: Schema['BookingType'],
  rules: Schema['BookingRulesVO'],
): string {
  if (!Number.isFinite(start)) return '请先选择预约日期和时间'
  if (start < now + rules.earliestHours * HOUR) return `至少提前${rules.earliestHours}小时`
  if (start > now + rules.latestDays * DAY) return `最多提前${rules.latestDays}天`
  const buffer = mode === 'STANDARD' ? rules.standardBufferMinutes : rules.offerBufferMinutes
  const end = start + (duration + buffer) * 60_000
  if (
    iso(start).slice(0, 10) !== iso(end).slice(0, 10) ||
    iso(start).slice(11, 16) < rules.workStart ||
    iso(end).slice(11, 16) > rules.workEnd
  )
    return '服务和缓冲超出工作时间'
  return ''
}
export function offerReason(
  start: number,
  now: number,
  sku: Schema['SkuVO'],
  rules: Schema['BookingRulesVO'],
): string {
  if (!sku.supportsOffer) return '该服务不支持优惠预约'
  if (quoteBounds(sku).high < quoteBounds(sku).low)
    return '该服务目前没有合法优惠报价，请选择标准预约'
  if (start < now + rules.offerLeadHours * HOUR)
    return `优惠预约需至少提前${rules.offerLeadHours}小时，请重新选时间或明确改为标准预约`
  return ''
}
