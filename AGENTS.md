# Codex 工作规则

开始任务时读取 `docs/PROJECT.md` 和 `docs/STATUS.md`；`README.md` 只用于 GitHub 展示，不是工作上下文。

- 项目用于 Java 后端实习求职，业务闭环、正确性、可解释性和完成度优先于技术堆砌。
- 后端采用模块化单体；未经用户确认，不拆微服务，不添加没有业务依据的中间件。
- hmall 是主要代码风格来源：采用 `controller`、`domain/{dto,po,query,vo}`、`service/impl`、`mapper`、`config`、`handler`、`enums` 分层，并沿用统一响应、`PageQuery/PageDTO`、业务异常、全局异常和 `UserContext` 思路。
- 苍穹外卖只借鉴多端接口分组、DTO/VO 隔离、统一结果、配置与异常处理；不照搬字段注入、手写简单 CRUD 或外卖业务。
- 黑马点评只借鉴缓存、幂等、条件更新和并发分析方法；Redis/Lua 不得脱离本项目业务强行使用。
- Java 使用 JDK 17；合理使用 Lombok；依赖注入统一为 `@RequiredArgsConstructor` 加 `private final`，禁止字段注入。
- Service 接口使用 `IXxxService`，实现类使用 `XxxServiceImpl`；Mapper 使用 `XxxMapper`；类、方法和字段使用清晰业务名称。
- Controller 按 `customer`、`worker`、`admin` 分组并保持轻量；事务、状态校验和业务规则放在 Service。
- 输入使用 DTO/Query，数据库映射使用 PO，输出使用 VO；接口禁止直接暴露 PO。
- `domain` 模型使用普通 Lombok 类，字段后写简短 `// 中文含义`；不使用 record 或 Builder 堆叠。DTO/Query 只保留输入格式校验并填写中文 `message`，VO 不承担输入校验，PO 只保留确有作用的 MyBatis-Plus 映射注解。
- 简单 CRUD 使用 MyBatis-Plus；复杂联表、批量条件更新或性能敏感 SQL 才使用 Mapper XML。
- 接口遵守 RESTful 语义，统一参数校验、响应、分页、异常和错误码；金额禁止使用 `double`。
- Controller 正常路径只返回成功结果；依赖数据库和业务状态的规则由 Service 校验并抛业务异常，失败响应统一由全局异常处理器生成，不在 Controller/Service 中散落 `Result.error(...)`。
- API统一以 `/api` 开头，三端前缀为 `/api/customer`、`/api/worker`、`/api/admin`；响应字段为 `code/message/data`，业务错误码使用字符串。
- 分页请求使用 `pageNo/pageSize`，分页响应使用 `list/total/pages`；幂等写请求使用 `Idempotency-Key`。
- 对外 ID 按字符串传输，金额按两位小数字符串传输，时间使用带 `+08:00` 偏移的 ISO 8601，业务时区统一为 `Asia/Shanghai`。
- 认证使用 BCrypt 与 HS256 JWT访问令牌；请求使用 `Authorization: Bearer <JWT>`，密钥和有效期配置化，严禁提交密钥。
- 枚举和常量表达稳定状态，禁止散落魔法数字；状态变化必须校验来源状态和操作者权限。
- 写操作必须考虑事务、幂等和并发；MySQL 是最终正确性来源，Redis 不能作为唯一保障。
- 不创建 `reserve1`、`reserve2` 或万能 JSON 预留未知需求；新需求通过迁移脚本和合理的新表演进。
- 前端保持低保真，但路由、表单、筛选、分页、滚动、状态操作、异常提示和接口字段必须真实可用。
- `docs/api/openapi.yaml` 创建后即为唯一正式接口契约和代码生成来源；Java 模型不重复维护 Swagger 契约注解，前后端不得各自猜测字段、状态或错误码。
- WebSocket只负责实时通知；任何抢单、报价和状态结果仍以 HTTP 接口与数据库为准。
- 需求与文档足以支撑实现时直接工作；低风险的命名、文案、演示数据和实现细节由执行者合理决定并记录，不先停下来要求用户逐项确认。
- 不提交密钥、真实个人数据或本机专用配置；保留用户已有修改，不执行破坏性 Git 操作。
- 实现后运行相称的测试或构建，不以“已生成代码”代替验证。
- 每次任务结束覆盖更新 `docs/STATUS.md`；仅当稳定业务或技术决定变化时更新 `docs/PROJECT.md`。
- 不创建每轮任务文档、交接文档或流水账；历史过程由 Git 提交保存。
