# 当前状态

更新时间：2026-10-09

## 当前阶段

第二阶段后端基础工程已完成并通过验收。`home-service-server` 是 JDK 17 的 Maven 父工程，真实继承、聚合 `home-service-common` 和 `home-service-app`；app 单向依赖 common，只有 app 打包为可执行 Spring Boot JAR。核心业务保留给用户练习，没有假成功接口或生产测试登录。

本次实际连接核验了 `127.0.0.1:3306/home_service`：MySQL 8.0.45，现有库已初始化且与当前唯一 SQL 基线一致。此前“新 SQL 尚未执行、后端尚未创建”的状态已失效；本次没有执行初始化、DDL、迁移或种子 SQL，也没有清库、修改现有业务记录。当前不存在等待用户初始化才能完成的数据库映射验收项。

## 已完成内容

- 固定版本：Spring Boot 3.5.16、MyBatis-Plus 3.5.17（Boot 3 starter + jsqlparser）、Hutool core 5.8.47、JJWT 0.13.0；底层依赖沿用 Boot 管理。真实读取三个参考工程的全部 POM、配置及代表性分层/基础设施代码，以 hmall 为主要规范。
- 27 个 PO、27 个 BaseMapper，覆盖所有关联表；主键策略、金额、时间、NULL 逻辑删除、时间填充、JSON 类型处理、生成列禁止写入及分页插件均已配置。全局枚举由28个精简为7个，只保留 `Role`、`AccountStatus`、`CatalogStatus`、`ServiceKind`、`BookingType`、`OrderStatus`、`ErrorCode`；它们沿用英文持久化/JSON值并增加中文说明。其他状态字段改用字符串，业务常量留待实现所属业务时就近定义。
- OpenAPI 的 DTO/Query/VO、嵌套对象和 WebSocket 帧：79 个具体对象模型；响应和分页包装用 `Result<T>`、`PageDTO<T>` 泛型等价表达。app 下 111 个 domain 类及 common 的 `Result/PageQuery/PageDTO` 已统一为容易阅读的普通 Lombok 类，不再使用 record 或 Builder；字段均使用简短行尾中文注释，import 已按实际使用清理。请求、持久化与输出继续分离；未实现 `allowedActions` 的业务计算。
- DTO/Query 只保留真实输入格式和基础交叉字段校验，每个约束均有中文 `message`；VO 已移除输入校验；PO 只保留主键、逻辑删除、字段填充、JSON 类型处理和生成列等必要 MyBatis-Plus 注解。OpenAPI YAML 仍是唯一正式接口契约，不在 Java 模型中重复维护 Swagger 注解。
- `Result<T>` 为苍穹风格的 Lombok 普通类并实现 `Serializable`，现有两个 `success` 和三个 `error` 重载均经测试覆盖；字段仍为契约要求的 `code/message/data`，成功码为字符串 `SUCCESS`。
- 严格 ID/金额/+08:00 时间格式、必需 nullable 与可选省略、SKU 入口字段缺失/显式 null 区分、未知字段拒绝、字符串裁剪、密码不裁剪与 UTF-8 72 字节限制、查询/路径转换及基础交叉字段校验。
- 异常分工已固定并写入项目规则：DTO/Query 负责请求格式；Service 负责数据库状态、权限、事务和并发业务规则并抛业务异常；全局异常处理器统一生成失败 `Result`。MVC 已验证注解中的精确中文 `message` 会进入 `data.fieldErrors`，同时保留稳定业务码和真实 HTTP 状态；空结果和超末页保留真实 total/pages。
- BCrypt、配置化 HS256 JWT、严格 Bearer/算法/签名/有效期/载荷检查、三端角色检查、真实账号只读查询服务、方法级公开白名单及请求上下文清理。
- `/ws` 首帧 AUTH、5 秒超时、共享身份校验、AUTHENTICATED、4401/4403 关闭、Token 到期、连接清理、同源/显式 Origin 配置。通知仅支持明确接收者并在事务提交后发送，没有业务广播和资格过滤。
- common/dev/test/prod 配置结构、校验后的属性类、本机无秘密示例、仅上传目录配置。应用启动必须连接真实 MySQL；SQL 自动初始化关闭。连接会话使用 `+08:00`，不要求本机 MySQL 安装命名时区表。

稳定工程边界、配置方法、IDEA/Maven 启动步骤和后续 Service 约束已写入 `docs/PROJECT.md` 第14节。

## 实际执行与验证结果

实际使用 JDK `17.0.12`、Maven `3.9.14`；以下 Maven 命令在仓库根目录执行，数据库凭据仅由当前进程环境提供，测试 JWT 密钥每次随机生成。

