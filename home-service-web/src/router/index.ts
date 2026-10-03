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
          component: () => import('../views/customer/AddressesView.vue'),
          meta: { title: '地址簿' },
        },
        {
          path: 'me',
          component: () => import('../views/customer/MyView.vue'),
          meta: { public: true, title: '我的' },
        },
        {
          path: 'profile',
          component: () => import('../views/customer/ProfileView.vue'),
          meta: { title: '个人信息' },
        },
        {
          path: 'rules',
          component: () => import('../views/customer/RulesView.vue'),
          meta: { public: true, title: '预约说明' },
        },
        {
          path: 'development',
          component: () => import('../views/customer/DevelopmentView.vue'),
          meta: { public: true, title: '演示工具' },
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
