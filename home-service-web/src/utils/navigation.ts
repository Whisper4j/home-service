import type { RouteLocationNormalizedLoaded } from 'vue-router'
import { sessions } from '../stores/session'
function sourceKey(id: string) {
  return `booking.source.${sessions.customer?.account.id}.${id}`
}
export function rememberBookingSource(id: string, code: unknown) {
  if (typeof code === 'string' && /^[A-Z][A-Z0-9_]{0,63}$/.test(code))
    sessionStorage.setItem(sourceKey(id), code)
}
export function bookingSource(id: string): string {
  const group = sessionStorage.getItem(sourceKey(id))
  return group && /^[A-Z][A-Z0-9_]{0,63}$/.test(group)
    ? `/customer/groups/${group}`
    : '/customer/home'
}
export function parentRoute(route: RouteLocationNormalizedLoaded): string {
  const path = route.path,
    role = path.startsWith('/worker') ? 'worker' : 'customer'
  if (path.startsWith('/customer/result/')) return bookingSource(String(route.params.id))
  const target = route.query.returnTo
  if (
    typeof target === 'string' &&
    target.startsWith(`/${role}/`) &&
    !target.includes('/login') &&
    target !== route.fullPath
  )
    return target
  if (path.startsWith('/customer/groups/')) return '/customer/home'
  if (path.startsWith('/customer/services/')) {
    const group = route.query.group
    return typeof group === 'string' && /^[A-Z][A-Z0-9_]{0,63}$/.test(group)
      ? `/customer/groups/${group}`
      : '/customer/home'
  }
  if (path.startsWith('/customer/booking/'))
    return typeof route.query.entry === 'string'
      ? `/customer/services/${encodeURIComponent(route.query.entry)}${typeof route.query.group === 'string' ? `?group=${encodeURIComponent(route.query.group)}` : ''}`
      : '/customer/home'
  if (path.startsWith('/customer/pay/')) return `/customer/orders/${route.params.id}`
  if (/\/(customer|worker)\/orders\//.test(path)) return `/${role}/orders`
  return `/${role}/me`
}
export const scrollPositions = new Map<string, number>()
