import { expect, it } from 'vitest'
import { bookingRules, createDatabase } from '../../src/mock/database'
import { offerReason, quoteReason, suggestedPrice, timeReason } from '../../src/utils/booking'
import { DAY, HOUR } from '../../src/utils/format'
const now = Date.parse('2026-10-03T09:00:00+08:00'),
  db = createDatabase(now),
  rules = bookingRules(db),
  sku = db.skus[0]
it('候选时间严格遵守提前量、最远窗口、时长和各预约方式缓冲', () => {
  expect(timeReason(now + 2 * HOUR, now, 120, 'STANDARD', rules)).toBe('')
  expect(timeReason(now + 2 * HOUR - 1, now, 120, 'STANDARD', rules)).toContain('提前')
  expect(timeReason(now + 7 * DAY + 1, now, 120, 'STANDARD', rules)).toContain('最多')
  const evening = Date.parse('2026-10-04T18:30:00+08:00')
  expect(timeReason(evening, now, 120, 'STANDARD', rules)).toContain('缓冲')
  expect(timeReason(evening, now, 120, 'OFFER', rules)).toBe('')
  expect(timeReason(NaN, now, 120, 'STANDARD', rules)).toContain('选择')
})
it('新订单建议价为合法5元整数倍且不改变客户预约方式', () => {
  expect(suggestedPrice(sku)).toBe('145.00')
  expect(quoteReason('130.00', sku)).toBe('')
  expect(quoteReason('155', sku)).toBe('')
  expect(quoteReason('131', sku)).toContain('5元')
  expect(quoteReason('160', sku)).toContain('低于标准价')
  expect(quoteReason('-5', sku)).toContain('有效金额')
  expect(quoteReason('130.001', sku)).toContain('两位小数')
  expect(offerReason(now + 12 * HOUR - 1, now, sku, rules)).toContain('重新选时间')
  expect(offerReason(now + 12 * HOUR, now, sku, rules)).toBe('')
  expect(offerReason(now + DAY, now, db.skus[5], rules)).toContain('不支持')
})
