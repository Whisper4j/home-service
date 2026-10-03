import { expect, test, type Page } from '@playwright/test'
async function imageFixture(page: Page) {
  const data = await page.evaluate(() => {
    const canvas = document.createElement('canvas')
    canvas.width = 80
    canvas.height = 60
    const context = canvas.getContext('2d')!
    context.fillStyle = '#ddd'
    context.fillRect(0, 0, 80, 60)
    context.strokeRect(12, 12, 56, 36)
    return canvas.toDataURL('image/png').split(',')[1]
  })
  return Buffer.from(data, 'base64')
}
async function login(page: Page, role = 'customer', username = role) {
  await page.goto(`/${role}/login`)
  await page.getByLabel('用户名', { exact: true }).fill(username)
  await page.getByLabel('密码', { exact: true }).fill('Demo12345')
  await page.getByRole('button', { name: '登录', exact: true }).click()
  await expect(page).not.toHaveURL(/login/)
}
async function book(page: Page, mode = 'STANDARD') {
  await page.goto('/customer/services/DAILY_2H')
  await page.getByRole('button', { name: mode === 'OFFER' ? /^优惠预约/ : /^标准预约/ }).click()
  await expect(page.getByLabel('本次联系人', { exact: true })).toBeVisible()
}
async function noOverflow(page: Page) {
  await expect
    .poll(async () => {
      const box = await page.locator('.customer-shell').boundingBox()
      return Math.abs(box!.height - page.viewportSize()!.height)
    })
    .toBeLessThan(2)
  expect(await page.evaluate(() => document.documentElement.scrollWidth <= window.innerWidth)).toBe(
    true,
  )
}

test('公开固定入口、清洁切换、维修详情和竖屏随视口缩放', async ({ page }) => {
  const errors: string[] = []
  page.on('pageerror', (e) => errors.push(e.message))
  await page.goto('/customer/home')
  await expect(page.getByRole('navigation', { name: '客户导航' }).getByRole('link')).toHaveCount(3)
  await expect(page.getByText('服务目录', { exact: true })).toHaveCount(0)
  const widths: number[] = []
  for (const size of [
    { width: 1440, height: 1000 },
    { width: 1200, height: 800 },
    { width: 360, height: 640 },
    { width: 320, height: 480 },
  ]) {
    await page.setViewportSize(size)
    await noOverflow(page)
    widths.push((await page.locator('.customer-shell').boundingBox())!.width)
    const tab = await page.getByRole('navigation', { name: '客户导航' }).boundingBox()
    expect(tab!.y + tab!.height).toBeLessThanOrEqual(size.height + 1)
  }
  expect(widths[0]).toBeCloseTo((1000 * 9) / 16, 0)
  expect(widths[1]).toBeCloseTo((800 * 9) / 16, 0)
  await page.getByRole('button', { name: /^清洁服务/ }).click()
  await expect(page.getByRole('dialog')).toBeVisible()
  await page.keyboard.press('Escape')
  await expect(page.getByRole('dialog')).not.toBeVisible()
  await page.getByRole('button', { name: /^清洁服务/ }).click()
  await page.getByRole('button', { name: /^日常清洁/ }).click()
  await expect(page.locator('.package-card')).toHaveCount(3)
  await page.getByRole('link', { name: '深度清洁', exact: true }).click()
  await expect(page.locator('.package-card')).toHaveCount(2)
  await page.getByRole('button', { name: /^60㎡以内/ }).click()
  await expect(page.getByText('深度清洁不包含装修后的开荒保洁。')).toBeVisible()
  await expect(page.getByRole('navigation', { name: '客户导航' })).toHaveCount(0)
  await noOverflow(page)
  const footer = await page.locator('#customer-actions').boundingBox()
  expect(footer!.y + footer!.height).toBeLessThanOrEqual(481)
  await page.screenshot({ path: 'test-results/customer-small-detail.png' })
  await page.goto('/customer/home')
  await page.getByRole('button', { name: /^维修服务/ }).click()
  await page.getByRole('link', { name: /管道与卫浴/ }).click()
  await page.getByRole('button', { name: /^更换同规格水龙头/ }).click()
  await expect(page.getByRole('button', { name: /优惠预约/ })).toHaveCount(0)
  await expect(page.getByText(/价格不含配件费用/)).toBeVisible()
  expect(errors).toEqual([])
})

