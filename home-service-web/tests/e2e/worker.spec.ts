import { expect, test, type Page } from '@playwright/test'
async function login(page: Page, role = 'worker', username = role) {
  await page.goto(`/${role}/login`)
  await page.getByLabel('用户名', { exact: true }).fill(username)
  await page.getByRole('button', { name: '登录', exact: true }).click()
  await expect(page).not.toHaveURL(/login/)
}
async function api(page: Page, operation: string, options: object = {}) {
  return page.evaluate(
    async ({ operation, options }) => {
      const path = '/src/api/client.ts'
      return (await import(path)).request(operation, options)
    },
    { operation, options },
  )
}
test('业务层级返回、切换不叠历史、草稿与支付结果返回', async ({ page }) => {
  await page.goto('/customer/cleaning/daily')
  for (let i = 0; i < 3; i++) {
    await page.getByRole('link', { name: '深度清洁', exact: true }).click()
    await page.getByRole('link', { name: '日常清洁', exact: true }).click()
  }
  await page.getByRole('button', { name: /^2 小时套餐/ }).click()
  await page.getByRole('button', { name: '返回上一页' }).click()
  await expect(page).toHaveURL(/cleaning\/daily$/)
  await page.getByRole('button', { name: '返回上一页' }).click()
  await expect(page).toHaveURL(/customer\/home$/)
  await page.getByRole('button', { name: /^维修服务/ }).click()
  await page.getByRole('link', { name: /管道与卫浴/ }).click()
  await page.getByRole('button', { name: /^马桶疏通/ }).click()
  await page.getByRole('button', { name: '返回上一页' }).click()
  await expect(page).toHaveURL(/repair\/plumbing$/)
  await page.getByRole('button', { name: '返回上一页' }).click()
  await expect(page).toHaveURL(/repair$/)
  await login(page, 'customer')
  await page.goto('/customer/pay/10001')
  await page.getByRole('button', { name: '返回上一页' }).click()
  await expect(page).toHaveURL(/orders\/10001$/)
  await expect(page.getByRole('heading', { name: '待支付', exact: true })).toBeVisible()
  await page.getByRole('link', { name: /去支付 ¥/ }).click()
  await page.getByRole('button', { name: '确认模拟支付' }).click()
  await expect(page).toHaveURL(/result\/10001$/)
  await page.getByRole('button', { name: '返回上一页' }).click()
  await expect(page).toHaveURL(/customer\/home$/)
  await page.goBack()
  await expect(page).not.toHaveURL(/booking|pay\//)
})
test('人员多任务展开、收起位置、卡片排序、筛选恢复及小屏', async ({ page }) => {
  const errors: string[] = []
  page.on('pageerror', (e) => errors.push(e.message))
  await login(page)
  // 固定多任务演示夹具；仍经真实页面查询和渲染契约数据。
  await page.evaluate(() => {
    const key = 'home-service.mock.v1',
      db = JSON.parse(localStorage.getItem(key)!)
    const orders = db.orders.filter(
      (s: { order: { workerId: string } }) => s.order.workerId === '201',
    )
    for (let i = 0; i < 9; i++)
      orders[i].order.status = i === 0 ? 'IN_SERVICE' : i === 1 ? 'ARRIVED' : 'PENDING_SERVICE'
    localStorage.setItem(key, JSON.stringify(db))
  })
  await page.getByRole('button', { name: '刷新抢单池', exact: true }).click()
  await expect(page.getByRole('button', { name: '查看全部9单' })).toBeVisible()
  await page.setViewportSize({ width: 360, height: 640 })
  await page.locator('#worker-scroll').evaluate((element) => {
    element.scrollTop = 180
  })
  const poolPosition = await page.locator('#worker-scroll').evaluate((element) => element.scrollTop)
  await page.getByRole('button', { name: '查看全部9单' }).click()
  await page
    .getByRole('dialog')
    .locator('.dialog-body')
    .evaluate((element) => {
      element.scrollTop = 400
    })
  await page.getByRole('dialog').getByRole('button', { name: '关闭', exact: true }).click()
  await expect
    .poll(() => page.locator('#worker-scroll').evaluate((element) => element.scrollTop))
    .toBe(poolPosition)
  await page.screenshot({ path: 'test-results/worker-home.png' })
  const widths: number[] = []
  for (const size of [
    { width: 1400, height: 1000 },
    { width: 1100, height: 800 },
    { width: 320, height: 480 },
  ]) {
    await page.setViewportSize(size)
    await expect
      .poll(async () => (await page.locator('.worker-shell').boundingBox())!.height)
      .toBe(size.height)
    const box = await page.locator('.worker-shell').boundingBox()
    widths.push(box!.width)
    expect(box!.height).toBeCloseTo(size.height, 0)
    expect(await page.evaluate(() => document.documentElement.scrollWidth <= innerWidth)).toBe(true)
  }
  expect(widths[0]).toBeCloseTo(562.5, 0)
  expect(widths[1]).toBeCloseTo(450, 0)
  await page.locator('#worker-scroll').evaluate((e) => {
    e.scrollTop = 60
  })
  await page.getByRole('button', { name: '查看全部9单' }).click()
  await expect(page.getByRole('dialog').locator('.order-card')).toHaveCount(9)
  await expect(page.getByRole('dialog').locator('.order-card').first()).toContainText('服务中')
  await page.getByRole('dialog').getByRole('button', { name: '关闭', exact: true }).click()
  await page
    .getByRole('navigation', { name: '人员导航' })
    .getByRole('link', { name: '订单', exact: true })
    .click()
  await page.getByRole('button', { name: '待服务', exact: true }).click()
  await page.getByRole('button', { name: '下一页' }).click()
  await page.getByRole('link', { name: '查看任务 / 履约' }).first().click()
  await page.getByRole('button', { name: '返回上一页' }).click()
  await expect(page.getByRole('button', { name: '待服务', exact: true })).toHaveAttribute(
    'aria-pressed',
    'true',
  )
  await expect(page.getByText('2 / 2 · 7单')).toBeVisible()
  await page.screenshot({ path: 'test-results/worker-small-orders.png' })
  expect(errors).toEqual([])
})
test('抢单报价更新冻结确认、重新确认和并发唯一接单', async ({ page, context }) => {
  await login(page)
  await page.getByRole('button', { name: '查看详情 / 接单' }).first().click()
  const customer = await context.newPage()
  await login(customer, 'customer')
  await customer.goto('/customer/orders/10002')
  await customer.getByRole('button', { name: '调整报价' }).click()
  await customer.getByRole('button', { name: '＋5元' }).click()
  await customer.getByLabel('我确认模拟补付上述差额').check()
  await customer.getByRole('button', { name: '确认报价调整' }).click()
  await expect(page.getByRole('dialog').getByText(/原报价 ¥130.00，最新报价 ¥135.00/)).toBeVisible()
  await expect(page.getByRole('button', { name: '按此价格确认接单' })).toBeDisabled()
  await expect(page.getByText('本次确认报价 ¥130.00')).toBeVisible()
  await page.getByRole('button', { name: '我已查看，重新确认此报价' }).click()
  await expect(page.getByText('本次确认报价 ¥135.00')).toBeVisible()
  const other = await context.newPage()
  await login(other, 'worker', 'worker2')
  await other.getByRole('button', { name: '查看详情 / 接单' }).first().click()
  await Promise.all([
    page.getByRole('button', { name: '按此价格确认接单' }).click(),
    other.getByRole('button', { name: '按此价格确认接单' }).click(),
  ])
  await expect
    .poll(async () => {
      const summaries = await Promise.all([
        page.locator('.task-summary').innerText(),
        other.locator('.task-summary').innerText(),
      ])
      return summaries.filter((s) => s.includes('查看全部1单')).length
    })
    .toBe(1)
  const order = await api(customer, 'customerGetOrder', { id: '10002' })
  expect(order.dealPrice).toBe('135.00')
  expect(['201', '202']).toContain(order.workerId)
})
test('月历服务与缓冲、请假冲突、电话边界及完整统计', async ({ page }) => {
  await login(page)
  await page.getByRole('button', { name: '查看详情 / 接单' }).first().click()
  await page.getByRole('button', { name: '按此价格确认接单' }).click()
  await expect(page.getByRole('button', { name: '查看全部1单' })).toBeVisible()
  const order = await api(page, 'workerGetOrder', { id: '10002' })
  await page.goto('/worker/schedule')
  await page.getByRole('button', { name: new RegExp(`^${order.startTime.slice(0, 10)} `) }).click()
  const calendar = page.getByRole('region', { name: '工作月历' })
  await expect(calendar.getByText(/服务后预留间隔（不可接单）/)).toBeVisible()
  await expect(calendar.getByText(/工作时间内空闲 \/ 可接时段/).first()).toBeVisible()
  await page.locator('#worker-scroll').evaluate((element) => {
    element.scrollTop = 0
  })
  await page.screenshot({ path: 'test-results/worker-calendar.png' })
  await page.getByLabel('开始（北京时间）').fill(order.endTime.slice(0, 16))
  await page.getByLabel('结束（北京时间）').fill(order.bufferEndTime.slice(0, 16))
  await page.getByLabel('原因', { exact: true }).fill('缓冲冲突验证')
  await page.getByRole('button', { name: '提交请假', exact: true }).click()
  await expect(page.getByRole('link', { name: '查看冲突订单 10002' })).toBeVisible()
  await page.getByRole('link', { name: '查看冲突订单 10002' }).click()
  await page.getByRole('button', { name: '返回上一页' }).click()
  await expect(page).toHaveURL(/worker\/schedule$/)
  await page.goto('/worker/profile')
  await page.getByLabel('联系电话', { exact: true }).fill('123')
  await page.getByLabel('联系电话', { exact: true }).blur()
  await expect(page.locator('[data-field-error]')).toContainText('格式不正确')
  await expect(page.locator('.error-toast')).toBeVisible()
  await expect(page.locator('.error-toast')).not.toBeVisible({ timeout: 4000 })
  await expect(page.locator('[data-field-error]')).toBeVisible()
  await page.getByLabel('联系电话', { exact: true }).fill('13800000999')
  await page.getByRole('button', { name: '保存联系电话' }).click()
  await expect(page.getByRole('status')).toContainText('联系电话已更新')
  await page.reload()
  await expect(page.getByLabel('联系电话', { exact: true })).toHaveValue('13800000999')
  await page.goto('/worker/statistics')
  const stats = await api(page, 'getWorkerStatistics')
  await expect(
    page.getByRole('heading', { name: `累计已完成 ${stats.totalCompletedCount} 单` }),
  ).toBeVisible()
  await expect(page.getByText(/预约服务时长/).last()).toBeVisible()
})
test('新增抢单只提示不插入；非法调价不修正、边界禁用、弹窗上方统一错误', async ({
  page,
  context,
}) => {
  await login(page)
  const before = await page.locator('.order-card').count()
  const customer = await context.newPage()
  await login(customer, 'customer')
  await customer.goto('/customer/orders/10002')
  const existing = await api(customer, 'customerGetOrder', { id: '10002' })
  const created = await api(customer, 'createOrder', {
    body: {
      skuId: '301',
      addressId: '401',
      bookingType: 'OFFER',
      startTime: existing.startTime,
      offerPrice: '145.00',
    },
  })
  await api(customer, 'payOrder', { id: created.id })
  await expect(page.getByRole('button', { name: /1条新订单/ })).toBeVisible()
  await expect(page.locator('.order-card')).toHaveCount(before)
  await page.getByRole('button', { name: /1条新订单/ }).click()
  await expect(page.locator('.order-card')).toHaveCount(before + 1)
  await customer.getByRole('button', { name: '调整报价' }).click()
  await expect(customer.getByRole('button', { name: '确认报价调整' })).toBeDisabled()
  await expect(customer.getByRole('button', { name: '－5元' })).toBeDisabled()
  await customer.getByLabel('新报价').fill('131')
  await customer.getByLabel('新报价').blur()
  await expect(customer.locator('.error-toast')).toBeVisible()
  await expect(customer.locator('.error-toast')).toContainText('5元')
  await expect(customer.getByLabel('新报价')).toHaveValue('131')
  await expect(customer.getByRole('button', { name: '＋5元' })).toBeDisabled()
  await expect(customer.locator('.error-toast')).not.toBeVisible({ timeout: 4000 })
  await expect(customer.getByRole('dialog').locator('.field-error')).toBeVisible()
  await customer.setViewportSize({ width: 320, height: 300 })
  await customer.getByLabel('新报价').fill('155.00')
  await expect(customer.getByRole('button', { name: '＋5元' })).toBeDisabled()
  await customer.getByLabel('我确认模拟补付上述差额').check()
  const footer = await customer.getByRole('dialog').locator('footer').boundingBox()
  expect(footer!.y + footer!.height).toBeLessThanOrEqual(300)
  await customer.getByRole('button', { name: '确认报价调整' }).click()
  await expect(customer.getByRole('dialog')).toHaveCount(0)
})

test('客户订单返回恢复原筛选和滚动，直接打开订单有返回兜底', async ({ page }) => {
  await login(page, 'customer')
  await page.getByRole('link', { name: '订单', exact: true }).click()
  await page.setViewportSize({ width: 360, height: 640 })
  await page.getByRole('button', { name: '已结束', exact: true }).click()
  await expect(page.getByRole('button', { name: '已结束', exact: true })).toBeEnabled()
  await expect
    .poll(async () => (await page.locator('.customer-shell').boundingBox())!.height)
    .toBe(640)
  const target = page.locator('.order-card').nth(3).getByRole('link')
  await target.scrollIntoViewIfNeeded()
  const position = await page.locator('#customer-scroll').evaluate((element) => element.scrollTop)
  expect(position).toBeGreaterThan(0)
  await target.click()
  await page.getByRole('button', { name: '返回上一页' }).click()
  await expect(page.getByRole('button', { name: '已结束', exact: true })).toHaveAttribute(
    'aria-pressed',
    'true',
  )
  await expect
    .poll(() => page.locator('#customer-scroll').evaluate((element) => element.scrollTop))
    .toBe(position)
  await page.goto('/customer/orders/10002')
  await page.getByRole('button', { name: '返回上一页' }).click()
  await expect(page).toHaveURL(/customer\/orders$/)
})
