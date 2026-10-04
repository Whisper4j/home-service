import { describe, expect, it } from 'vitest'
import { quoteBounds, quoteReason, suggestedPrice } from '../../src/utils/quote'
import { cents, money, iso } from '../../src/utils/format'

describe('整数分与接口步长的报价边界', () => {
  it('最低价向上对齐，严格排除标准价', () => {
    expect(quoteBounds({ minimumOfferPrice: '11.01', standardPrice: '20.00' }, '5.00')).toEqual({
      low: 1500,
      high: 1500,
    })
  })
  it('读取调用方传入的步长，不内置固定步长', () => {
    expect(quoteBounds({ minimumOfferPrice: '11.01', standardPrice: '20.00' }, '2.00')).toEqual({
      low: 1200,
      high: 1800,
    })
  })
  it('合法范围为空不建议报价', () => {
    expect(suggestedPrice({ minimumOfferPrice: '19.01', standardPrice: '20.00' }, '5.00')).toBe('')
  })
  it('非法输入不静默修正', () => {
    const range = { minimumOfferPrice: '10.00', standardPrice: '30.00' }
    for (const input of ['', '-1', 'NaN', '1e2', '010', '10.001', '11', '30'])
      expect(quoteReason(input, range, '5.00')).not.toBe('')
    expect(quoteReason('15', range, '5.00')).toBe('')
  })
  it('建议报价在范围内且为传入步长整数倍', () => {
    for (const step of ['2.00', '5.00', '7.00']) {
      const range = { minimumOfferPrice: '10.01', standardPrice: '90.00' }
      expect(quoteReason(suggestedPrice(range, step), range, step)).toBe('')
    }
  })
  it('整数分转换不丢失小数精度', () => {
    expect(cents('999999999.99')).toBe(99999999999)
    expect(money(cents('0.29'))).toBe('0.29')
    expect(() => cents('1.001')).toThrow()
    expect(() => money(-1)).toThrow()
  })
  it('时间显示显式使用北京时间偏移', () => {
    expect(iso(Date.parse('2026-01-01T20:00:00Z'))).toBe('2026-01-02T04:00:00+08:00')
  })
})
