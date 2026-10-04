import { createRouter, createWebHistory } from 'vue-router'
import { sessions } from '../stores/session'
import type { RolePath } from '../api/types'
import { request } from '../api/client'
import { ApiError } from '../api/errors'

export const router = createRouter({
  history: createWebHistory(),
  scrollBehavior: (_to, _from, saved) => saved || { top: 0 },
  routes: [
    { path: '/', redirect: '/customer/home' },
    ...(['worker', 'admin'] as const).map((role) => ({
      path: `/${role}/login`,
      component: () => import('../views/auth/LoginView.vue'),
      props: { role },
      meta: { public: true },
    })),
    {
      path: '/customer',
      component: () => import('../layouts/CustomerLayout.vue'),
      meta: { role: 'customer' },
      redirect: '/customer/home',
      children: [
        { path: 'catalog', redirect: '/customer/home' },
        {
          path: 'login',
          component: () => import('../views/auth/LoginView.vue'),
          props: { role: 'customer' },
          meta: { public: true, title: '客户登录' },
        },
        {
          path: 'register',
          component: () => import('../views/auth/RegisterView.vue'),
          meta: { public: true, title: '注册账号' },
        },
        {
          path: 'home',
          component: () => import('../views/customer/HomeView.vue'),
          meta: { public: true, title: '首页' },
        },
        {
          path: 'cleaning/:group',
          component: () => import('../views/customer/ServicesView.vue'),
          meta: { public: true, title: '清洁服务' },
        },
        {
          path: 'repair/:group?',
          component: () => import('../views/customer/ServicesView.vue'),
          meta: { public: true, title: '维修服务' },
        },
        {
          path: 'services/:code',
          component: () => import('../views/customer/ServiceDetailView.vue'),
          meta: { public: true, title: '服务详情' },
        },
        {
          path: 'booking/:skuId',
          component: () => import('../views/customer/BookingView.vue'),
          meta: { title: '填写预约信息' },
        },
        {
          path: 'addresses',
          redirect: (to) => {
            const target =
              typeof to.query.returnTo === 'string' &&
              /^\/customer\/booking\/[0-9]+(?:\?|$)/.test(to.query.returnTo)
                ? to.query.returnTo
                : '/customer/me'
            const resolved = new URL(target, 'https://local.invalid')
            return {
              path: resolved.pathname,
              query: {
                ...Object.fromEntries(resolved.searchParams),
                panel: to.query.add === '1' ? 'address-add' : 'addresses',
              },
            }
          },
        },
        {
          path: 'me',
          component: () => import('../views/customer/MyView.vue'),
          meta: { public: true, title: '我的' },
        },
        { path: 'profile', redirect: '/customer/me?panel=profile' },
        { path: 'rules', redirect: '/customer/me?panel=rules' },
        {
          path: 'development',
          component: () => import('../views/customer/DevelopmentView.vue'),
          meta: { public: true, title: '演示工具' },
        },
        {
          path: 'result/:id',
          component: () => import('../views/customer/PaymentResultView.vue'),
          meta: { title: '支付结果' },
        },
        {
          path: 'pay/:id',
          component: () => import('../views/customer/PaymentView.vue'),
          meta: { title: '确认模拟支付' },
        },
        {
          path: 'orders',
          component: () => import('../views/customer/OrdersView.vue'),
          meta: { title: '我的订单' },
        },
        {
          path: 'orders/:id',
          component: () => import('../views/customer/OrderDetailView.vue'),
          meta: { title: '预约进度' },
        },
      ],
    },
    {
      path: '/worker',
      component: () => import('../layouts/WorkerLayout.vue'),
      meta: { role: 'worker' },
      redirect: '/worker/home',
      children: [
        {
          path: 'orders',
          component: () => import('../views/worker/OrdersView.vue'),
          meta: { title: '我的订单' },
        },
        {
          path: 'orders/:id',
          component: () => import('../views/worker/OrderDetailView.vue'),
          meta: { title: '任务详情' },
        },
        { path: 'offers', redirect: '/worker/home' },
        {
          path: 'home',
          component: () => import('../views/worker/HomeView.vue'),
          meta: { title: '人员首页' },
        },
        {
          path: 'me',
          component: () => import('../views/worker/MyView.vue'),
          meta: { title: '我的' },
        },
        { path: 'profile', redirect: '/worker/me?panel=profile' },
        { path: 'statistics', redirect: '/worker/me' },
        { path: 'rules', redirect: '/worker/me?panel=rules' },
        { path: 'schedule', redirect: '/worker/me?panel=schedule' },
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
  const privateCustomerPanel =
    to.path === '/customer/me' && ['profile', 'addresses'].includes(String(to.query.panel))
  if (to.meta.public && !privateCustomerPanel) return true
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
  } catch (error) {
    if (
      error instanceof ApiError &&
      (error.status === 401 || error.code === 'ACCOUNT_DISABLED' || error.code === 'FORBIDDEN')
    )
      return { path: `/${role}/login`, query: { redirect: to.fullPath } }
    // 暂时断网不抹掉登录或草稿；页面请求呈现可重试错误。
    return true
  }
})
