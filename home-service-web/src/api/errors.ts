import type { Schema } from './types'
export class ApiError extends Error {
  constructor(public code: Schema['ErrorCode'] | 'NETWORK_ERROR', message: string, public status = 400, public data: Schema['ErrorDetailsVO'] = {}) {
    super(message)
    this.name = 'ApiError'
  }
}
export function fail(code: Schema['ErrorCode'], message: string, status = 409, data: Schema['ErrorDetailsVO'] = {}): never {
  throw new ApiError(code, message, status, data)
}
export function errorMessage(error: unknown): string {
  if (error instanceof ApiError) return `${error.message}（${error.code}）`
  return error instanceof Error ? error.message : '请求失败，请重试'
}
