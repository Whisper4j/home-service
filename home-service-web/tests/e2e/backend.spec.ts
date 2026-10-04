import { test, expect } from '@playwright/test'
import type { Schema } from '../../src/api/types'
test('真实目录接口驱动首页与分组，不拦截或伪造接口', async ({ page, request }) => {
  const response = await request.get('/api/customer/service-entries')
  expect(response.ok()).toBe(true)
  const envelope = await response.json()
  expect(envelope.code).toBe('SUCCESS')
  const entries = envelope.data as Schema['ClientEntryVO'][]
  expect(Array.isArray(entries)).toBe(true)
  expect(entries.length, '数据库迁移初始化数据应至少包含一个入口').toBeGreaterThan(0)
  await page.goto('/customer/home')
  const first = [...entries].sort(
    (a, b) => a.groupSort - b.groupSort || a.groupCode.localeCompare(b.groupCode),
  )[0]!
  await page.getByRole('link', { name: first.groupName, exact: false }).click()
  await expect(page).toHaveURL(new RegExp(`/customer/groups/${first.groupCode}`))
  await expect(page.getByRole('heading', { name: first.groupName })).toBeVisible()
})
