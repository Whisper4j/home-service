import { expect, test, type WebSocketRoute } from '@playwright/test'
import { routes } from '../../src/api/generated/routes'
import type { OperationId, Schema } from '../../src/api/types'
import { MockEngine } from '../../src/mock/engine'
import { ApiError } from '../../src/api/errors'

test('真实模式使用同源API、Bearer、幂等重试和WebSocket认证首帧', async ({ page }) => {
  // 此测试用路由拦截模拟HTTP服务器；浏览器运行真实fetch分支，不导入Mock业务。
  const engine = new MockEngine()
  const sockets: WebSocketRoute[] = []
  let rejectPhone = true
  const paymentKeys: string[] = [],
    headers: string[] = [],
    requested: string[] = [],
    authFrames: unknown[] = []
  let losePaymentResponse = true
  let loseCreationResponse = true
  const creationKeys: string[] = [],
    uploadedFiles = new Map<string, File>()
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
        query[name] = ['pageNo', 'pageSize'].includes(name)
          ? Number(value)
          : name === 'statuses'
            ? value.split(',')
            : value
      })
      const key = req.headers()['idempotency-key']
      if (operation === 'payOrder') paymentKeys.push(key)
      if (operation === 'createOrder') creationKeys.push(key)
      try {
        if (operation === 'updateWorkerContact' && rejectPhone) {
          rejectPhone = false
          throw new ApiError('VALIDATION_ERROR', '请核对联系电话', 400, {
            fieldErrors: [{ field: 'phone', message: '演示接口要求重新核对联系电话' }],
          })
        }
        let file: File | undefined
        if (meta.upload) {
          expect(req.headers()['content-type']).toMatch(/^multipart\/form-data; boundary=/)
          const multipart = await new Response(new Uint8Array(req.postDataBuffer()!), {
            headers: { 'Content-Type': req.headers()['content-type'] },
          }).formData()
          file = multipart.get('file') as File
          expect(file.type).toBe('image/png')
          expect(key).toBeTruthy()
        }
        const response = await engine.handle(operation as OperationId, {
          token: auth?.replace(/^Bearer /, ''),
          id: path.match(/\/(\d+)(?:\/|$)/)?.[1],
          query,
          body: !meta.upload && req.postData() ? req.postDataJSON() : undefined,
          file,
          idempotencyKey: key,
        })
        if (meta.upload) uploadedFiles.set((response.data as Schema['SceneImageVO']).id, file!)
        if (meta.binary) {
          const image = response.data as Schema['SceneImageVO']
          await route.fulfill({
            status: 200,
            contentType: image.mimeType,
            headers: { 'Cache-Control': 'private, no-store', 'X-Content-Type-Options': 'nosniff' },
            body: Buffer.from(await uploadedFiles.get(image.id)!.arrayBuffer()),
          })
          return
        }
        if (operation === 'createOrder' && loseCreationResponse) {
          loseCreationResponse = false
          await route.abort('failed')
          return
        }
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
    sockets.push(ws)
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
  await expect(page.getByRole('heading', { name: '首页', exact: true })).toBeVisible()
  await expect(page.locator('.demo-panel')).toHaveCount(0)
  await expect.poll(() => authFrames.length).toBeGreaterThan(0)
  expect(authFrames[0]).toMatchObject({ type: 'AUTH', accessToken: expect.any(String) })
  await page.goto('http://127.0.0.1:5180/customer/services/DAILY_2H')
  await page.getByRole('button', { name: /^标准预约/ }).click()
  await page.getByLabel('本次联系人', { exact: true }).fill('接口联调联系人')
  const image = await page.evaluate(() => {
    const canvas = document.createElement('canvas')
    canvas.width = 8
    canvas.height = 8
    return canvas.toDataURL('image/png').split(',')[1]
  })
  await page.locator('input[type=file]').setInputFiles({
    name: 'scene.png',
    mimeType: 'image/png',
    buffer: Buffer.from(image, 'base64'),
  })
  await expect(page.getByRole('button', { name: '预览现场图片 1', exact: true })).toBeVisible()
  await page.getByRole('button', { name: '提交预约并去支付' }).click()
  await expect(page.locator('.error-toast')).toContainText('无法连接服务')
  await page.reload()
  await expect(page.getByLabel('本次联系人', { exact: true })).toHaveValue('接口联调联系人')
  await page.getByRole('button', { name: '提交预约并去支付' }).click()
  await expect(page).toHaveURL(/customer\/pay\/\d+/)
  const orderId = page.url().split('/').at(-1)!
  expect(creationKeys).toHaveLength(2)
  expect(creationKeys[0]).toBe(creationKeys[1])
  expect(engine.db.orders).toHaveLength(28)
  await page.getByRole('button', { name: '确认模拟支付' }).click()
  await expect(page.locator('.error-toast')).toContainText('无法连接服务')
  await page.getByRole('button', { name: '确认模拟支付' }).click()
  await page.getByRole('link', { name: '查看订单进度' }).click()
  await expect(page.getByText('派单成功，人员已安排。', { exact: true })).toBeVisible()
  expect(paymentKeys).toHaveLength(2)
  expect(paymentKeys[0]).toBe(paymentKeys[1])
  expect(engine.db.histories[orderId].payments).toHaveLength(1)
  expect(engine.db.orders[0].order.sceneImages).toHaveLength(1)
  await expect(page.getByRole('button', { name: '预览现场图片 1', exact: true })).toBeVisible()
  expect(headers.every((h) => h.startsWith('Bearer '))).toBe(true)
  expect(requested.some((url) => url.includes('/src/mock/transport'))).toBe(false)
  Object.values(engine.db.sessions).forEach((session) => {
    session.expiresAt = 0
  })
  await page.getByRole('button', { name: '重新查询最新状态' }).click()
  await expect(page).toHaveURL(/customer\/login/)
  await page.goto('http://127.0.0.1:5180/worker/login')
  await page.getByLabel('用户名', { exact: true }).fill('worker')
  await page.getByLabel('密码', { exact: true }).fill('Demo12345')
  await page.getByRole('button', { name: '登录', exact: true }).click()
  await expect(page).toHaveURL(/worker\/home$/)
  await expect(page.getByRole('button', { name: '查看全部1单' })).toBeVisible()
  const before = requested.filter((url) => url.includes('/api/worker/offers')).length
  const connectionCount = sockets.length
  await sockets.at(-1)!.close({ code: 1012, reason: '测试重连' })
  await expect.poll(() => sockets.length).toBeGreaterThan(connectionCount)
  await expect
    .poll(() => requested.filter((url) => url.includes('/api/worker/offers')).length)
    .toBeGreaterThan(before)
  await page.getByRole('link', { name: '我的', exact: true }).click()
  await page.getByRole('link', { name: '个人资料', exact: true }).click()
  await page.getByLabel('联系电话', { exact: true }).fill('13800000991')
  await page.getByRole('button', { name: '保存联系电话' }).click()
  await expect(page.locator('[data-field-error]')).toContainText('演示接口要求重新核对')
  await expect(page.locator('.error-toast')).not.toBeVisible({ timeout: 4000 })
  await expect(page.locator('[data-field-error]')).toBeVisible()
  await page.getByLabel('联系电话', { exact: true }).focus()
  await page.getByLabel('联系电话', { exact: true }).blur()
  await expect(page.locator('[data-field-error]')).toBeVisible()
  await page.getByLabel('联系电话', { exact: true }).fill('13800000992')
  await expect(page.locator('[data-field-error]')).toHaveCount(0)
  await page.getByRole('button', { name: '保存联系电话' }).click()
  await expect(page.getByRole('status')).toContainText('联系电话已更新')
  await page.getByRole('button', { name: '返回上一页' }).click()
  await page.getByRole('link', { name: '服务数据', exact: true }).click()
  await expect(page.getByRole('heading', { name: /累计已完成/ })).toBeVisible()
  await page.getByRole('button', { name: '返回上一页' }).click()
  await page.getByRole('link', { name: '工作时间与请假' }).click()
  await expect(page.getByRole('region', { name: '工作月历' })).toBeVisible()
  expect(requested.some((url) => url.includes('/src/mock/transport'))).toBe(false)
})
