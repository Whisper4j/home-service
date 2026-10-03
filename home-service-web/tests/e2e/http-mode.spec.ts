import { expect, test } from '@playwright/test'
import { routes } from '../../src/api/generated/routes'
import type { OperationId } from '../../src/api/types'
import { MockEngine } from '../../src/mock/engine'
import { ApiError } from '../../src/api/errors'

test('真实模式使用同源API、Bearer、幂等重试和WebSocket认证首帧', async ({ page }) => {
  // 此测试用路由拦截模拟HTTP服务器；浏览器运行真实fetch分支，不导入Mock业务。
  const engine = new MockEngine()
  const paymentKeys: string[] = [],
    headers: string[] = [],
    requested: string[] = [],
    authFrames: unknown[] = []
  let losePaymentResponse = true
  page.on('request', (req) => requested.push(req.url()))
  await page.route(
    (url) => url.pathname.startsWith('/api/'),
    async (route) => {
      const req = route.request(),
        url = new URL(req.url()),
        path = url.pathname.slice(4)
      const match = Object.entries(routes).find(
        ([, meta]) =>
          meta.method === req.method() &&
          new RegExp(`^${meta.path.replace('{id}', '[1-9][0-9]*')}$`).test(path),
      )
      if (!match) {
        await route.fulfill({
          status: 404,
          json: { code: 'NOT_FOUND', message: '测试接口不存在', data: {} },
        })
        return
      }
      const [operation, meta] = match
      const auth = req.headers()['authorization']
      if (!meta.anonymous) headers.push(auth || '')
      const query: Record<string, unknown> = {}
      url.searchParams.forEach((value, name) => {
        query[name] = ['pageNo', 'pageSize'].includes(name) ? Number(value) : value
      })
      const key = req.headers()['idempotency-key']
      if (operation === 'payOrder') paymentKeys.push(key)
      try {
        const response = await engine.handle(operation as OperationId, {
          token: auth?.replace(/^Bearer /, ''),
          id: path.match(/\/(\d+)(?:\/|$)/)?.[1],
          query,
          body: req.postData() ? req.postDataJSON() : undefined,
          idempotencyKey: key,
        })
        if (operation === 'payOrder' && losePaymentResponse) {
          losePaymentResponse = false
          await route.abort('failed')
          return
        }
        await route.fulfill({ status: meta.status, json: response })
      } catch (error) {
        if (!(error instanceof ApiError)) throw error
        await route.fulfill({
          status: error.status,
          json: { code: error.code, message: error.message, data: error.data },
        })
      }
    },
  )
  await page.routeWebSocket('**/ws', (ws) => {
    expect(new URL(ws.url()).search).toBe('')
    ws.onMessage((message) => {
      authFrames.push(JSON.parse(String(message)))
      ws.send(JSON.stringify({ type: 'AUTHENTICATED', occurredAt: '2026-10-03T09:00:00+08:00' }))
    })
  })
  await page.goto('http://127.0.0.1:5180/customer/login')
  await page.getByLabel('用户名', { exact: true }).fill('customer')
  await page.getByLabel('密码', { exact: true }).fill('Demo12345')
  await page.getByRole('button', { name: '登录', exact: true }).click()
  await expect(page.getByRole('heading', { name: '服务目录', exact: true })).toBeVisible()
  await expect(page.locator('.demo-panel')).toHaveCount(0)
  await expect.poll(() => authFrames.length).toBeGreaterThan(0)
  expect(authFrames[0]).toMatchObject({ type: 'AUTH', accessToken: expect.any(String) })
  await page.goto('http://127.0.0.1:5180/customer/orders/10001')
  await page.getByRole('button', { name: '模拟支付 ¥160.00' }).click()
  await expect(page.getByRole('alert')).toContainText('NETWORK_ERROR')
  await page.getByRole('button', { name: '模拟支付 ¥160.00' }).click()
  await expect(page.getByText('派单成功', { exact: true })).toBeVisible()
  expect(paymentKeys).toHaveLength(2)
  expect(paymentKeys[0]).toBe(paymentKeys[1])
  expect(engine.db.histories['10001'].payments).toHaveLength(1)
  expect(headers.every((h) => h.startsWith('Bearer '))).toBe(true)
  expect(requested.some((url) => url.includes('/src/mock/transport'))).toBe(false)
  Object.values(engine.db.sessions).forEach((session) => {
    session.expiresAt = 0
  })
  await page.getByRole('button', { name: '重新查询最新状态' }).click()
  await expect(page).toHaveURL(/customer\/login/)
})
