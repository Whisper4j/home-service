import type { Schema } from '../api/types'
import { cents, money } from './format'
export type PriceRange = Pick<Schema['SkuVO'], 'minimumOfferPrice' | 'standardPrice'>
export function quoteBounds(range: PriceRange, priceStep: string) {
  const step = cents(priceStep),
    minimum = cents(range.minimumOfferPrice),
    standard = cents(range.standardPrice)
  if (
    !Number.isSafeInteger(step) ||
    step <= 0 ||
    !Number.isSafeInteger(minimum) ||
    !Number.isSafeInteger(standard)
  )
    return { low: 0, high: -1 }
  return { low: Math.ceil(minimum / step) * step, high: Math.floor((standard - 1) / step) * step }
}
export function quoteReason(value: string, range: PriceRange, priceStep: string): string {
  if (!/^(0|[1-9][0-9]{0,8})(\.[0-9]{1,2})?$/.test(value)) return '请输入有效金额，最多两位小数'
  const amount = Math.round(Number(value) * 100),
    { low, high } = quoteBounds(range, priceStep)
  if (high < low) return '该服务目前没有合法优惠报价，请选择标准预约'
  if (amount < low || amount > high)
    return `报价须在 ¥${money(low)} 至 ¥${money(high)} 之间，且低于标准价`
  return amount % cents(priceStep) ? `报价必须为 ${priceStep} 元的整数倍` : ''
}
export function suggestedPrice(range: PriceRange, priceStep: string): string {
  const { low, high } = quoteBounds(range, priceStep),
    step = cents(priceStep)
  if (high < low) return ''
  return money(
    Math.max(
      low,
      Math.min(
        high,
        Math.floor((cents(range.minimumOfferPrice) + cents(range.standardPrice)) / 2 / step) * step,
      ),
    ),
  )
}