test('未登录回跳、地址新增返回、联系人覆盖、个人资料和照片保存查看', async ({ page, context }) => {
  const png = await imageFixture(page)
  const errors: string[] = []
  page.on('pageerror', (e) => errors.push(e.message))
  await page.goto('/customer/services/DAILY_2H')
  await page.getByRole('button', { name: /^优惠预约/ }).click()
  await expect(page).toHaveURL(/login.*redirect/)
  await page.getByRole('button', { name: '登录', exact: true }).click()
  await expect(page).toHaveURL(/booking\/301/)
  await expect(page.getByLabel('当前报价')).toHaveValue('145.00')
  await page.getByRole('button', { name: '返回上一页', exact: true }).click()
  await expect(page).toHaveURL(/customer\/services\/DAILY_2H/)
  await page.getByRole('button', { name: /^优惠预约/ }).click()
  await expect(page.getByLabel('当前报价')).toHaveValue('145.00')
  await page.getByLabel('额外要求（可选）').fill('厨房重点清洁，请提前联系')
  await page.getByRole('button', { name: '新增地址并返回预约' }).click()
  await page.getByLabel('联系人', { exact: true }).fill('家人地址联系人')
  await page.getByLabel('联系电话', { exact: true }).fill('13800000008')
  await page.getByLabel('详细地址').fill('虚构路8号，仅用于演示')
  await page.getByRole('button', { name: '保存地址', exact: true }).click()
  await expect(page.getByLabel('本次联系人', { exact: true })).toHaveValue('家人地址联系人')
  await expect(page.getByLabel('额外要求（可选）')).toHaveValue('厨房重点清洁，请提前联系')
  await page.getByLabel('本次联系人', { exact: true }).fill('本次家人')
  await page.getByLabel('本次联系电话').fill('13800000009')
  const input = page.locator('input[type=file]')
  await input.setInputFiles({ name: 'scene.png', mimeType: 'image/png', buffer: png })
  await expect(page.getByRole('button', { name: '预览现场图片 1', exact: true })).toBeVisible()
  await page.getByRole('button', { name: '预览现场图片 1', exact: true }).click()
  await expect(page.getByRole('dialog')).toBeVisible()
  await page.getByRole('button', { name: '关闭预览' }).click()
  await page.getByRole('button', { name: '删除现场图片 1', exact: true }).click()
  await expect(page.getByRole('button', { name: '预览现场图片 1', exact: true })).toHaveCount(0)
  await input.setInputFiles({ name: 'scene.png', mimeType: 'image/png', buffer: png })
  await expect(page.getByRole('button', { name: '预览现场图片 1', exact: true })).toBeVisible()
  await page.reload()
  await expect(page.getByLabel('本次联系人', { exact: true })).toHaveValue('本次家人')
  await expect(page.getByRole('button', { name: '预览现场图片 1', exact: true })).toBeVisible()
  await page.locator('input[type=file]').setInputFiles([
    { name: 'second.png', mimeType: 'image/png', buffer: png },
    { name: 'third.png', mimeType: 'image/png', buffer: png },
  ])
  await expect(page.getByRole('button', { name: '预览现场图片 3', exact: true })).toBeVisible()
  await page.setViewportSize({ width: 360, height: 500 })
  await noOverflow(page)
  await page.getByLabel('额外要求（可选）').focus()
  await page.setViewportSize({ width: 360, height: 300 })
  await noOverflow(page)
  const submitBar = await page.locator('#customer-actions').boundingBox()
  expect(submitBar!.y + submitBar!.height).toBeLessThanOrEqual(301)
  await page.setViewportSize({ width: 360, height: 500 })
  await page.getByRole('button', { name: '提交预约并去支付' }).click()
  await expect(page).toHaveURL(/customer\/pay\/\d+/)
  const id = page.url().split('/').at(-1)!
  await expect(page.getByText('本次家人 · 13800000009')).toBeVisible()
  await page.getByRole('button', { name: '确认模拟支付' }).click()
  await page.getByRole('link', { name: '查看订单进度' }).click()
  await expect(page.getByRole('heading', { name: '待抢单', exact: true })).toBeVisible()
  await expect(page.getByRole('button', { name: '预览现场图片 1', exact: true })).toBeVisible()
  await page.screenshot({ path: 'test-results/customer-order-images.png' })
  const worker = await context.newPage()
  await login(worker, 'worker')
  await worker.goto('/worker/offers')
  await worker
    .locator('article')
    .filter({ hasText: `订单 ${id}` })
    .getByRole('button', { name: '查看详情 / 接单' })
    .click()
  await expect(worker.getByRole('button', { name: '预览现场图片 1', exact: true })).toBeVisible()
  await expect(worker.getByText('本次家人', { exact: false })).toHaveCount(0)
  await expect(worker.getByText('虚构路8号', { exact: false })).toHaveCount(0)
  const admin = await context.newPage()
  await login(admin, 'admin')
  await admin.goto(`/admin/orders/${id}`)
  await expect(admin.getByRole('button', { name: '预览现场图片 3', exact: true })).toBeVisible()
  await page.goto('/customer/profile')
  await page.getByLabel('称呼', { exact: true }).fill('更新称呼')
  await page.getByLabel('个人联系电话').fill('13800000007')
  await page.getByRole('button', { name: '保存个人信息' }).click()
  await expect(page.getByRole('status')).toContainText('个人资料已保存')
  await page.goto('/customer/addresses')
  await expect(page.locator('article').filter({ hasText: '虚构路8号' })).toContainText(
    '家人地址联系人',
  )
  await page.goto(`/customer/orders/${id}`)
  await expect(page.getByText('本次家人 · 13800000009')).toBeVisible()
  expect(errors).toEqual([])
})

