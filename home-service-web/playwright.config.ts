import { defineConfig } from '@playwright/test'
export default defineConfig({
  testDir: './tests/e2e',
  fullyParallel: false,
  workers: 1,
  timeout: 40_000,
  expect: { timeout: 8_000 },
  use: {
    baseURL: 'http://127.0.0.1:5179',
    viewport: { width: 1440, height: 1000 },
    trace: 'retain-on-failure',
  },
  webServer: [
    {
      command: 'npm run dev -- --host 127.0.0.1 --port 5179 --strictPort',
      url: 'http://127.0.0.1:5179',
      reuseExistingServer: !process.env.CI,
      env: { VITE_USE_MOCK: 'true' },
    },
    {
      command: 'npm run dev -- --host 127.0.0.1 --port 5180 --strictPort',
      url: 'http://127.0.0.1:5180',
      reuseExistingServer: !process.env.CI,
      env: { VITE_USE_MOCK: 'false' },
    },
  ],
})
