import type { Schema } from '../api/types'
import { cents, money } from './format'
export type PriceRange = Pick<Schema['SkuVO'], 'minimumOfferPrice' | 'standardPrice'>
export function quoteBounds(
  range: PriceRange,
  rule: Schema['OfferPriceRule'] = 'MULTIPLE_OF_FIVE',
) {
  const minimum = cents(range.minimumOfferPrice),
    standard = cents(range.standardPrice)
  const low = rule === 'MINIMUM_ANCHORED' ? minimum : Math.ceil(minimum / 500) * 500
  return { low, high: low + Math.floor((standard - 1 - low) / 500) * 500 }
}
export function quoteReason(
  value: string,
  range: PriceRange,
  rule: Schema['OfferPriceRule'] = 'MULTIPLE_OF_FIVE',
): string {
  if (!/^(0|[1-9][0-9]{0,8})(\.[0-9]{1,2})?$/.test(value)) return '请输入有效金额，最多两位小数'
  const amount = Math.round(Number(value) * 100),
    { low, high } = quoteBounds(range, rule)
  if (high < low) return '该服务目前没有合法优惠报价，请选择标准预约'
  if (amount < low || amount > high)
    return `报价须在 ¥${money(low)} 至 ¥${money(high)} 之间，且低于标准价`
  return (amount - low) % 500
    ? rule === 'MINIMUM_ANCHORED'
      ? '本历史订单须从最低价起按5元步长调整'
      : '报价必须为5元整数倍'
    : ''
}
export function suggestedPrice(range: PriceRange): string {
  const { low, high } = quoteBounds(range)
  if (high < low) return ''
  return money(
    Math.max(
      low,
      Math.min(
        high,
        Math.floor((cents(range.minimumOfferPrice) + cents(range.standardPrice)) / 2 / 500) * 500,
      ),
    ),
  )
}