test('图片类型大小数量、失败重试和报价边界', async ({ page }) => {
  const png = await imageFixture(page)
  await login(page)
  await book(page, 'OFFER')
  const input = page.locator('input[type=file]')
  await input.setInputFiles({
    name: 'bad.svg',
    mimeType: 'image/svg+xml',
    buffer: Buffer.from('<svg/>'),
  })
  await expect(page.getByRole('alert')).toContainText('仅支持')
  await page.getByRole('button', { name: '移除待上传图片' }).click()
  await input.setInputFiles({
    name: 'big.png',
    mimeType: 'image/png',
    buffer: Buffer.alloc(6 * 1024 * 1024),
  })
  await expect(page.getByRole('alert')).toContainText('不能超过')
  await page.getByRole('button', { name: '移除待上传图片' }).click()
  await input.setInputFiles(
    Array.from({ length: 4 }, (_, i) => ({ name: `${i}.png`, mimeType: 'image/png', buffer: png })),
  )
  await expect(page.getByRole('alert')).toContainText('最多上传 3 张')
  await page.evaluate(async () => {
    const path = '/src/mock/transport.ts'
    ;(await import(path)).simulateNetworkFailure()
  })
  await input.setInputFiles({ name: 'scene.png', mimeType: 'image/png', buffer: png })
  await expect(page.getByRole('button', { name: '重试上传' })).toBeVisible()
  await expect(page.getByRole('button', { name: '提交预约并去支付' })).toBeDisabled()
  await page.getByRole('button', { name: '重试上传' }).click()
  await expect(page.getByRole('button', { name: '预览现场图片 1', exact: true })).toBeVisible()
  for (const [value, message] of [
    ['abc', '有效金额'],
    ['131', '5元整数倍'],
    ['160', '低于标准价'],
    ['125', '报价须在'],
  ]) {
    await page.getByLabel('当前报价').fill(value)
    await page.getByLabel('当前报价').blur()
    await expect(page.locator('.error-toast')).toContainText(message)
    await expect(page.getByRole('button', { name: '＋5元', exact: true })).toBeDisabled()
    await expect(page.getByLabel('当前报价')).toHaveValue(value)
  }
  await page.getByLabel('当前报价').fill('130.00')
  await page.getByRole('button', { name: '＋5元', exact: true }).click()
  await expect(page.getByLabel('当前报价')).toHaveValue('135.00')
  await page.getByRole('button', { name: '－5元', exact: true }).click()
  await expect(page.getByLabel('当前报价')).toHaveValue('130.00')
  await expect(page.getByRole('button', { name: '－5元', exact: true })).toBeDisabled()
})

