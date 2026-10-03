export interface paths {
    "/customer/auth/login": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /**
         * 用户名密码登录
         * @description 三端独立入口，拒绝非对应角色。密码 BCrypt；HS256 JWT 至少包含 accountId、role、exp。有效期/密钥配置化。不实现刷新令牌、黑名单或服务端退出，退出仅删除客户端令牌。
         *     无需登录。
         */
        post: operations["customerLogin"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/auth/me": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 当前账号
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["customerCurrentAccount"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/auth/login": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /**
         * 用户名密码登录
         * @description 三端独立入口，拒绝非对应角色。密码 BCrypt；HS256 JWT 至少包含 accountId、role、exp。有效期/密钥配置化。不实现刷新令牌、黑名单或服务端退出，退出仅删除客户端令牌。
         *     无需登录。
         */
        post: operations["workerLogin"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/auth/me": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 当前账号
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["workerCurrentAccount"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/auth/login": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /**
         * 用户名密码登录
         * @description 三端独立入口，拒绝非对应角色。密码 BCrypt；HS256 JWT 至少包含 accountId、role、exp。有效期/密钥配置化。不实现刷新令牌、黑名单或服务端退出，退出仅删除客户端令牌。
         *     无需登录。
         */
        post: operations["adminLogin"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/auth/me": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 当前账号
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["adminCurrentAccount"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/auth/register": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /**
         * 客户注册
         * @description 无需登录。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["customerRegister"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/regions": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 服务城市与行政区
         * @description 无需登录。
         */
        get: operations["listServiceRegions"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/booking-rules": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 预约规则
         * @description 无需登录。
         */
        get: operations["getBookingRules"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/categories": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 浏览上架服务目录
         * @description 仅返回本层及父级均上架数据；status 只能省略或 ON_SHELF。
         *     无需登录。
         */
        get: operations["listCustomerCategory"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/service-items": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 浏览上架服务目录
         * @description 仅返回本层及父级均上架数据；status 只能省略或 ON_SHELF。
         *     无需登录。
         */
        get: operations["listCustomerServiceItem"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/skus": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 浏览上架服务目录
         * @description 仅返回本层及父级均上架数据；status 只能省略或 ON_SHELF。
         *     无需登录。
         */
        get: operations["listCustomerSku"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/skus/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 规格详情
         * @description 无需登录。
         */
        get: operations["getCustomerSku"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/addresses": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 地址列表
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["listAddresses"];
        put?: never;
        /**
         * 新增地址
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。 首个地址自动设为默认；默认地址始终唯一。
         */
        post: operations["createAddress"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/addresses/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        /**
         * 修改地址与默认标记
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。 设置默认时原默认自动取消；若把当前默认设为非默认且有其他地址，则自动选择最早创建的剩余地址为默认。
         */
        put: operations["updateAddress"];
        post?: never;
        /**
         * 删除地址
         * @description 不影响历史订单地址快照；删除默认地址后自动选最早剩余地址为默认。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。 删除默认地址时最早创建的剩余地址成为默认，无剩余地址则无默认地址。
         */
        delete: operations["deleteAddress"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/orders": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 分页查询订单
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["customerListOrders"];
        put?: never;
        /**
         * 创建预约
         * @description 保存服务/地址/价格范围快照。待支付15分钟；支付前不占槽、不承诺人员可用性。不提供修改规格/地址/时间接口，取消重下。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["createOrder"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/orders/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 订单详情
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["customerGetOrder"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/orders/{id}/history": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 支付、报价、分配与评价历史
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["customerGetOrderHistory"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/orders": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 分页查询订单
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["workerListOrders"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/orders/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 订单详情
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["workerGetOrder"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/orders/{id}/history": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 支付、报价、分配与评价历史
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["workerGetOrderHistory"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/orders": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 分页查询订单
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["adminListOrders"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/orders/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 订单详情
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["adminGetOrder"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/orders/{id}/history": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 支付、报价、分配与评价历史
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["adminGetOrderHistory"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/orders/{id}/payments": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /**
         * 模拟支付
         * @description 仅 PENDING_PAYMENT 且未过期。STANDARD 支付后同步按当天已分配服务分钟数、订单数、人员ID排序，有限候选尝试；无可用人员 WAITING_DISPATCH，每30秒扫描，5分钟超时取消全额退款。OFFER 进入 WAITING_ACCEPTANCE，截止 min(支付成功+2小时,开始-6小时)。支付成功与分配成功分别表达。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["payOrder"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/orders/{id}/offer": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        /**
         * 调整优惠报价并模拟补差或退款
         * @description 预期金额或版本不匹配返回409 PRICE_CHANGED（含当前金额和版本）。价格与抢单竞争按订单条件更新裁决。涨价需客户明确确认模拟补差，降价同事务部分退款。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        put: operations["changeOffer"];
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/orders/{id}/cancellations": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /**
         * 客户取消预约
         * @description 仅 PENDING_PAYMENT/WAITING_DISPATCH/WAITING_ACCEPTANCE/PENDING_SERVICE。已出发及以后禁止。已支付退净已收金额并释放服务及缓冲槽，根据当前排班恢复 AVAILABLE/NON_WORKING。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["cancelCustomerOrder"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/orders/{id}/start-code": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 客户查看服务开始码
         * @description 仅本人订单 PENDING_SERVICE/DEPARTED/ARRIVED/IN_SERVICE 可查询。开始码不返回人员或管理员。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["getStartCode"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/orders/{id}/confirmations": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /**
         * 客户确认完成
         * @description 仅 PENDING_CONFIRMATION；人员提交完成24小时后系统自动完成。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["confirmOrder"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/orders/{id}/reviews": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /**
         * 评价订单
         * @description 仅本人 COMPLETED 订单且每单一次；不提供修改/删除/回复。重复评价返回409 REVIEW_EXISTS。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["createReview"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/profile": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 当前人员资料与技能
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["getWorkerProfile"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/schedule": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 读取统一排班模板
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["getSchedule"];
        /**
         * 设置统一每日区间和固定休息日
         * @description 所有非休息日复用相同区间；首次设置生成未来30天槽，后续仅更新空闲/非工作槽。不得覆盖请假或已占用槽，缩短区间/新增休息日影响 SERVICE/BUFFER 返回409 SCHEDULE_CONFLICT。生成幂等、启动补漏、零点补齐。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        put: operations["updateSchedule"];
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/slots": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 查看某天时间槽
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["listWorkerSlots"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/leaves": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 分页查看请假
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["listLeaves"];
        put?: never;
        /**
         * 提交请假
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["createLeave"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/leaves/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        post?: never;
        /**
         * 撤销尚未开始的请假
         * @description 仅开始前可撤销，保留请假记录，将 LEAVE 恢复为当前排班对应状态。不得修改已有订单。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        delete: operations["cancelLeave"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/offers": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 符合资格的优惠抢单池
         * @description 账号启用、允许派单、技能覆盖、城市匹配、服务及全部缓冲槽可用才可见。不泄漏详细地址/电话。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["listEligibleOffers"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/offers/{id}/claims": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /**
         * 按预期价格与版本抢单
         * @description 仅未截止 WAITING_ACCEPTANCE。PRICE_CHANGED 不静默成交；被抢 ORDER_TAKEN，截止 OFFER_CLOSED。订单状态+priceVersion条件更新与全部时间槽AVAILABLE条件占用同一事务，槽按时间顺序更新且影响行数必须完整；失败全回滚。成功锁定成交价、唯一分配和60分钟尾部缓冲。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["claimOffer"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/orders/{id}/departures": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /**
         * 出发
         * @description 仅已分配本人订单 PENDING_SERVICE → DEPARTED，禁止跳步。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["departOrder"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/orders/{id}/arrivals": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /**
         * 到达
         * @description 仅已分配本人订单 DEPARTED → ARRIVED，禁止跳步。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["arriveOrder"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/orders/{id}/starts": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /**
         * 开始服务
         * @description 仅已分配本人订单 ARRIVED → IN_SERVICE，禁止跳步。校验六位客户开始码且不得早于预约开始时间；开始码不是定位证明。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["startOrder"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/orders/{id}/completions": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /**
         * 提交完成
         * @description 仅已分配本人订单 IN_SERVICE → PENDING_CONFIRMATION，禁止跳步。写入24小时确认截止时间，不强制照片。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["finishOrder"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/categories": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 分页维护目录
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["listAdminCategory"];
        put?: never;
        /**
         * 新增Category
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["createAdminCategory"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/categories/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        /**
         * 更新Category
         * @description 服务目录上下架/价格修改记录审计；不改写订单快照。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        put: operations["updateAdminCategory"];
        post?: never;
        /**
         * 逻辑删除Category
         * @description 存在当前目录/人员引用时返回409 RESOURCE_IN_USE；历史快照不受影响。目录需先下架。所有删除记录审计，不物理删除订单及财务历史。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        delete: operations["deleteAdminCategory"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/service-items": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 分页维护目录
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["listAdminServiceItem"];
        put?: never;
        /**
         * 新增ServiceItem
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["createAdminServiceItem"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/service-items/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        /**
         * 更新ServiceItem
         * @description 服务目录上下架/价格修改记录审计；不改写订单快照。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        put: operations["updateAdminServiceItem"];
        post?: never;
        /**
         * 逻辑删除ServiceItem
         * @description 存在当前目录/人员引用时返回409 RESOURCE_IN_USE；历史快照不受影响。目录需先下架。所有删除记录审计，不物理删除订单及财务历史。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        delete: operations["deleteAdminServiceItem"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/skus": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 分页维护目录
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["listAdminSku"];
        put?: never;
        /**
         * 新增Sku
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["createAdminSku"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/skus/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        /**
         * 更新Sku
         * @description 服务目录上下架/价格修改记录审计；不改写订单快照。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        put: operations["updateAdminSku"];
        post?: never;
        /**
         * 逻辑删除Sku
         * @description 存在当前目录/人员引用时返回409 RESOURCE_IN_USE；历史快照不受影响。目录需先下架。所有删除记录审计，不物理删除订单及财务历史。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        delete: operations["deleteAdminSku"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/skills": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 分页维护目录
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["listAdminSkill"];
        put?: never;
        /**
         * 新增Skill
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["createAdminSkill"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/skills/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        /**
         * 更新Skill
         * @description 服务目录上下架/价格修改记录审计；不改写订单快照。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        put: operations["updateAdminSkill"];
        post?: never;
        /**
         * 逻辑删除Skill
         * @description 存在当前目录/人员引用时返回409 RESOURCE_IN_USE；历史快照不受影响。目录需先下架。所有删除记录审计，不物理删除订单及财务历史。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        delete: operations["deleteAdminSkill"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/accounts": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 账号列表
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["listAccounts"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/accounts/{id}/status": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        /**
         * 启用或禁用账号
         * @description 禁止禁用初始化管理员；有未完成分配的人员禁止禁用，需先处理异常订单。每次请求仍校验账号状态；不实现Token黑名单或强制下线。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        put: operations["setAccountStatus"];
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/accounts/{id}/profile": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        /**
         * 维护账号联系资料
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        put: operations["updateAccountProfile"];
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/workers": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 服务人员列表
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["listWorkers"];
        put?: never;
        /**
         * 创建服务人员账号和资料
         * @description 仅管理员创建人员。初始无排班，槽为NON_WORKING；人员首次设置排班后参与调度。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["createWorker"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/workers/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        /**
         * 维护人员技能与派单开关
         * @description 派单开关只影响新的分配；已有订单继续履约。移除已分配订单所需技能返回409 RESOURCE_IN_USE。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        put: operations["updateWorker"];
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/workers/{id}/slots": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 查看人员时间槽
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["getAdminWorkerSlots"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/orders/{id}/cancellations": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /**
         * 异常取消并退款
         * @description 必须填写异常原因。允许所有非终态订单；全额退净收款并释放占用、保留分配历史及审计。COMPLETED/CANCELLED拒绝新操作；同Key重放仍返回原成功结果。无手工派单/改派。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        post: operations["cancelAdminOrder"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/dispatch-attempts": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 候选筛选、调度尝试和失败原因
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["listDispatchAttempts"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/payments": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 支付、补差及退款流水
         * @description 退款由调价或异常取消产生，无任意金额的手动重复退款接口。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["listPayments"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/audits": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 关键操作审计
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["listAudits"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/settings": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 读取平台规则
         * @description 校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。
         */
        get: operations["getSettings"];
        /**
         * 修改预约窗口
         * @description earliestHours 必须小于 latestDays*24；变更写审计。
         *     校验 JWT、对应角色和账号启用状态；资源所属关系在服务端校验。写操作遵循 Idempotency-Key；业务写入、流水及资源占用须在同一事务内完成。
         */
        put: operations["updateSettings"];
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/service-entries": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 获取固定客户端入口与正式 SKU 的可预约关系
         * @description 始终返回全部固定入口。available=true 时提供 sku；缺失、未绑定、下架或业务性质/时长不符时 available=false，仅提供不可预约原因。价格和能力来自关联的同一个正式 SKU。
         */
        get: operations["listClientEntries"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/profile": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        /**
         * 客户编辑本人称呼和联系电话
         * @description 只允许修改登录客户本人的 displayName 和 phone；username 不可修改。不自动更新地址簿或任何历史订单联系人。幂等写入。
         */
        put: operations["updateCustomerProfile"];
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/scene-images": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /**
         * 上传一张预约现场图片
         * @description multipart/form-data，字段 file。仅客户上传本人图片，单张不超过5 MiB，仅JPEG/PNG/WebP且验证真实内容并清理EXIF。相同 Idempotency-Key 的指纹包含文件内容摘要和 MIME，重试返回相同引用，不重复存储；不同内容返回 IDEMPOTENCY_CONFLICT。上传成功并不创建订单，创建订单最多关联3张。原型仅浏览器存储，正式服务端存储和鉴权待实现。
         */
        post: operations["uploadSceneImage"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/scene-images/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        post?: never;
        /**
         * 删除本人未关联订单的临时图片
         * @description 只能删除本人尚未关联任何订单的图片；已关联返回 RESOURCE_IN_USE，历史订单附件永不随移除预约草稿而删除。幂等重试返回首次结果。
         */
        delete: operations["deleteSceneImage"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/customer/scene-images/{id}/content": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 鉴权读取现场图片内容
         * @description 图片流成功响应是统一 JSON 响应的唯一例外：返回二进制图片、准确 Content-Type、Cache-Control: private, no-store 和 X-Content-Type-Options: nosniff。失败仍返回统一 ErrorResponse。必须 JWT/角色/账号启用校验，禁止公开 URL 和无权限列表。客户仅本人图片；服务人员必须为当前订单已分配人员，或订单仍待抢单且当前满足全部接单资格；管理员仅可读取指定订单已关联图片。人员/管理员必须提供 orderId，图片必须属于该订单。每次读取重新判权（接单、取消、排班变化后不再有抢单资格即拒绝）。不存在或无权均404。前端通过 Bearer fetch 得到 Blob 后创建临时对象 URL，退出/离页撤销。
         */
        get: operations["customerGetSceneImage"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/worker/scene-images/{id}/content": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 鉴权读取现场图片内容
         * @description 图片流成功响应是统一 JSON 响应的唯一例外：返回二进制图片、准确 Content-Type、Cache-Control: private, no-store 和 X-Content-Type-Options: nosniff。失败仍返回统一 ErrorResponse。必须 JWT/角色/账号启用校验，禁止公开 URL 和无权限列表。客户仅本人图片；服务人员必须为当前订单已分配人员，或订单仍待抢单且当前满足全部接单资格；管理员仅可读取指定订单已关联图片。人员/管理员必须提供 orderId，图片必须属于该订单。每次读取重新判权（接单、取消、排班变化后不再有抢单资格即拒绝）。不存在或无权均404。前端通过 Bearer fetch 得到 Blob 后创建临时对象 URL，退出/离页撤销。
         */
        get: operations["workerGetSceneImage"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/scene-images/{id}/content": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /**
         * 鉴权读取现场图片内容
         * @description 图片流成功响应是统一 JSON 响应的唯一例外：返回二进制图片、准确 Content-Type、Cache-Control: private, no-store 和 X-Content-Type-Options: nosniff。失败仍返回统一 ErrorResponse。必须 JWT/角色/账号启用校验，禁止公开 URL 和无权限列表。客户仅本人图片；服务人员必须为当前订单已分配人员，或订单仍待抢单且当前满足全部接单资格；管理员仅可读取指定订单已关联图片。人员/管理员必须提供 orderId，图片必须属于该订单。每次读取重新判权（接单、取消、排班变化后不再有抢单资格即拒绝）。不存在或无权均404。前端通过 Bearer fetch 得到 Blob 后创建临时对象 URL，退出/离页撤销。
         */
        get: operations["adminGetSceneImage"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
}
export type webhooks = Record<string, never>;
export interface components {
    schemas: {
        /**
         * @description 十进制字符串 ID，路径、查询、JSON 中均不得转为 JS number。
         * @example 10001
         */
        Id: string;
        /**
         * @description 人民币元，两位小数字符串。服务端 BigDecimal/decimal；前端比较和计算使用整数分。
         * @example 128.00
         */
        Money: string;
        /**
         * Format: date-time
         * @description Asia/Shanghai，必须显式 +08:00；不接受无偏移时间。
         * @example 2026-10-04T09:00:00+08:00
         */
        DateTime: string;
        /** @example 08:00 */
        LocalTime: string;
        /**
         * Format: date
         * @example 2026-10-04
         */
        LocalDate: string;
        /** @enum {string} */
        Role: "CUSTOMER" | "WORKER" | "ADMIN";
        /** @enum {string} */
        AccountStatus: "ENABLED" | "DISABLED";
        /** @enum {string} */
        CatalogStatus: "ON_SHELF" | "OFF_SHELF";
        /** @enum {string} */
        ServiceKind: "CLEANING" | "REPAIR" | "OTHER";
        /** @enum {string} */
        BookingType: "STANDARD" | "OFFER";
        /** @enum {string} */
        OrderStatus: "PENDING_PAYMENT" | "WAITING_DISPATCH" | "WAITING_ACCEPTANCE" | "PENDING_SERVICE" | "DEPARTED" | "ARRIVED" | "IN_SERVICE" | "PENDING_CONFIRMATION" | "COMPLETED" | "CANCELLED";
        /** @enum {string} */
        PaymentStatus: "UNPAID" | "PAID" | "PARTIALLY_REFUNDED" | "REFUNDED";
        /** @enum {string} */
        DispatchStatus: "NOT_REQUIRED" | "PENDING" | "SUCCEEDED" | "FAILED";
        /** @enum {string} */
        SlotStatus: "NON_WORKING" | "AVAILABLE" | "LEAVE" | "SERVICE" | "BUFFER";
        /** @enum {string} */
        PaymentType: "PAYMENT" | "TOP_UP" | "PARTIAL_REFUND" | "FULL_REFUND";
        /** @enum {string} */
        AssignmentStatus: "ACTIVE" | "RELEASED" | "FINISHED";
        /** @enum {string} */
        LeaveStatus: "ACTIVE" | "CANCELLED";
        /** @enum {string} */
        ActorType: "USER" | "SYSTEM";
        /** @enum {string} */
        ReviewTag: "PUNCTUAL" | "PROFESSIONAL" | "FRIENDLY";
        /** @enum {string} */
        DispatchAttemptResult: "ASSIGNED" | "INELIGIBLE" | "SLOT_CONFLICT" | "NO_CANDIDATE";
        /** @enum {string} */
        WsEventType: "OFFER_CREATED" | "OFFER_PRICE_CHANGED" | "ORDER_CLAIMED" | "ORDER_CLOSED" | "DISPATCH_SUCCEEDED" | "DISPATCH_FAILED" | "ORDER_STATUS_CHANGED";
        /** @enum {string} */
        ErrorCode: "VALIDATION_ERROR" | "UNAUTHENTICATED" | "TOKEN_EXPIRED" | "FORBIDDEN" | "ACCOUNT_DISABLED" | "INVALID_CREDENTIALS" | "USERNAME_EXISTS" | "NOT_FOUND" | "IDEMPOTENCY_CONFLICT" | "REQUEST_IN_PROGRESS" | "STATE_CONFLICT" | "PRICE_CHANGED" | "PRICE_OUT_OF_RANGE" | "SLOT_CONFLICT" | "SCHEDULE_CONFLICT" | "OUTSIDE_SERVICE_AREA" | "BOOKING_WINDOW_INVALID" | "OFFER_NOT_SUPPORTED" | "OFFER_CLOSED" | "ORDER_TAKEN" | "WORKER_INELIGIBLE" | "START_CODE_INVALID" | "REVIEW_EXISTS" | "CATALOG_UNAVAILABLE" | "RESOURCE_IN_USE" | "CONFIG_CONFLICT" | "INTERNAL_ERROR" | "IMAGE_TOO_LARGE" | "IMAGE_TYPE_UNSUPPORTED" | "IMAGE_NOT_AVAILABLE";
        PageQuery: {
            /** @default 1 */
            pageNo: number;
            /** @default 20 */
            pageSize: number;
        };
        LoginDTO: {
            /** @example customer */
            username: string;
            /**
             * Format: password
             * @example Demo12345
             */
            password: string;
        };
        RegisterDTO: {
            /** @example customer */
            username: string;
            /**
             * Format: password
             * @example Demo12345
             */
            password: string;
            displayName: string;
            phone: string;
        };
        AccountVO: {
            id: components["schemas"]["Id"];
            username: string;
            displayName: string;
            role: components["schemas"]["Role"];
            status: components["schemas"]["AccountStatus"];
            phone: string;
        };
        LoginVO: {
            accessToken: string;
            /** @enum {string} */
            tokenType: "Bearer";
            expiresAt: components["schemas"]["DateTime"];
            account: components["schemas"]["AccountVO"];
        };
        AccountStatusDTO: {
            status: components["schemas"]["AccountStatus"];
        };
        AccountProfileDTO: {
            displayName: string;
            phone: string;
        };
        AccountQuery: {
            /** @default 1 */
            pageNo: number;
            /** @default 20 */
            pageSize: number;
            keyword?: string;
            role?: components["schemas"]["Role"];
            status?: components["schemas"]["AccountStatus"];
        };
        /** @description 仅支持广东省广州市及其全部区。行政区编码与名称必须匹配；经纬度同时为空或同时提供。不按距离计价。设置默认地址原子清除旧默认。 */
        AddressDTO: {
            contactName: string;
            contactPhone: string;
            provinceCode: string;
            provinceName: string;
            cityCode: string;
            cityName: string;
            districtCode: string;
            districtName: string;
            detail: string;
            longitude: number | null;
            latitude: number | null;
            isDefault: boolean;
        };
        AddressVO: {
            id: components["schemas"]["Id"];
            contactName: string;
            contactPhone: string;
            provinceCode: string;
            provinceName: string;
            cityCode: string;
            cityName: string;
            districtCode: string;
            districtName: string;
            detail: string;
            longitude: number | null;
            latitude: number | null;
            isDefault: boolean;
        };
        RegionVO: {
            provinceCode: string;
            provinceName: string;
            cityCode: string;
            cityName: string;
            districts: {
                code: string;
                name: string;
            }[];
        };
        CategoryDTO: {
            name: string;
            sort: number;
            status: components["schemas"]["CatalogStatus"];
        };
        CategoryVO: {
            id: components["schemas"]["Id"];
            name: string;
            sort: number;
            status: components["schemas"]["CatalogStatus"];
        };
        /** @description 业务性质独立于可维护分类名称和ID。当前只有 CLEANING 可支持优惠，REPAIR 和 OTHER 仅标准；有支持优惠的SKU时不能改为非清洁。新增分类无需新增订单流程。 */
        ServiceItemDTO: {
            categoryId: components["schemas"]["Id"];
            name: string;
            serviceKind: components["schemas"]["ServiceKind"];
            description: string;
            status: components["schemas"]["CatalogStatus"];
        };
        ServiceItemVO: {
            id: components["schemas"]["Id"];
            categoryId: components["schemas"]["Id"];
            name: string;
            serviceKind: components["schemas"]["ServiceKind"];
            description: string;
            status: components["schemas"]["CatalogStatus"];
        };
        SkillDTO: {
            name: string;
            description: string;
        };
        SkillVO: {
            id: components["schemas"]["Id"];
            name: string;
            description: string;
        };
        /** @description 价格必须大于 0；支持优惠时 0 < 最低价 < 标准价，报价以最低价为锚点步进 5 元。不支持优惠时最低价等于标准价。维修不开放优惠。上下架和价格变更仅影响后续订单，历史订单保留快照。 */
        SkuDTO: {
            itemId: components["schemas"]["Id"];
            name: string;
            standardPrice: components["schemas"]["Money"];
            minimumOfferPrice: components["schemas"]["Money"];
            durationMinutes: number;
            unit: string;
            skillIds: components["schemas"]["Id"][];
            status: components["schemas"]["CatalogStatus"];
            supportsOffer: boolean;
            description: string;
            included: string;
            excluded: string;
            customerSuppliesParts: boolean;
            /**
             * @description 显式绑定的客户端入口；同一入口最多绑定一个 SKU（含已下架 SKU），null 为不绑定。更新时省略保持已有绑定；解除须显式 null。日常套餐时长须匹配入口，清洁入口只能绑定 CLEANING，维修入口只能绑定 REPAIR。删除/下架后入口不可预约，不自动回退其他 SKU。
             * @enum {string|null}
             */
            clientEntryCode?: "DAILY_2H" | "DAILY_3H" | "DAILY_4H" | "DEEP_60" | "DEEP_100" | "TOILET_UNBLOCK" | "TOILET_VALVE" | "TAP_REPAIR" | "TAP_REPLACE" | "BULB_REPLACE" | "LIGHT_REPLACE" | "FUSE_REPLACE" | "AC_CLEAN" | null;
        };
        SkuVO: {
            id: components["schemas"]["Id"];
            categoryId: components["schemas"]["Id"];
            categoryName: string;
            itemName: string;
            itemId: components["schemas"]["Id"];
            name: string;
            standardPrice: components["schemas"]["Money"];
            minimumOfferPrice: components["schemas"]["Money"];
            durationMinutes: number;
            unit: string;
            skillIds: components["schemas"]["Id"][];
            status: components["schemas"]["CatalogStatus"];
            supportsOffer: boolean;
            description: string;
            included: string;
            excluded: string;
            customerSuppliesParts: boolean;
            /**
             * @description 显式绑定的客户端入口；同一入口最多绑定一个 SKU（含已下架 SKU），null 为不绑定。更新时省略保持已有绑定；解除须显式 null。日常套餐时长须匹配入口，清洁入口只能绑定 CLEANING，维修入口只能绑定 REPAIR。删除/下架后入口不可预约，不自动回退其他 SKU。
             * @enum {string|null}
             */
            clientEntryCode?: "DAILY_2H" | "DAILY_3H" | "DAILY_4H" | "DEEP_60" | "DEEP_100" | "TOILET_UNBLOCK" | "TOILET_VALVE" | "TAP_REPAIR" | "TAP_REPLACE" | "BULB_REPLACE" | "LIGHT_REPLACE" | "FUSE_REPLACE" | "AC_CLEAN" | null;
        };
        CatalogQuery: {
            /** @default 1 */
            pageNo: number;
            /** @default 20 */
            pageSize: number;
            keyword?: string;
            categoryId?: components["schemas"]["Id"];
            itemId?: components["schemas"]["Id"];
            status?: components["schemas"]["CatalogStatus"];
        };
        WorkerDTO: {
            displayName: string;
            phone: string;
            /** @enum {string} */
            cityCode: "440100";
            skillIds: components["schemas"]["Id"][];
            dispatchEnabled: boolean;
        };
        WorkerCreateDTO: {
            /** @example customer */
            username: string;
            /**
             * Format: password
             * @example Demo12345
             */
            password: string;
            displayName: string;
            phone: string;
            /** @enum {string} */
            cityCode: "440100";
            skillIds: components["schemas"]["Id"][];
            dispatchEnabled: boolean;
        };
        WorkerVO: {
            id: components["schemas"]["Id"];
            accountId: components["schemas"]["Id"];
            username: string;
            status: components["schemas"]["AccountStatus"];
            displayName: string;
            phone: string;
            /** @enum {string} */
            cityCode: "440100";
            skillIds: components["schemas"]["Id"][];
            dispatchEnabled: boolean;
        };
        WorkerQuery: {
            /** @default 1 */
            pageNo: number;
            /** @default 20 */
            pageSize: number;
            keyword?: string;
            skillId?: components["schemas"]["Id"];
            dispatchEnabled?: boolean;
        };
        /** @description 08:00 <= start < end <= 22:00，区间不可重叠，允许相邻。 */
        WorkIntervalDTO: {
            start: components["schemas"]["LocalTime"];
            end: components["schemas"]["LocalTime"];
        };
        /**
         * @example {
         *       "intervals": [
         *         {
         *           "start": "08:00",
         *           "end": "12:00"
         *         },
         *         {
         *           "start": "13:00",
         *           "end": "22:00"
         *         }
         *       ],
         *       "restWeekdays": [
         *         7
         *       ]
         *     }
         */
        ScheduleDTO: {
            intervals: components["schemas"]["WorkIntervalDTO"][];
            restWeekdays: number[];
        };
        ScheduleVO: {
            configured: boolean;
            intervals: components["schemas"]["WorkIntervalDTO"][];
            restWeekdays: number[];
        };
        /** @description 30 分钟对齐、start < end、至少提前 2 小时且位于未来 30 天槽窗口；不得覆盖 SERVICE/BUFFER。 */
        LeaveDTO: {
            startTime: components["schemas"]["DateTime"];
            endTime: components["schemas"]["DateTime"];
            reason: string;
        };
        LeaveVO: {
            id: components["schemas"]["Id"];
            startTime: components["schemas"]["DateTime"];
            endTime: components["schemas"]["DateTime"];
            reason: string;
            status: components["schemas"]["LeaveStatus"];
        };
        /** @description 只有 SERVICE/BUFFER 包含 bookingType、assignmentId、orderId。每天 08:00—22:00，未来 30 天，不包含窗口末日。 */
        SlotVO: {
            startTime: components["schemas"]["DateTime"];
            endTime: components["schemas"]["DateTime"];
            status: components["schemas"]["SlotStatus"];
            bookingType?: components["schemas"]["BookingType"];
            assignmentId?: components["schemas"]["Id"];
            orderId?: components["schemas"]["Id"];
        };
        SlotQuery: {
            date: components["schemas"]["LocalDate"];
        };
        BookingRulesVO: {
            cityCode: string;
            workStart: components["schemas"]["LocalTime"];
            workEnd: components["schemas"]["LocalTime"];
            slotMinutes: number;
            earliestHours: number;
            latestDays: number;
            offerLeadHours: number;
            offerWaitMinutes: number;
            offerSafetyHours: number;
            paymentTimeoutMinutes: number;
            dispatchWaitMinutes: number;
            dispatchScanSeconds: number;
            standardBufferMinutes: number;
            offerBufferMinutes: number;
            autoConfirmHours: number;
            /**
             * @description 人民币元，两位小数字符串。服务端 BigDecimal/decimal；前端比较和计算使用整数分。
             * @example 128.00
             * @enum {string}
             */
            priceStep: "5.00";
        };
        /** @description 当前仅预约窗口可配置，其他核心规则只读。只影响新预约，已创建订单的截止时间不改变。 */
        SettingsDTO: {
            earliestHours: number;
            latestDays: number;
        };
        /**
         * @description OFFER 必须提供 offerPrice；STANDARD 不提供。数量固定 1。开始时间按半小时对齐，至少提前 earliestHours、最多 latestDays。OFFER 需 SKU 支持且至少提前 12 小时。服务及尾部缓冲均在 08:00—22:00 内。 contactName/contactPhone 必须同时提供或同时省略，省略时使用地址联系人。图片至多3张，不影响价格和服务范围，不保证有人接单。
         * @example {
         *       "skuId": "301",
         *       "addressId": "401",
         *       "bookingType": "OFFER",
         *       "startTime": "2026-10-04T09:00:00+08:00",
         *       "offerPrice": "130.00",
         *       "remark": "请提前联系",
         *       "contactName": "家人联系人",
         *       "contactPhone": "13800000009",
         *       "sceneImageIds": [
         *         "701"
         *       ]
         *     }
         */
        CreateOrderDTO: {
            skuId: components["schemas"]["Id"];
            addressId: components["schemas"]["Id"];
            bookingType: components["schemas"]["BookingType"];
            startTime: components["schemas"]["DateTime"];
            offerPrice?: components["schemas"]["Money"];
            remark?: string;
            /** @description 本次订单联系人；省略则取所选地址联系人，仅写订单快照。 */
            contactName?: string;
            /** @description 本次订单联系电话；与 contactName 同时提供或同时省略。仅写订单快照。 */
            contactPhone?: string;
            /** @description 可选，默认空。必须均属于当前客户且上传成功；在创建订单事务中校验并保存不可变引用，不因地址/套餐修改变化。 */
            sceneImageIds?: components["schemas"]["Id"][];
        };
        ServiceSnapshotVO: {
            categoryName: string;
            itemName: string;
            skuName: string;
            standardPrice: components["schemas"]["Money"];
            minimumOfferPrice: components["schemas"]["Money"];
            durationMinutes: number;
            unit: string;
            skillIds: components["schemas"]["Id"][];
            description: string;
            included: string;
            excluded: string;
            customerSuppliesParts: boolean;
        };
        /** @description 客户仅自己的订单；人员仅分配给自己的订单；管理员可查看全部。未发生的可选时间/人员/成交字段省略，不传 null。已支付订单禁止修改 SKU、地址和预约时间。开始码单独返回，不泄漏给人员。 contactName/contactPhone 为本次履约使用的订单联系人，address 内联系人为创建时地址簿快照，两者职责独立。sceneImages 为客户自愿提交的现场图片，附件不可替换或删除历史关联。 */
        OrderVO: {
            id: components["schemas"]["Id"];
            customerId: components["schemas"]["Id"];
            skuId: components["schemas"]["Id"];
            bookingType: components["schemas"]["BookingType"];
            status: components["schemas"]["OrderStatus"];
            paymentStatus: components["schemas"]["PaymentStatus"];
            dispatchStatus: components["schemas"]["DispatchStatus"];
            service: components["schemas"]["ServiceSnapshotVO"];
            address: components["schemas"]["AddressDTO"];
            startTime: components["schemas"]["DateTime"];
            endTime: components["schemas"]["DateTime"];
            bufferEndTime: components["schemas"]["DateTime"];
            createdAt: components["schemas"]["DateTime"];
            paymentDeadline: components["schemas"]["DateTime"];
            offerDeadline?: components["schemas"]["DateTime"];
            dispatchDeadline?: components["schemas"]["DateTime"];
            confirmationDeadline?: components["schemas"]["DateTime"];
            currentPrice: components["schemas"]["Money"];
            dealPrice?: components["schemas"]["Money"];
            priceVersion: number;
            workerId?: components["schemas"]["Id"];
            workerName?: string;
            cancellationReason?: string;
            remark: string;
            reviewed: boolean;
            sceneImages: components["schemas"]["SceneImageVO"][];
            /** @description 本次订单联系人；省略则取所选地址联系人，仅写订单快照。 */
            contactName: string;
            /** @description 本次订单联系电话；与 contactName 同时提供或同时省略。仅写订单快照。 */
            contactPhone: string;
        };
        /** @description 日期按预约开始时间在 Asia/Shanghai 的自然日闭区间筛选；from 不得晚于 to。keyword 搜索订单 ID 或服务名称。 statuses 以逗号分隔传输，可筛选多个精确状态；与 status 互斥。仅用于页面浏览分组，不改变正式状态。 */
        OrderQuery: {
            /** @default 1 */
            pageNo: number;
            /** @default 20 */
            pageSize: number;
            keyword?: string;
            status?: components["schemas"]["OrderStatus"];
            bookingType?: components["schemas"]["BookingType"];
            from?: components["schemas"]["LocalDate"];
            to?: components["schemas"]["LocalDate"];
            statuses?: components["schemas"]["OrderStatus"][];
        };
        OfferQuery: {
            /** @default 1 */
            pageNo: number;
            /** @default 20 */
            pageSize: number;
            keyword?: string;
            from?: components["schemas"]["LocalDate"];
            to?: components["schemas"]["LocalDate"];
        };
        /** @description 抢单前最小信息集，无客户 ID、门牌、电话、联系人或开始码。仅符合条件的人员可见。 sceneImages 仅允许当前符合接单资格的人员通过鉴权接口查看。提示客户避免拍入隐私；不得在抢单池暴露结构化门牌、联系方式或开始码。 */
        OfferVO: {
            id: components["schemas"]["Id"];
            skuName: string;
            districtName: string;
            cityCode: string;
            durationMinutes: number;
            startTime: components["schemas"]["DateTime"];
            endTime: components["schemas"]["DateTime"];
            bufferEndTime: components["schemas"]["DateTime"];
            currentPrice: components["schemas"]["Money"];
            priceVersion: number;
            offerDeadline: components["schemas"]["DateTime"];
            description: string;
            included: string;
            excluded: string;
            customerSuppliesParts: boolean;
            sceneImages: components["schemas"]["SceneImageVO"][];
        };
        /**
         * @description 只有 WAITING_ACCEPTANCE 且未截止可调价。newPrice 与旧价相差非零 5 元整数倍且在订单快照范围内；涨价需 confirmSimulatedPayment=true。涨价补差或降价退款、报价历史、版本递增在同一事务提交，失败全部回滚。
         * @example {
         *       "newPrice": "135.00",
         *       "expectedPrice": "130.00",
         *       "priceVersion": 1,
         *       "confirmSimulatedPayment": true
         *     }
         */
        ChangeOfferDTO: {
            newPrice: components["schemas"]["Money"];
            expectedPrice: components["schemas"]["Money"];
            priceVersion: number;
            confirmSimulatedPayment: boolean;
        };
        /**
         * @example {
         *       "expectedPrice": "135.00",
         *       "priceVersion": 2
         *     }
         */
        ClaimOfferDTO: {
            expectedPrice: components["schemas"]["Money"];
            priceVersion: number;
        };
        CancelOrderDTO: {
            reason: string;
        };
        StartServiceDTO: {
            startCode: string;
        };
        StartCodeVO: {
            /** @example 123456 */
            startCode: string;
        };
        ReviewDTO: {
            score: number;
            tags: components["schemas"]["ReviewTag"][];
            content: string;
        };
        ReviewVO: {
            id: components["schemas"]["Id"];
            orderId: components["schemas"]["Id"];
            createdAt: components["schemas"]["DateTime"];
            score: number;
            tags: components["schemas"]["ReviewTag"][];
            content: string;
        };
        /** @description 所有金额为正；流水不可修改或删除。模拟支付同步成功或整体失败，不存在真实渠道回调。净收款为 PAYMENT+TOP_UP-PARTIAL_REFUND-FULL_REFUND。 */
        PaymentVO: {
            id: components["schemas"]["Id"];
            orderId: components["schemas"]["Id"];
            businessNo: string;
            type: components["schemas"]["PaymentType"];
            amount: components["schemas"]["Money"];
            createdAt: components["schemas"]["DateTime"];
        };
        PriceHistoryVO: {
            id: components["schemas"]["Id"];
            orderId: components["schemas"]["Id"];
            previousPrice: components["schemas"]["Money"];
            newPrice: components["schemas"]["Money"];
            priceVersion: number;
            createdAt: components["schemas"]["DateTime"];
        };
        AssignmentVO: {
            id: components["schemas"]["Id"];
            orderId: components["schemas"]["Id"];
            workerId: components["schemas"]["Id"];
            workerName: string;
            bookingType: components["schemas"]["BookingType"];
            status: components["schemas"]["AssignmentStatus"];
            assignedAt: components["schemas"]["DateTime"];
        };
        DispatchAttemptVO: {
            id: components["schemas"]["Id"];
            orderId: components["schemas"]["Id"];
            workerId?: components["schemas"]["Id"];
            result: components["schemas"]["DispatchAttemptResult"];
            reason: string;
            serviceMinutes?: number;
            orderCount?: number;
            createdAt: components["schemas"]["DateTime"];
        };
        OrderHistoryVO: {
            payments: components["schemas"]["PaymentVO"][];
            priceHistory: components["schemas"]["PriceHistoryVO"][];
            assignments: components["schemas"]["AssignmentVO"][];
            review?: components["schemas"]["ReviewVO"];
        };
        /** @description USER操作必须包含actorId；SYSTEM定时任务省略actorId。退款处理包含订单ID、退款类型与金额，不能伪造系统的用户身份。 */
        AuditVO: {
            id: components["schemas"]["Id"];
            actorType: components["schemas"]["ActorType"];
            actorId?: components["schemas"]["Id"];
            action: string;
            targetId: components["schemas"]["Id"];
            detail: string;
            createdAt: components["schemas"]["DateTime"];
        };
        RecordQuery: {
            /** @default 1 */
            pageNo: number;
            /** @default 20 */
            pageSize: number;
            orderId?: components["schemas"]["Id"];
            keyword?: string;
        };
        MutationVO: {
            /** @enum {boolean} */
            success: true;
        };
        /** @description PRICE_CHANGED 必须携带 currentPrice 和 priceVersion；客户端展示变化并重新查询，不能自动重试抢单或调价。 */
        ErrorDetailsVO: {
            fieldErrors?: {
                field: string;
                message: string;
            }[];
            currentPrice?: components["schemas"]["Money"];
            priceVersion?: number;
            currentStatus?: components["schemas"]["OrderStatus"];
        };
        ErrorResponse: {
            code: components["schemas"]["ErrorCode"];
            message: string;
            data: components["schemas"]["ErrorDetailsVO"];
        };
        /** @description 浏览器原生 WebSocket 不能设置 Authorization 请求头。连接同源 /ws 后 5 秒内发首帧 AUTH，服务端在校验前不发送业务数据；不得把 JWT 放 URL。认证失败或过期以 4401 关闭，账号禁用/角色非法以 4403 关闭。 */
        WsAuthFrame: {
            /** @enum {string} */
            type: "AUTH";
            accessToken: string;
        };
        WsAuthAck: {
            /** @enum {string} */
            type: "AUTHENTICATED";
            occurredAt: components["schemas"]["DateTime"];
        };
        /** @description 无门牌、电话、开始码。仅事件相关且有权限的用户收到；优惠池事件需按人员资格过滤。reason 用于关闭或失败。 */
        WsOrderPayload: {
            status: components["schemas"]["OrderStatus"];
            currentPrice: components["schemas"]["Money"];
            reason?: string;
        };
        /**
         * @description 同源 /ws，仅通知，事务提交后发送。OFFER_CREATED=新优惠单；OFFER_PRICE_CHANGED=变价；ORDER_CLAIMED=被抢；ORDER_CLOSED=取消/超时；DISPATCH_SUCCEEDED/FAILED=标准调度结果；ORDER_STATUS_CHANGED=履约变化。客户收本人订单；人员收本人分配及有资格的池事件；管理员收管理事件。允许丢失/重复/乱序，eventId 去重，priceVersion 不得回退；断线、重连、重新登录必须 HTTP 重查，不以推送作为状态裁决。
         * @example {
         *       "eventId": "9001",
         *       "type": "OFFER_PRICE_CHANGED",
         *       "orderId": "10001",
         *       "priceVersion": 2,
         *       "occurredAt": "2026-10-03T10:00:00+08:00",
         *       "payload": {
         *         "status": "WAITING_ACCEPTANCE",
         *         "currentPrice": "135.00"
         *       }
         *     }
         */
        WsEvent: {
            eventId: components["schemas"]["Id"];
            type: components["schemas"]["WsEventType"];
            orderId: components["schemas"]["Id"];
            priceVersion: number;
            occurredAt: components["schemas"]["DateTime"];
            payload: components["schemas"]["WsOrderPayload"];
        };
        LoginVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["LoginVO"];
        };
        AccountVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["AccountVO"];
        };
        RegionListResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["RegionVO"][];
        };
        BookingRulesVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["BookingRulesVO"];
        };
        CategoryPageDTO: {
            list: components["schemas"]["CategoryVO"][];
            total: number;
            pages: number;
        };
        CategoryPageDTOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["CategoryPageDTO"];
        };
        ServiceItemPageDTO: {
            list: components["schemas"]["ServiceItemVO"][];
            total: number;
            pages: number;
        };
        ServiceItemPageDTOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["ServiceItemPageDTO"];
        };
        SkuPageDTO: {
            list: components["schemas"]["SkuVO"][];
            total: number;
            pages: number;
        };
        /**
         * @example {
         *       "code": "SUCCESS",
         *       "message": "成功",
         *       "data": {
         *         "list": [],
         *         "total": 0,
         *         "pages": 0
         *       }
         *     }
         */
        SkuPageDTOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["SkuPageDTO"];
        };
        SkuVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["SkuVO"];
        };
        AddressListResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["AddressVO"][];
        };
        AddressVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["AddressVO"];
        };
        MutationVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["MutationVO"];
        };
        OrderVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["OrderVO"];
        };
        OrderPageDTO: {
            list: components["schemas"]["OrderVO"][];
            total: number;
            pages: number;
        };
        OrderPageDTOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["OrderPageDTO"];
        };
        OrderHistoryVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["OrderHistoryVO"];
        };
        StartCodeVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["StartCodeVO"];
        };
        ReviewVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["ReviewVO"];
        };
        WorkerVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["WorkerVO"];
        };
        ScheduleVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["ScheduleVO"];
        };
        SlotListResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["SlotVO"][];
        };
        LeavePageDTO: {
            list: components["schemas"]["LeaveVO"][];
            total: number;
            pages: number;
        };
        LeavePageDTOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["LeavePageDTO"];
        };
        LeaveVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["LeaveVO"];
        };
        OfferPageDTO: {
            list: components["schemas"]["OfferVO"][];
            total: number;
            pages: number;
        };
        OfferPageDTOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["OfferPageDTO"];
        };
        CategoryVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["CategoryVO"];
        };
        ServiceItemVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["ServiceItemVO"];
        };
        SkillPageDTO: {
            list: components["schemas"]["SkillVO"][];
            total: number;
            pages: number;
        };
        SkillPageDTOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["SkillPageDTO"];
        };
        SkillVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["SkillVO"];
        };
        AccountPageDTO: {
            list: components["schemas"]["AccountVO"][];
            total: number;
            pages: number;
        };
        AccountPageDTOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["AccountPageDTO"];
        };
        WorkerPageDTO: {
            list: components["schemas"]["WorkerVO"][];
            total: number;
            pages: number;
        };
        WorkerPageDTOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["WorkerPageDTO"];
        };
        DispatchAttemptPageDTO: {
            list: components["schemas"]["DispatchAttemptVO"][];
            total: number;
            pages: number;
        };
        DispatchAttemptPageDTOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["DispatchAttemptPageDTO"];
        };
        PaymentPageDTO: {
            list: components["schemas"]["PaymentVO"][];
            total: number;
            pages: number;
        };
        PaymentPageDTOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["PaymentPageDTO"];
        };
        AuditPageDTO: {
            list: components["schemas"]["AuditVO"][];
            total: number;
            pages: number;
        };
        AuditPageDTOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["AuditPageDTO"];
        };
        /**
         * @description 固定客户端入口标识，不能从分类名称、列表顺序或 SKU 名称推断。增加后台分类不会自动增加客户端入口。
         * @enum {string}
         */
        ClientEntryCode: "DAILY_2H" | "DAILY_3H" | "DAILY_4H" | "DEEP_60" | "DEEP_100" | "TOILET_UNBLOCK" | "TOILET_VALVE" | "TAP_REPAIR" | "TAP_REPLACE" | "BULB_REPLACE" | "LIGHT_REPLACE" | "FUSE_REPLACE" | "AC_CLEAN";
        /** @description 始终返回全部固定入口。available=true 时提供 sku；缺失、未绑定、下架或业务性质/时长不符时 available=false，仅提供不可预约原因。价格和能力来自关联的同一个正式 SKU。 */
        ClientEntryVO: {
            code: components["schemas"]["ClientEntryCode"];
            available: boolean;
            sku?: components["schemas"]["SkuVO"];
            unavailableReason?: string;
        };
        ClientEntryListResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["ClientEntryVO"][];
        };
        /** @description 现场图片不可变引用元数据，无公开 URL、客户身份或原始文件名。通过当前角色的鉴权内容接口读取；订单保存这些引用快照。 */
        SceneImageVO: {
            id: components["schemas"]["Id"];
            /** @enum {string} */
            mimeType: "image/jpeg" | "image/png" | "image/webp";
            size: number;
            createdAt: components["schemas"]["DateTime"];
        };
        SceneImageUploadDTO: {
            /**
             * Format: binary
             * @description 单张 JPEG/PNG/WebP，1 字节至 5 MiB。服务端验证真实图片内容和 MIME，移除 EXIF 等元数据，不接受 SVG 或其他可执行内容。
             */
            file: string;
        };
        SceneImageVOResponse: {
            /** @enum {string} */
            code: "SUCCESS";
            message: string;
            data: components["schemas"]["SceneImageVO"];
        };
        /** @description 客户读取本人上传图片可省略 orderId；人员和管理员必须指定包含该图片的订单 ID，每次读取重新校验访问资格。 */
        SceneImageQuery: {
            orderId?: components["schemas"]["Id"];
        };
    };
    responses: {
        /** @description 请求格式或参数无效 */
        Error400: {
            headers: {
                [name: string]: unknown;
            };
            content: {
                "application/json": components["schemas"]["ErrorResponse"];
            };
        };
        /** @description 未登录、凭证无效或令牌到期 */
        Error401: {
            headers: {
                [name: string]: unknown;
            };
            content: {
                "application/json": components["schemas"]["ErrorResponse"];
            };
        };
        /** @description 角色或资源权限不足、账号禁用 */
        Error403: {
            headers: {
                [name: string]: unknown;
            };
            content: {
                "application/json": components["schemas"]["ErrorResponse"];
            };
        };
        /** @description 资源不存在或不属于当前访问范围 */
        Error404: {
            headers: {
                [name: string]: unknown;
            };
            content: {
                "application/json": components["schemas"]["ErrorResponse"];
            };
        };
        /** @description 状态、价格版本、时间槽或幂等冲突 */
        Error409: {
            headers: {
                [name: string]: unknown;
            };
            content: {
                "application/json": components["schemas"]["ErrorResponse"];
            };
        };
        /** @description 业务规则不满足 */
        Error422: {
            headers: {
                [name: string]: unknown;
            };
            content: {
                "application/json": components["schemas"]["ErrorResponse"];
            };
        };
        /** @description 内部错误；事务已回滚 */
        Error500: {
            headers: {
                [name: string]: unknown;
            };
            content: {
                "application/json": components["schemas"]["ErrorResponse"];
            };
        };
        /** @description 文件超过大小限制 */
        Error413: {
            headers: {
                [name: string]: unknown;
            };
            content: {
                "application/json": components["schemas"]["ErrorResponse"];
            };
        };
        /** @description 不支持的图片类型或内容 */
        Error415: {
            headers: {
                [name: string]: unknown;
            };
            content: {
                "application/json": components["schemas"]["ErrorResponse"];
            };
        };
    };
    parameters: {
        /**
         * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
         * @example request_20261003_0001
         */
        IdempotencyKey: string;
    };
    requestBodies: never;
    headers: never;
    pathItems: never;
}
export type $defs = Record<string, never>;
export interface operations {
    customerLogin: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["LoginDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["LoginVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    customerCurrentAccount: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["AccountVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    workerLogin: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["LoginDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["LoginVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    workerCurrentAccount: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["AccountVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    adminLogin: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["LoginDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["LoginVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    adminCurrentAccount: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["AccountVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    customerRegister: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["RegisterDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            201: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["AccountVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listServiceRegions: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["RegionListResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    getBookingRules: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["BookingRulesVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listCustomerCategory: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
                keyword?: string;
                categoryId?: components["schemas"]["Id"];
                itemId?: components["schemas"]["Id"];
                status?: components["schemas"]["CatalogStatus"];
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["CategoryPageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listCustomerServiceItem: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
                keyword?: string;
                categoryId?: components["schemas"]["Id"];
                itemId?: components["schemas"]["Id"];
                status?: components["schemas"]["CatalogStatus"];
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["ServiceItemPageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listCustomerSku: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
                keyword?: string;
                categoryId?: components["schemas"]["Id"];
                itemId?: components["schemas"]["Id"];
                status?: components["schemas"]["CatalogStatus"];
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["SkuPageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    getCustomerSku: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["SkuVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listAddresses: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["AddressListResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    createAddress: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["AddressDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            201: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["AddressVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    updateAddress: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["AddressDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["AddressVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    deleteAddress: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["MutationVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    customerListOrders: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
                keyword?: string;
                status?: components["schemas"]["OrderStatus"];
                bookingType?: components["schemas"]["BookingType"];
                from?: components["schemas"]["LocalDate"];
                to?: components["schemas"]["LocalDate"];
                statuses?: components["schemas"]["OrderStatus"][];
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderPageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    createOrder: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["CreateOrderDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            201: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    customerGetOrder: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    customerGetOrderHistory: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderHistoryVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    workerListOrders: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
                keyword?: string;
                status?: components["schemas"]["OrderStatus"];
                bookingType?: components["schemas"]["BookingType"];
                from?: components["schemas"]["LocalDate"];
                to?: components["schemas"]["LocalDate"];
                statuses?: components["schemas"]["OrderStatus"][];
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderPageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    workerGetOrder: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    workerGetOrderHistory: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderHistoryVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    adminListOrders: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
                keyword?: string;
                status?: components["schemas"]["OrderStatus"];
                bookingType?: components["schemas"]["BookingType"];
                from?: components["schemas"]["LocalDate"];
                to?: components["schemas"]["LocalDate"];
                statuses?: components["schemas"]["OrderStatus"][];
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderPageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    adminGetOrder: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    adminGetOrderHistory: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderHistoryVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    payOrder: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    changeOffer: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["ChangeOfferDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    cancelCustomerOrder: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["CancelOrderDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    getStartCode: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["StartCodeVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    confirmOrder: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    createReview: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["ReviewDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            201: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["ReviewVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    getWorkerProfile: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["WorkerVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    getSchedule: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["ScheduleVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    updateSchedule: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["ScheduleDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["ScheduleVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listWorkerSlots: {
        parameters: {
            query: {
                date: components["schemas"]["LocalDate"];
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["SlotListResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listLeaves: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["LeavePageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    createLeave: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["LeaveDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            201: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["LeaveVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    cancelLeave: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["MutationVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listEligibleOffers: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
                keyword?: string;
                from?: components["schemas"]["LocalDate"];
                to?: components["schemas"]["LocalDate"];
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OfferPageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    claimOffer: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["ClaimOfferDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    departOrder: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    arriveOrder: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    startOrder: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["StartServiceDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    finishOrder: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listAdminCategory: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
                keyword?: string;
                categoryId?: components["schemas"]["Id"];
                itemId?: components["schemas"]["Id"];
                status?: components["schemas"]["CatalogStatus"];
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["CategoryPageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    createAdminCategory: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["CategoryDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            201: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["CategoryVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    updateAdminCategory: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["CategoryDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["CategoryVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    deleteAdminCategory: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["MutationVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listAdminServiceItem: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
                keyword?: string;
                categoryId?: components["schemas"]["Id"];
                itemId?: components["schemas"]["Id"];
                status?: components["schemas"]["CatalogStatus"];
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["ServiceItemPageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    createAdminServiceItem: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["ServiceItemDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            201: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["ServiceItemVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    updateAdminServiceItem: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["ServiceItemDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["ServiceItemVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    deleteAdminServiceItem: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["MutationVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listAdminSku: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
                keyword?: string;
                categoryId?: components["schemas"]["Id"];
                itemId?: components["schemas"]["Id"];
                status?: components["schemas"]["CatalogStatus"];
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["SkuPageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    createAdminSku: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["SkuDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            201: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["SkuVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    updateAdminSku: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["SkuDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["SkuVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    deleteAdminSku: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["MutationVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listAdminSkill: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
                keyword?: string;
                categoryId?: components["schemas"]["Id"];
                itemId?: components["schemas"]["Id"];
                status?: components["schemas"]["CatalogStatus"];
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["SkillPageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    createAdminSkill: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["SkillDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            201: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["SkillVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    updateAdminSkill: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["SkillDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["SkillVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    deleteAdminSkill: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["MutationVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listAccounts: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
                keyword?: string;
                role?: components["schemas"]["Role"];
                status?: components["schemas"]["AccountStatus"];
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["AccountPageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    setAccountStatus: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["AccountStatusDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["AccountVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    updateAccountProfile: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["AccountProfileDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["AccountVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listWorkers: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
                keyword?: string;
                skillId?: components["schemas"]["Id"];
                dispatchEnabled?: boolean;
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["WorkerPageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    createWorker: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["WorkerCreateDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            201: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["WorkerVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    updateWorker: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["WorkerDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["WorkerVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    getAdminWorkerSlots: {
        parameters: {
            query: {
                date: components["schemas"]["LocalDate"];
            };
            header?: never;
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["SlotListResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    cancelAdminOrder: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["CancelOrderDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["OrderVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listDispatchAttempts: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
                orderId?: components["schemas"]["Id"];
                keyword?: string;
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["DispatchAttemptPageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listPayments: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
                orderId?: components["schemas"]["Id"];
                keyword?: string;
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["PaymentPageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listAudits: {
        parameters: {
            query?: {
                pageNo?: number;
                pageSize?: number;
                orderId?: components["schemas"]["Id"];
                keyword?: string;
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["AuditPageDTOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    getSettings: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["BookingRulesVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    updateSettings: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["SettingsDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["BookingRulesVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    listClientEntries: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["ClientEntryListResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    updateCustomerProfile: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["AccountProfileDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["AccountVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    uploadSceneImage: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "multipart/form-data": components["schemas"]["SceneImageUploadDTO"];
            };
        };
        responses: {
            /** @description 成功 */
            201: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["SceneImageVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            413: components["responses"]["Error413"];
            415: components["responses"]["Error415"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    deleteSceneImage: {
        parameters: {
            query?: never;
            header: {
                /**
                 * @description 以账号+方法+路径+Key为作用域。相同载荷重放首次成功状态码/响应且不重复执行；不同载荷返回409 IDEMPOTENCY_CONFLICT；执行中返回409 REQUEST_IN_PROGRESS。至少保留24小时；不可逆业务还需永久业务唯一约束兜底。失败不缓存，重试原操作沿用Key，变更载荷生成新Key。
                 * @example request_20261003_0001
                 */
                "Idempotency-Key": components["parameters"]["IdempotencyKey"];
            };
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 成功 */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json": components["schemas"]["MutationVOResponse"];
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    customerGetSceneImage: {
        parameters: {
            query?: {
                orderId?: components["schemas"]["Id"];
            };
            header?: never;
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 授权图片内容（无 JSON 包裹） */
            200: {
                headers: {
                    "Cache-Control"?: "private, no-store";
                    "X-Content-Type-Options"?: "nosniff";
                    [name: string]: unknown;
                };
                content: {
                    "image/jpeg": string;
                    "image/png": string;
                    "image/webp": string;
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    workerGetSceneImage: {
        parameters: {
            query: {
                orderId: components["schemas"]["Id"];
            };
            header?: never;
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 授权图片内容（无 JSON 包裹） */
            200: {
                headers: {
                    "Cache-Control"?: "private, no-store";
                    "X-Content-Type-Options"?: "nosniff";
                    [name: string]: unknown;
                };
                content: {
                    "image/jpeg": string;
                    "image/png": string;
                    "image/webp": string;
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
    adminGetSceneImage: {
        parameters: {
            query: {
                orderId: components["schemas"]["Id"];
            };
            header?: never;
            path: {
                id: components["schemas"]["Id"];
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description 授权图片内容（无 JSON 包裹） */
            200: {
                headers: {
                    "Cache-Control"?: "private, no-store";
                    "X-Content-Type-Options"?: "nosniff";
                    [name: string]: unknown;
                };
                content: {
                    "image/jpeg": string;
                    "image/png": string;
                    "image/webp": string;
                };
            };
            400: components["responses"]["Error400"];
            401: components["responses"]["Error401"];
            403: components["responses"]["Error403"];
            404: components["responses"]["Error404"];
            409: components["responses"]["Error409"];
            422: components["responses"]["Error422"];
            500: components["responses"]["Error500"];
        };
    };
}
