# 家政预约与调度平台

面向客户、家政服务人员和平台管理员的家政预约与履约调度系统，用于 Java 后端实习求职项目展示。

Git仓库名为 `home-service`。当前已有前端子工程 `home-service-web`、OpenAPI 契约和27张表的数据库设计。`home-service-server` 目前只包含空库初始化 SQL，Java/Maven 后端骨架尚未实现。

## 项目特点

- 平台统一维护服务项目、规格、技能和价格。
- 标准预约由系统自动派单，清洁优惠预约由符合条件的人员自主抢单。
- 使用半小时时间槽统一处理排班、请假、服务占用和缓冲时间。
- 重点展示状态机、模拟支付、报价版本、幂等、并发抢单、自动调度、超时退款和 WebSocket通知。
- 采用模块化单体起步，不为展示技术强行拆分微服务。

## 计划技术栈

- Java 17、Spring Boot、MyBatis-Plus、MySQL
- Vue 3、Vite、TypeScript、Nginx
- OpenAPI、WebSocket、Docker Compose
- Redis和RabbitMQ只在后续出现明确业务价值时引入

## 文档入口

- [项目说明](docs/PROJECT.md)
- [当前状态](docs/STATUS.md)
- [数据库设计文档](docs/database/数据库设计文档.md)
- [空库初始化 SQL](home-service-server/database/init/home_service.sql)
- `docs/api/openapi.yaml`：接口设计阶段创建的正式接口契约

## 当前仓库结构

```text
home-service/
├── docs/                    # 项目说明、状态和唯一 OpenAPI 契约
│   └── database/            # 数据库中文设计文档
├── home-service-server/
│   └── database/
│       └── init/home_service.sql # 空库建表，无基础数据或测试数据
├── home-service-web/        # Vue 3 + Vite + TypeScript 前端独立工程
├── .gitignore
├── AGENTS.md
└── README.md
```

`home-service-web/src` 只存放前端源码；后续 Java 工程在平级的 `home-service-server` 内创建。初始化 SQL 需手动选择空项目库执行，不自动建库或清理已有表；此次重设计没有执行数据库操作。以后已有数据需要变更结构时，再新增 `database/migrations` 增量脚本；部署目录在开始部署时按实际组件创建。

## 仓库

GitHub：<https://github.com/Whisper4j/home-service>