test('订单卡片分组分页、客户调价冲突和取消退款', async ({ page, context }) => {
  await login(page)
  await page.getByRole('link', { name: '订单', exact: true }).click()
  await expect(page.locator('.order-card')).toHaveCount(6)
  await page.getByRole('button', { name: '下一页', exact: true }).click()
  await expect(page.getByText('2 / 5 · 27单')).toBeVisible()
  await page.getByRole('button', { name: '待确认', exact: true }).click()
  await expect(page.getByText('这里还没有订单。', { exact: false })).toBeVisible()
  await page.goto('/customer/orders/10002')
  await page.getByRole('button', { name: '调整报价', exact: true }).click()
  await page.getByLabel('新报价', { exact: true }).fill('135.00')
  await page.getByLabel(/我确认模拟补付/).check()
  const other = await context.newPage()
  await login(other)
  await other.goto('/customer/orders/10002')
  await other.getByRole('button', { name: '调整报价', exact: true }).click()
  await other.getByLabel('新报价', { exact: true }).fill('140.00')
  await other.getByLabel(/我确认模拟补付/).check()
  await other.getByRole('button', { name: '确认报价调整' }).click()
  await expect(other.getByRole('dialog')).toHaveCount(0)
  await expect(other.getByText('¥130.00 → ¥140.00')).toBeVisible()
  await expect(page.getByText('当前价 ¥130.00', { exact: true })).toBeVisible()
  await page.getByRole('button', { name: '确认报价调整' }).click()
  await expect(page.getByText(/原确认报价 ¥130.00；最新报价 ¥140.00/)).toBeVisible()
  await page.getByRole('button', { name: '查看最新报价', exact: true }).click()
  await page.getByRole('button', { name: '我已查看，按最新报价重新填写' }).click()
  await expect(page.getByText('当前价 ¥140.00', { exact: true })).toBeVisible()
  await page.getByLabel('新报价', { exact: true }).fill('135.00')
  await page.getByRole('button', { name: '确认报价调整' }).click()
  await expect(page.getByText('部分退款 · ¥5.00')).toBeVisible()
  await page.getByText('需要取消预约？', { exact: true }).click()
  await page.getByLabel('取消原因', { exact: true }).fill('行程有变')
  await page.getByRole('button', { name: '取消预约', exact: true }).click()
  await expect(page.getByRole('heading', { name: '已取消', exact: true })).toBeVisible()
  await expect(page.getByText('全额退款 · ¥135.00')).toBeVisible()
})

test('优惠提前量需明确切换、标准派单等待与超时退款、待支付退出保留', async ({ page }) => {
  await login(page)
  await page.evaluate(() => {
    const key = 'home-service.mock.v1',
      db = JSON.parse(localStorage.getItem(key)!)
    db.clockOffset = Date.parse('2026-10-03T09:00:00+08:00') - Date.now()
    db.workers.forEach((w: { dispatchEnabled: boolean }) => (w.dispatchEnabled = false))
    localStorage.setItem(key, JSON.stringify(db))
  })
  await book(page, 'OFFER')
  await page.getByRole('combobox', { name: '预约日期', exact: true }).selectOption('2026-10-03')
  await page.getByRole('combobox', { name: '开始时间', exact: true }).selectOption('12:00')
  await expect(page.getByRole('alert')).toContainText('至少提前12小时')
  await expect(page.getByLabel('当前报价')).toBeVisible()
  await page.getByRole('button', { name: '我选择改为标准预约' }).click()
  await expect(page.getByLabel('当前报价')).toHaveCount(0)
  await page.getByRole('button', { name: '提交预约并去支付' }).click()
  await expect(page).toHaveURL(/customer\/pay\/\d+/)
  const id = page.url().split('/').at(-1)!
  await page.getByRole('link', { name: '稍后处理 / 查看订单' }).click()
  await expect(page.getByRole('heading', { name: '待支付', exact: true })).toBeVisible()
  await page.getByRole('link', { name: /去支付 ¥/ }).click()
  await page.getByRole('button', { name: '确认模拟支付' }).click()
  await expect(page.getByText(/正在安排人员，每30秒重试/)).toBeVisible()
  await page.goto('/customer/development')
  await page.locator('.demo-panel summary').click()
  await page.getByRole('button', { name: '+5分钟', exact: true }).click()
  await expect(page.getByText('演示时间已推进', { exact: true })).toBeVisible()
  await page.goto(`/customer/orders/${id}`)
  await expect(page.getByRole('heading', { name: '已取消', exact: true })).toBeVisible()
  await expect(page.getByText('全额退款 · ¥160.00')).toBeVisible()
})
