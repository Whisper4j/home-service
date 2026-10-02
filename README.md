# 家政预约与调度平台

面向客户、家政服务人员和平台管理员的家政预约与履约调度系统，用于 Java 后端实习求职项目展示。

Git仓库名为 `home-service`，前端子工程为 `home-service-web`，后端子工程为 `home-service-server`。

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
- `docs/api/openapi.yaml`：接口设计阶段创建的正式接口契约

## 仓库

GitHub：<https://github.com/Whisper4j/home-service>
