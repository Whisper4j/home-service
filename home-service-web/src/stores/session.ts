import { reactive } from 'vue'
import type { RolePath, Schema } from '../api/types'
const key = 'home-service.sessions.v2'
function restore(): Partial<Record<RolePath, Schema['LoginVO']>> {
  try {
    const saved = JSON.parse(sessionStorage.getItem(key) || '{}')
    const restored: Partial<Record<RolePath, Schema['LoginVO']>> = {}
    for (const role of ['customer', 'worker', 'admin'] as const) {
      const entry = saved?.[role]
      // 这里只检查会话结构；令牌签名、过期和账号权限必须由后端验证。
      if (
        entry?.account?.id &&
        entry.account.role === role.toUpperCase() &&
        typeof entry.accessToken === 'string' &&
        /^[A-Za-z0-9_-]+\.[A-Za-z0-9_-]+\.[A-Za-z0-9_-]+$/.test(entry.accessToken)
      )
        restored[role] = entry
    }
    return restored
  } catch {
    return {}
  }
}
export const sessions = reactive<Partial<Record<RolePath, Schema['LoginVO']>>>(restore())
export function saveSession(role: RolePath, data: Schema['LoginVO']): void {
  sessions[role] = data
  sessionStorage.setItem(key, JSON.stringify(sessions))
}
export function clearSession(role: RolePath): void {
  delete sessions[role]
  sessionStorage.setItem(key, JSON.stringify(sessions))
}
