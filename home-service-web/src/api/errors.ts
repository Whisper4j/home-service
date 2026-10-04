import type { Schema } from './types'
export class ApiError extends Error {
  constructor(
    public code: Schema['ErrorCode'] | 'NETWORK_ERROR',
    message: string,
    public status = 400,
    public data: Schema['ErrorDetailsVO'] = {},
  ) {
    super(message)
    this.name = 'ApiError'
  }
}
export function errorMessage(error: unknown): string {
  if (error instanceof ApiError)
    return `${error.message}（${error.code}）${error.code === 'PRICE_CHANGED' ? `；最新报价 ¥${error.data.currentPrice}。请重新查询后确认。` : ''}`
  return error instanceof Error ? error.message : '请求失败，请重试'
}
