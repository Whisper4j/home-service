import { reactive } from 'vue'
import type { RolePath, Schema } from '../api/types'
const key = 'home-service.sessions.v1'
function restore(): Partial<Record<RolePath, Schema['LoginVO']>> {
  try { return JSON.parse(sessionStorage.getItem(key) || '{}') } catch { return {} }
}
export const sessions = reactive<Partial<Record<RolePath, Schema['LoginVO']>>>(restore())
export function saveSession(role: RolePath, data: Schema['LoginVO']): void {
  sessions[role] = data; sessionStorage.setItem(key, JSON.stringify(sessions))
}
export function clearSession(role: RolePath): void {
  delete sessions[role]; sessionStorage.setItem(key, JSON.stringify(sessions))
}
export function clearAllSessions(): void {
  for (const role of ['customer', 'worker', 'admin'] as const) delete sessions[role]
  sessionStorage.removeItem(key)
}
