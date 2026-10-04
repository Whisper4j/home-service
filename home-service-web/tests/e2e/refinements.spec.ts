import { expect, test, type Page } from '@playwright/test'
async function login(page: Page, role = 'customer', username = role) {
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
test('自动抢单同步保持位置、全局后部加载、重复通知去重及失效确认', async ({ page, context }) => {
  await login(page, 'customer')
  const existing = await api(page, 'customerGetOrder', { id: '10002' })
  const create = async () => {
    const order = await api(page, 'createOrder', {
      body: {
        skuId: '301',
        addressId: '401',
        bookingType: 'OFFER',
        startTime: existing.startTime,
        offerPrice: '145.00',
      },
    })
    await api(page, 'payOrder', { id: order.id })
    return order.id as string
  }
  for (let i = 0; i < 9; i++) await create()
  const worker = await context.newPage()
  await worker.setViewportSize({ width: 360, height: 640 })
  await login(worker, 'worker')
  await expect(worker.getByRole('combobox')).toHaveCount(0)
  await expect(worker.getByRole('button', { name: /刷新抢单池|点击更新/ })).toHaveCount(0)
  await expect(worker.locator('[data-offer-id]')).toHaveCount(8)
  const card = worker.locator('[data-offer-id]').nth(4)
  await card.scrollIntoViewIfNeeded()
  const anchorId = await card.getAttribute('data-offer-id')
  const before = (await card.boundingBox())!.y
  const newId = await create()
  await expect(worker.getByText(/新增1单，已更新/)).toBeAttached()
  await expect(worker.locator(`[data-offer-id="${newId}"]`)).toHaveCount(0)
  expect(
    Math.abs((await worker.locator(`[data-offer-id="${anchorId}"]`).boundingBox())!.y - before),
  ).toBeLessThan(3)
  await api(page, 'cancelCustomerOrder', { id: '10002', body: { reason: '测试取消' } })
  await expect(worker.locator('[data-offer-id="10002"]')).toHaveCount(0)
  await expect(worker.locator('[data-offer-id]')).toHaveCount(8)
  expect(
    Math.abs((await worker.locator(`[data-offer-id="${anchorId}"]`).boundingBox())!.y - before),
  ).toBeLessThan(3)
  await worker.getByRole('button', { name: /继续查看较新订单/ }).click()
  await expect(worker.locator('[data-offer-id]').last()).toHaveAttribute('data-offer-id', newId)
  await worker.evaluate(() => {
    for (let i = 0; i < 12; i++) window.dispatchEvent(new Event('data-refresh'))
  })
  await expect(worker.locator(`[data-offer-id="${newId}"]`)).toHaveCount(1)
  await worker.locator(`[data-offer-id="${newId}"]`).getByRole('button').click()
  await api(page, 'cancelCustomerOrder', { id: newId, body: { reason: '确认中取消' } })
  await expect(worker.getByRole('dialog')).toContainText('不能继续接单')
  await expect(worker.getByRole('button', { name: '按此价格确认接单' })).toBeDisabled()
  await expect(worker.locator(`[data-offer-id="${newId}"]`)).toHaveCount(0)
  await worker.getByRole('dialog').getByRole('button', { name: '关闭' }).click()
  await worker.screenshot({ path: 'test-results/automatic-pool-small.png' })
})
test('两端我的弹窗、放弃修改、浏览器返回、退出确认及紧凑标签', async ({ page }) => {
  await login(page, 'worker')
  await page.getByRole('link', { name: '我的', exact: true }).click()
  await expect(page.getByRole('heading', { name: '我的', exact: true })).toHaveCount(0)
  await expect(page.getByRole('region', { name: '服务数据' })).toContainText('累计已完成')
  await expect(page.getByRole('region', { name: '工作月历' })).toBeVisible()
  await expect(page.getByLabel('开始', { exact: true })).toHaveCount(0)
  await page.locator('.identity-name').click()
  await expect(page.getByRole('dialog')).toContainText('技能')
  await expect(page.getByRole('textbox')).toHaveCount(0)
  await page.getByRole('button', { name: '✎ 编辑信息' }).click()
  await expect(page.getByRole('textbox')).toHaveCount(1)
  await page.getByLabel('联系电话', { exact: true }).fill('13800000988')
  await page.getByRole('dialog').getByRole('button', { name: '关闭' }).click()
  await expect(page.getByText('有未保存的修改，确定放弃吗？')).toBeVisible()
  await page.getByRole('button', { name: '继续编辑' }).click()
  await expect(page.getByLabel('联系电话', { exact: true })).toHaveValue('13800000988')
  await page.getByRole('button', { name: '保存个人信息' }).click()
  await expect(page.getByRole('dialog').getByText('13800000988')).toBeVisible()
  await page.goBack()
  await expect(page.getByRole('dialog')).toHaveCount(0)
  await page.getByRole('button', { name: '退出登录', exact: true }).click()
  await page.getByRole('button', { name: '取消', exact: true }).click()
  await expect(page).toHaveURL(/worker\/me$/)
  await page.getByRole('button', { name: '服务规则 →' }).click()
  await expect(page.getByRole('dialog', { name: '服务规则', exact: true })).toBeVisible()
  await page.goBack()
  await expect(page).toHaveURL(/worker\/me$/)
  await expect(page.getByRole('dialog')).toHaveCount(0)
  await page.setViewportSize({ width: 320, height: 480 })
  await expect(page.locator('.identity-name')).toBeVisible()
  await expect(page.getByRole('region', { name: '工作月历' })).toBeVisible()
  await expect(page.locator('.error-toast')).not.toBeVisible({ timeout: 4000 })
  await page.screenshot({ path: 'test-results/worker-me-small.png' })
  for (const role of ['worker', 'customer']) {
    if (role === 'customer') await login(page)
    await page.goto(`/${role}/orders`)
    const tags = page.locator('.order-groups button')
    await expect(tags.first()).toBeVisible()
    const tops = await tags.evaluateAll((elements) =>
      elements.map((el) => el.getBoundingClientRect().top),
    )
    expect(new Set(tops).size).toBe(1)
    await tags.last().click()
    expect(await page.evaluate(() => document.documentElement.scrollWidth <= innerWidth)).toBe(true)
  }
  await page.goto('/customer/me')
  await page.getByRole('button', { name: '退出登录', exact: true }).click()
  await page.getByRole('button', { name: '确认退出' }).click()
  await page.goto('/customer/me')
  await page.getByRole('button', { name: '地址簿 →' }).click()
  await expect(page).toHaveURL(/login.*redirect/)
  await page.getByRole('button', { name: '登录', exact: true }).click()
  await expect(page.getByRole('dialog', { name: '地址簿', exact: true })).toBeVisible()
})
test('预约地址共用面板保留手动联系人，结果刷新回业务列表并拒绝外部来源', async ({ page }) => {
  await login(page)
  await page.goto('/customer/services/DEEP_60')
  await page.getByRole('button', { name: /^标准预约/ }).click()
  await page.getByLabel('本次联系人', { exact: true }).fill('替家人预约')
  await page.getByLabel('本次联系电话').fill('13800000987')
  await page.getByLabel('额外要求（可选）').fill('原预约草稿')
  await page.getByRole('button', { name: '新增地址并返回预约' }).click()
  await expect(page.getByLabel('经度')).toHaveCount(0)
  await page.getByLabel('详细地址').fill('演示长地址'.repeat(15))
  await page.setViewportSize({ width: 320, height: 300 })
  await expect
    .poll(async () => {
      const footer = await page.getByRole('dialog').locator('footer').boundingBox()
      return footer!.y + footer!.height
    })
    .toBeLessThanOrEqual(300)
  await page.getByRole('button', { name: '保存地址', exact: true }).click()
  await expect(page.getByRole('dialog')).toHaveCount(0)
  await expect(page.getByLabel('本次联系人', { exact: true })).toHaveValue('替家人预约')
  await expect(page.getByLabel('本次联系电话')).toHaveValue('13800000987')
  await expect(page.getByLabel('额外要求（可选）')).toHaveValue('原预约草稿')
  await page.setViewportSize({ width: 360, height: 640 })
  await page.getByRole('button', { name: '提交预约并去支付' }).click()
  await expect(page.locator('.error-toast')).toContainText('预约已提交')
  await page.getByRole('button', { name: '确认模拟支付' }).click()
  await expect(page).toHaveURL(/result\/\d+/)
  const resultUrl = page.url()
  await page.reload()
  await page.getByRole('button', { name: '返回上一页' }).click()
  await expect(page).toHaveURL(/cleaning\/deep$/)
  await page.goto(resultUrl + '?returnTo=https://example.com')
  await page.getByRole('button', { name: '返回上一页' }).click()
  await expect(page).toHaveURL(/cleaning\/deep$/)
  await page.goto(resultUrl)
  await page.getByRole('link', { name: '返回首页', exact: true }).click()
  await expect(page).toHaveURL(/customer\/home$/)
})
test('请假记录分页、局部失败、保存草稿及登录过期不跨账号恢复', async ({ page }) => {
  await login(page, 'worker')
  await page.goto('/worker/me')
  const calendar = await api(page, 'getWorkerCalendar', {
    query: { month: new Date(Date.now() + 8 * 3600000).toISOString().slice(0, 7) },
  })
  const day = calendar.days[2].date
  for (let i = 0; i < 6; i++)
    await api(page, 'createLeave', {
      body: {
        startTime: `${day}T${String(8 + i).padStart(2, '0')}:00:00+08:00`,
        endTime: `${day}T${String(8 + i).padStart(2, '0')}:30:00+08:00`,
        reason: `记录${i}`,
      },
    })
  await page.getByRole('button', { name: '请假记录 →' }).click()
  await expect(page.getByRole('dialog').locator('article')).toHaveCount(5)
  await page.getByRole('button', { name: '下一页', exact: true }).click()
  await expect(page.getByRole('dialog').locator('article')).toHaveCount(1)
  await page.getByRole('dialog').getByRole('button', { name: '关闭' }).click()
  await page.locator('.identity-name').click()
  await page.getByRole('button', { name: '✎ 编辑信息' }).click()
  await page.getByLabel('联系电话', { exact: true }).fill('13800000986')
  await page.evaluate(() => {
    const key = 'home-service.mock.v1',
      db = JSON.parse(localStorage.getItem(key)!)
    for (const session of Object.values(db.sessions) as { expiresAt: number }[])
      session.expiresAt = 0
    localStorage.setItem(key, JSON.stringify(db))
  })
  await page.getByRole('button', { name: '保存个人信息' }).click()
  await expect(page).toHaveURL(/worker\/login/)
  await page.getByLabel('用户名', { exact: true }).fill('worker2')
  await page.getByRole('button', { name: '登录', exact: true }).click()
  await expect(page.getByRole('textbox')).toHaveCount(0)
  await page.getByRole('button', { name: '✎ 编辑信息' }).click()
  await expect(page.getByLabel('联系电话', { exact: true })).not.toHaveValue('13800000986')
})

test('漏通知由30秒HTTP补齐，后台同步不覆盖确认价，网络恢复保留卡片', async ({ page }) => {
  await page.clock.install()
  await login(page, 'worker')
  await page.getByRole('button', { name: '查看详情 / 接单' }).first().click()
  await expect(page.getByText('本次确认报价 ¥130.00')).toBeVisible()
  // Deliberately change the authoritative fixture without publishing any notification.
  await page.evaluate(() => {
    const key = 'home-service.mock.v1',
      db = JSON.parse(localStorage.getItem(key)!)
    const order = db.orders.find((row: { order: { id: string } }) => row.order.id === '10002').order
    order.currentPrice = '135.00'
    order.priceVersion = 2
    localStorage.setItem(key, JSON.stringify(db))
  })
  await page.clock.runFor(31_000)
  await expect(page.getByRole('dialog')).toContainText('最新报价 ¥135.00')
  await expect(page.getByText('本次确认报价 ¥130.00')).toBeVisible()
  await page.getByRole('dialog').getByRole('button', { name: '关闭' }).click()
  const count = await page.locator('[data-offer-id]').count()
  await page.evaluate(async () => {
    const path = '/src/mock/transport.ts'
    ;(await import(path)).simulateNetworkFailure()
    window.dispatchEvent(new Event('data-refresh'))
  })
  await expect(page.getByRole('button', { name: '重试连接', exact: true })).toBeVisible()
  await expect(page.locator('[data-offer-id]')).toHaveCount(count)
  await page.evaluate(() => window.dispatchEvent(new Event('online')))
  await expect(page.getByRole('button', { name: '重试连接', exact: true })).toHaveCount(0)
  await expect(page.locator('[data-offer-id]')).toHaveCount(count)
})

test('明确支付失败与不确定状态区分，日常和维修支付返回所属列表', async ({ page }) => {
  await login(page)
  await page.goto('/customer/pay/10001')
  await expect(page.getByRole('button', { name: '确认模拟支付' })).toBeVisible()
  await api(page, 'cancelCustomerOrder', { id: '10001', body: { reason: '支付前取消' } })
  await page.getByRole('button', { name: '确认模拟支付' }).click()
  await expect(page.getByText(/支付未成功：/)).toBeVisible()
  await expect(page.getByText(/正在确认支付结果/)).toHaveCount(0)
  expect((await api(page, 'customerGetOrderHistory', { id: '10001' })).payments).toHaveLength(0)
  for (const [entry, target] of [
    ['DAILY_2H', '/customer/cleaning/daily'],
    ['TOILET_UNBLOCK', '/customer/repair/plumbing'],
  ]) {
    await page.goto(`/customer/services/${entry}`)
    await page.getByRole('button', { name: /^标准预约/ }).click()
    await page.getByRole('button', { name: '提交预约并去支付' }).click()
    await expect(page).toHaveURL(/customer\/pay\/\d+/)
    await page.getByRole('button', { name: '确认模拟支付' }).click()
    await expect(page.getByRole('heading', { name: '模拟支付成功', exact: true })).toBeVisible()
    await page.getByRole('button', { name: '返回上一页' }).click()
    await expect(page).toHaveURL(new RegExp(target + '$'))
  }
})
