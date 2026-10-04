# 家政预约与调度平台前端

Vue 3 + Vite + TypeScript，独立 npm 工程。使用 Node.js 22.12+，可直接用 VS Code 或 IDEA 打开本目录。

```sh
npm install
npm run dev
npm run build
```

客户端 `/customer/home`；三端独立登录入口 `/customer/login`、`/worker/login`、`/admin/login`。账号及业务数据由后端和数据库提供，前端没有默认测试账号或离线业务数据库。当前尚无 Java 后端，接口请求失败会明确显示错误，不会生成模拟成功结果。

## 同源接口与 Nginx 联调

HTTP 始终请求 `/api`，WebSocket 始终连接同源 `/ws`。开发时可在不提交的 `.env.local` 设置 `DEV_API_TARGET` 为实际后端 origin，由 Vite 代理两个路径；未配置时不代理，也没有后备业务数据。不要将后端主机、端口或密钥写入源码。

部署时由 Nginx 提供构建后的 `dist`，将 `/api` 按原路径转发到后端；`/ws` 配置 HTTP/1.1、Upgrade 和 Connection 请求头及合理连接超时。API 和 WebSocket 路径必须优先于 SPA history 回退，不能把 API 错误改成 `index.html`。前端业务路由使用 `try_files $uri $uri/ /index.html`。实际 upstream 由部署环境设置。

唯一契约为 `../docs/api/openapi.yaml`。HTTP 使用 Bearer Token；WebSocket 连接后发送 AUTH 首帧，JWT 不放入 URL。收到通知或断线重连后重新执行 HTTP 查询；确认中的报价保留原版本，冲突须重新确认。支付仍为项目的后端模拟支付接口，不连接真实支付渠道。

## 验证

```sh
npm run api:generate
npm run api:check
npm run format:check
npm test
npm run build
```

生成物包含 TypeScript 类型、路由元数据和 JSON Schema 镜像；不手工修改生成文件。单元测试检查金额/步长边界、时区以及契约职责，不模拟数据库。

真实端到端测试：先启动同源前端、Java 后端及迁移初始化后的数据库，设置 `E2E_BASE_URL` 为实际部署 origin，再执行 `npx playwright install chromium` 和 `npm run test:e2e`。未配置 origin 时测试明确拒绝启动；不拦截接口伪造返回，不启动浏览器业务模拟。目前仅提供真实目录到页面的联调冒烟用例；登录、创建/支付、调度、抢单并发、履约、图片权限及定时任务的完整联调仍等待后端和数据库。

`src/api` 管理契约类型、请求与通知；`src/views/{customer,worker,admin}` 按端组织页面；其他目录分别负责布局、路由、组件、组合逻辑和登录状态。页面只在内存中展示后端数据；sessionStorage 仅保留登录会话、按账号隔离的未提交草稿、幂等键和导航上下文，不作为业务事实来源。历史浏览器演示数据不会被新代码读取或迁移。

不提交 `node_modules`、`dist`、测试产物、本机配置或密钥。
