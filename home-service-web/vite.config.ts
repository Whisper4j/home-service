import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  return {
    plugins: [vue()],
    server: {
      // 联调时在本机 .env.local 设置代理；部署由 Nginx 转发同源路径。
      proxy: env.DEV_API_TARGET ? {
        '/api': { target: env.DEV_API_TARGET, changeOrigin: true },
        '/ws': { target: env.DEV_API_TARGET, ws: true, changeOrigin: true },
      } : undefined,
    },
    test: { include: ['tests/unit/**/*.test.ts'], environment: 'node' },
  }
})
