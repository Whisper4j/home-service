import { defineConfig } from '@playwright/test'
const baseURL = process.env.E2E_BASE_URL
if (!baseURL)
  throw new Error(
    '真实联调需要 E2E_BASE_URL：同源部署的前端、Java 后端及已初始化数据库；不提供浏览器业务模拟。',
  )
export default defineConfig({
  testDir: './tests/e2e',
  workers: 1,
  timeout: 40_000,
  use: { baseURL, viewport: { width: 1440, height: 1000 }, trace: 'retain-on-failure' },
})
