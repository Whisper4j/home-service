# 家政预约与调度平台前端

Vue 3 + Vite + TypeScript，独立 npm 工程。使用 Node.js 22.12+，可直接用 VS Code 或 IDEA 打开本目录。

```sh
npm install
npm run dev
npm run build
```

默认使用浏览器本地 Mock，入口 `/customer/login`、`/worker/login`、`/admin/login`。演示账号：`customer`、`customer2`、`worker`、`worker2`、`repair`、`admin`；密码统一为 `Demo12345`。所有联系人、地址、订单为虚构演示数据。

三端可在不同标签页登录，共享业务数据、隔离登录令牌。Mock 通过 Web Locks 串行修改 localStorage，使用 BroadcastChannel 模拟在线通知；请使用现代浏览器及 localhost。顶部演示工具可推进时间、模拟断网、重置数据。订单详情可推进到预约开始时间，客户端查看开始码，人员端输入后开始服务。

Mock 是可操作的产品原型，不是后端并发实现或安全边界。Mock 令牌明确以 `mock.` 开头；真实模式只接收后端签发的 HS256 JWT。后端 BCrypt、MySQL 事务/行锁、定时任务和 WebSocket 服务仍需后续实现与验证。

## 接入真实接口

在未提交的 `.env.local` 中设置 `VITE_USE_MOCK=false`。开发时可同时设置 `DEV_API_TARGET` 为自己的后端 origin；值不写入仓库。请求保持同源 `/api`，WebSocket 保持 `/ws`；部署时由 Nginx 代理这两个路径，并为前端 history 路由配置 `try_files $uri $uri/ /index.html`。

唯一契约在 `../docs/api/openapi.yaml`。WebSocket 认证使用连接后的 AUTH 首帧，不把 JWT 放入 URL。断线重连后重新执行 HTTP 查询；确认中的报价保持原版本，冲突由用户重新确认。

```sh
npm run api:generate # 修改契约后再生成类型、路由元数据和Mock校验Schema
npm run api:check    # 校验OpenAPI语法、引用和生成产物一致性
npm test            # 业务边界与契约测试
npx playwright install chromium
npm run test:e2e     # 浏览器交互验证
```

`src/api` 放契约类型、集中请求和通知；`src/mock` 放独立演示业务；`src/views/{customer,worker,admin}` 按端组织页面，复用视图在 `shared`；`layouts`、`router`、`components`、`composables` 和 `stores` 各自负责布局、路由、组件、组合逻辑与登录状态。不提交 `node_modules`、`dist`、本机环境文件或密钥。
