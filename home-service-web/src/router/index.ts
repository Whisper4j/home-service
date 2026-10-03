import { createRouter, createWebHistory } from 'vue-router'
import { sessions } from '../stores/session'
import type { RolePath } from '../api/types'
import { request } from '../api/client'

export const router = createRouter({
  history: createWebHistory(),
  scrollBehavior: (_to, _from, saved) => saved || { top: 0 },
  routes: [
    { path: '/', redirect: '/customer/catalog' },
    ...(['customer', 'worker', 'admin'] as const).map((role) => ({
      path: `/${role}/login`,
      component: () => import('../views/auth/LoginView.vue'),
      props: { role },
      meta: { public: true },
    })),
    {
      path: '/customer/register',
      component: () => import('../views/auth/RegisterView.vue'),
      meta: { public: true },
    },
    {
      path: '/customer',
      component: () => import('../layouts/CustomerLayout.vue'),
      meta: { role: 'customer' },
      redirect: '/customer/catalog',
      children: [
        { path: 'catalog', component: () => import('../views/customer/CatalogView.vue') },
        { path: 'booking/:skuId', component: () => import('../views/customer/BookingView.vue') },
        { path: 'addresses', component: () => import('../views/customer/AddressesView.vue') },
        {
          path: 'orders',
          component: () => import('../views/shared/OrdersView.vue'),
          props: { role: 'customer' },
        },
        {
          path: 'orders/:id',
          component: () => import('../views/shared/OrderDetailView.vue'),
          props: { role: 'customer' },
        },
      ],
    },
    {
      path: '/worker',
      component: () => import('../layouts/WorkerLayout.vue'),
      meta: { role: 'worker' },
      redirect: '/worker/orders',
      children: [
        {
          path: 'orders',
          component: () => import('../views/shared/OrdersView.vue'),
          props: { role: 'worker' },
        },
        {
          path: 'orders/:id',
          component: () => import('../views/shared/OrderDetailView.vue'),
          props: { role: 'worker' },
        },
        { path: 'offers', component: () => import('../views/worker/OffersView.vue') },
        { path: 'schedule', component: () => import('../views/worker/ScheduleView.vue') },
      ],
    },
    {
      path: '/admin',
      component: () => import('../layouts/AdminLayout.vue'),
      meta: { role: 'admin' },
      redirect: '/admin/orders',
      children: [
        {
          path: 'orders',
          component: () => import('../views/shared/OrdersView.vue'),
          props: { role: 'admin' },
        },
        {
          path: 'orders/:id',
          component: () => import('../views/shared/OrderDetailView.vue'),
          props: { role: 'admin' },
        },
        {
          path: 'catalog/:resource',
          component: () => import('../views/admin/CatalogManageView.vue'),
        },
        { path: 'accounts', component: () => import('../views/admin/AccountsView.vue') },
        { path: 'workers', component: () => import('../views/admin/WorkersView.vue') },
        { path: 'records/:resource', component: () => import('../views/admin/RecordsView.vue') },
        { path: 'settings', component: () => import('../views/admin/SettingsView.vue') },
      ],
    },
    {
      path: '/:pathMatch(.*)*',
      component: () => import('../views/shared/NotFoundView.vue'),
      meta: { public: true },
    },
  ],
})
router.beforeEach(async (to) => {
  if (to.meta.public) return true
  const role = to.meta.role as RolePath
  if (!sessions[role]) return { path: `/${role}/login`, query: { redirect: to.fullPath } }
  try {
    await request(
      (
        {
          customer: 'customerCurrentAccount',
          worker: 'workerCurrentAccount',
          admin: 'adminCurrentAccount',
        } as const
      )[role],
    )
    return true
  } catch {
    return { path: `/${role}/login`, query: { redirect: to.fullPath } }
  }
})
