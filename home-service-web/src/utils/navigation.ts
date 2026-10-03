import type { RouteLocationNormalizedLoaded } from 'vue-router'
import { clientEntries } from './clientEntries'
export function parentRoute(route: RouteLocationNormalizedLoaded): string {
  const path = route.path,
    role = path.startsWith('/worker') ? 'worker' : 'customer'
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
  if (path.startsWith('/customer/result/')) return '/customer/home'
  if (/\/(customer|worker)\/orders\//.test(path)) return `/${role}/orders`
  return `/${role}/me`
}
export const scrollPositions = new Map<string, number>()
