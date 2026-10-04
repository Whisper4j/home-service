import type { RouteLocationNormalizedLoaded } from 'vue-router'
import { clientEntries } from './clientEntries'
import { sessions } from '../stores/session'
function sourceKey(id: string) {
  return `booking.source.${sessions.customer?.account.id}.${id}`
}
export function rememberBookingSource(id: string, code: unknown) {
  if (typeof code === 'string' && clientEntries.some((entry) => entry.code === code))
    sessionStorage.setItem(sourceKey(id), code)
}
export function bookingSource(id: string): string {
  const entry = clientEntries.find((entry) => entry.code === sessionStorage.getItem(sourceKey(id)))
  return entry
    ? `/customer/${['daily', 'deep'].includes(entry.group) ? 'cleaning' : 'repair'}/${entry.group}`
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
  if (path.startsWith('/customer/cleaning/')) return '/customer/home'
  if (path.startsWith('/customer/repair/')) return '/customer/repair'
  if (path === '/customer/repair') return '/customer/home'
  if (path.startsWith('/customer/services/')) {
    const entry = clientEntries.find((e) => e.code === route.params.code)
    return entry
      ? `/customer/${['daily', 'deep'].includes(entry.group) ? 'cleaning' : 'repair'}/${entry.group}`
      : '/customer/home'
  }
  if (path.startsWith('/customer/booking/'))
    return typeof route.query.entry === 'string'
      ? `/customer/services/${route.query.entry}`
      : '/customer/home'
  if (path.startsWith('/customer/pay/')) return `/customer/orders/${route.params.id}`
  if (/\/(customer|worker)\/orders\//.test(path)) return `/${role}/orders`
  return `/${role}/me`
}
export const scrollPositions = new Map<string, number>()
