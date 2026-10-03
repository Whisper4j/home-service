import { routes } from './generated/routes'
import type { OperationId, Output, RequestOptions, RolePath, Schema } from './types'
import { ApiError } from './errors'
import { clearSession, sessions } from '../stores/session'

export const useMock = import.meta.env.VITE_USE_MOCK !== 'false'
export function createIdempotencyKey(): string {
  return crypto.randomUUID()
}
export async function request<K extends OperationId>(
  operation: K,
  options: RequestOptions<K> = {},
): Promise<Output<K>> {
  const route = routes[operation],
    role = route.path.split('/')[1] as RolePath
  const token = sessions[role]?.accessToken
  const idempotencyKey = route.idempotent
    ? options.idempotencyKey || createIdempotencyKey()
    : undefined
  try {
    if (useMock) {
      const { mockRequest } = await import('../mock/transport')
      const result = await mockRequest(operation, {
        id: options.id,
        body: options.body,
        query: options.query as Record<string, unknown>,
        token,
        idempotencyKey,
      })
      return result.data as Output<K>
    }
    if (route.path.includes('{id}') && !options.id)
      throw new ApiError('VALIDATION_ERROR', '缺少资源 ID', 400)
    const path = route.path.replace('{id}', encodeURIComponent(options.id || ''))
    const query = new URLSearchParams()
    for (const [key, value] of Object.entries(options.query || {}))
      if (value !== '' && value !== undefined && value !== null) query.set(key, String(value))
    const controller = new AbortController()
    const timeout = setTimeout(() => controller.abort(), 15_000)
    const abort = () => controller.abort()
    options.signal?.addEventListener('abort', abort, { once: true })
    try {
      const response = await fetch(`/api${path}${query.size ? `?${query}` : ''}`, {
        method: route.method,
        headers: {
          ...(options.body ? { 'Content-Type': 'application/json' } : {}),
          ...(token && !route.anonymous ? { Authorization: `Bearer ${token}` } : {}),
          ...(idempotencyKey ? { 'Idempotency-Key': idempotencyKey } : {}),
        },
        body: options.body ? JSON.stringify(options.body) : undefined,
        signal: controller.signal,
      })
      const envelope = (await response.json()) as {
        code: string
        message: string
        data: Output<K> | Schema['ErrorDetailsVO']
      }
      if (!response.ok || envelope.code !== 'SUCCESS')
        throw new ApiError(
          envelope.code as Schema['ErrorCode'],
          envelope.message || '请求失败',
          response.status,
          envelope.data as Schema['ErrorDetailsVO'],
        )
      return envelope.data as Output<K>
    } finally {
      clearTimeout(timeout)
      options.signal?.removeEventListener('abort', abort)
    }
  } catch (error) {
    if (error instanceof ApiError) {
      if (error.status === 401 || error.code === 'ACCOUNT_DISABLED') {
        clearSession(role)
        if (!route.anonymous)
          window.dispatchEvent(new CustomEvent('session-expired', { detail: role }))
      }
      throw error
    }
    throw new ApiError('NETWORK_ERROR', '无法连接服务或请求超时，请检查网络后重试', 0)
  }
}