| 实际命令或检查 | 结果 |
| --- | --- |
| `mvn -B -ntp -f home-service-server/pom.xml clean verify`（2026-10-09） | JDK 17.0.12 下 BUILD SUCCESS；common 2项、app 22项，共24项测试，0失败/错误/跳过；验证普通 Lombok 模型、完整契约字段、序列化/校验、Result 重载、MVC 精确字段错误及原有基础设施行为 |
| `mvn -B -ntp -f home-service-server/pom.xml clean verify -Pmysql-it`（2026-10-09） | JDK 17.0.12 下 BUILD SUCCESS；common 2项 + app 22项常规测试 + 3项真实 MySQL 集成测试，共27项，0失败/错误/跳过；重新验证27表 PO/Mapper、特殊 JSON、生成列和真实账号映射 |
| `mvn -B -ntp -f home-service-server/pom.xml dependency:tree` | SUCCESS；app→common，分页 jsqlparser 已解析，只有一套 MyBatis starter，没有 Redis/MQ/Cloud/PageHelper/Fastjson/H2 |
| `java -jar home-service-app/target/home-service-app-1.0.0-SNAPSHOT.jar --spring.profiles.active=dev` | 独立进程真实启动，验证端口18080，成功连接 MySQL 8.0.45；不是只打包或测试上下文启动 |
| 独立 JAR 的 HTTP 请求 | 无 Token 的 `/api/customer/auth/me` 为401；公开地区 GET 和登录 POST 尚无业务 Controller，正确404；地区 POST 仍需认证并返回401。均使用统一 JSON 响应 |
| JAR 内容检查 | app 含启动类和 common/运行依赖；不含测试 Controller、账号替身或本机配置；common 为普通 JAR |
| `npm run api:generate`、`npm run api:check`（web目录） | 86个 HTTP 操作，语法/引用/派生文件一致 |
| `npm run build`（web目录） | TypeScript 与 Vite 构建成功；不是业务联调结论 |
| `git diff --check`、源码与依赖检查 | 通过；无字段注入、循环依赖放行、硬编码凭据、超范围业务实现或受跟踪构建产物 |

独立 JAR 仅用于验收，验证后已停止该进程。下次运行需配置自己的本机密码/JWT 密钥，默认端口8080；示例中不提供可用固定密钥。

### 数据库证据与测试隔离

- 实际结构与 SQL 一致：27张 InnoDB 表、305个字段、27个主键、14个额外唯一索引、88个启用 CHECK、0普通查询二级索引、0物理外键。检查列名/类型/可空/默认值/排序规则/自增/生成表达式及索引字段顺序；未迁就实际结构修改 SQL。
- 全部27个 Mapper 已注册并向实际表执行只读列投影；另核对 PO 主键、字段及逻辑删除/生成列写入策略。
- 少量夹具在同线程显式回滚事务中验证：账号查询、时间、保留枚举、字符串状态、金额、分页超末页、NULL 逻辑删除、JSON NULL/空数组/对象数组/字符串数组、幂等响应与生成列不写入；退出后复核夹具记录未保留。已有设置行只读，未修改已有数据；MySQL 自增序列可能产生正常间隙。
- MVC 使用仅在测试源集中的 Controller，验证认证与异常链路、角色/禁用/缺失账号、公开方法边界、参数格式及上下文清理；JWT 覆盖有效、过期、篡改和算法错误，BCrypt 覆盖匹配及多字节边界。
- WebSocket 通过真实随机端口网络连接测试认证成功、缺少 AUTH 超时、无效/过期 Token、禁用/角色错误、到期断开、清理、URL Token/Origin 拒绝；账号查询替身仅用于协议测试。真实账号 Mapper 与数据库映射由独立 mysql-it 验证，未把替身结果当作数据库结果。
- 没有运行数据库初始化或迁移，没有 H2。基础设施/映射通过不代表业务事务、资源归属、状态机、并发抢单、性能或前后端闭环通过；没有普通索引不构成性能保证。

## 契约与已有修改

保留原有数据库设计文档修改；唯一初始化文件 `database/init/home_service.sql` 未修改。OpenAPI 仅补充有依据的说明：公开目录不接受 status、ID 为正的有符号 BIGINT 范围、BCrypt UTF-8 字节上限；没有改路径、字段、类型、枚举或错误码。已同步派生 TypeScript/JSON 并通过契约检查。

所有修改留在工作区，没有 Git 提交或推送。构建产物、本机配置及临时验证文件被忽略；密码、签名密钥未写入受 Git 跟踪或待添加的项目文件。不创建本轮报告、交接文件或额外 SQL。

## 下一步业务练习

1. 从三端登录、客户注册和 me 开始，复用 PasswordEncoder、JwtTool、只读账号服务与上下文；按契约补充正式 Service/Controller、注册幂等和资料 VO。
2. 实现地区/平台规则及目录只读查询，再做客户地址簿：维护默认地址必须先在事务内锁定客户账号行，不能只加 `@Transactional`。
3. 实现管理员目录、人员/技能、排班请假和时间槽；SKU 删除同事务释放入口，排班遵守项目锁定顺序。
4. 逐步实现标准预约、支付、派单与履约，再扩展优惠抢单、报价/补差/退款、评价与统计。分配/占槽/订单条件更新同事务，成功分配每单最多一条；历史读快照。
5. 业务通知先完成接收者归属/资格过滤，在提交后调用通知基础设施；附件上传、资源归属鉴权、业务定时任务、基础数据准备和真实前后端闭环仍待后续完成。
