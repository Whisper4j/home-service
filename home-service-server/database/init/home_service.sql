-- 家政预约与调度平台：空库初始化结构（27张表）
-- 目标版本：MySQL 8.0.16及以上；InnoDB；utf8mb4；Asia/Shanghai（+08:00）。
-- 设计说明：docs/database/数据库设计文档.md。
-- 本文件用于空项目数据库的结构初始化。
-- 不含建库、删库、删表或测试数据；不使用IF NOT EXISTS掩盖旧结构差异。
-- 采用逻辑外键，关联有效性与跨表业务一致性由后续Service事务负责。

SET NAMES utf8mb4 COLLATE utf8mb4_0900_ai_ci;
SET SESSION time_zone = '+08:00';

-- 1. auth_account-账号表
CREATE TABLE auth_account (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '认证账号ID，对外按十进制字符串传输',
    username VARCHAR(32) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '登录用户名，区分大小写',
    password_hash VARCHAR(60) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT 'BCrypt哈希，不保存明文密码',
    role VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '角色：CUSTOMER/WORKER/ADMIN',
    status VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL DEFAULT 'ENABLED' COMMENT '账号状态：ENABLED/DISABLED',
    display_name VARCHAR(40) NOT NULL COMMENT '展示称呼',
    phone CHAR(11) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '联系电话',
    protected_account TINYINT NOT NULL DEFAULT 0 COMMENT '是否为禁止禁用的初始化管理员',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（Asia/Shanghai）',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_auth_account_username (username),
    CONSTRAINT ck_auth_account_role CHECK (role IN ('CUSTOMER', 'WORKER', 'ADMIN')),
    CONSTRAINT ck_auth_account_status CHECK (status IN ('ENABLED', 'DISABLED')),
    CONSTRAINT ck_auth_account_phone CHECK (phone REGEXP '^1[0-9]{10}$'),
    CONSTRAINT ck_auth_account_protected CHECK (protected_account IN (0, 1) AND (protected_account = 0 OR role = 'ADMIN'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账号表';

-- 2. administrative_region-行政区表
CREATE TABLE administrative_region (
    code VARCHAR(6) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '国家统计局行政区划代码',
    parent_code VARCHAR(6) CHARACTER SET ascii COLLATE ascii_bin NULL COMMENT '上级行政区代码',
    name VARCHAR(40) NOT NULL COMMENT '行政区名称',
    level VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '级别：PROVINCE/CITY/DISTRICT',
    service_enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否属于当前服务区域',
    sort_no INT NOT NULL DEFAULT 0 COMMENT '展示顺序',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（Asia/Shanghai）',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（Asia/Shanghai）',
    PRIMARY KEY (code),
    CONSTRAINT ck_region_level CHECK (level IN ('PROVINCE', 'CITY', 'DISTRICT')),
    CONSTRAINT ck_region_service_enabled CHECK (service_enabled IN (0, 1)),
    CONSTRAINT ck_region_parent CHECK ((level = 'PROVINCE' AND parent_code IS NULL) OR (level IN ('CITY', 'DISTRICT') AND parent_code IS NOT NULL))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='行政区表';

-- 3. worker_profile-服务人员表
CREATE TABLE worker_profile (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '服务人员业务ID，与认证账号ID相互独立',
    account_id BIGINT NOT NULL COMMENT '认证账号ID',
    city_code VARCHAR(6) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '服务城市代码',
    dispatch_enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否允许新的派单和抢单',
    work_intervals JSON NULL COMMENT '每日工作区间；未设置排班时为空',
    rest_weekdays JSON NULL COMMENT '每周休息日；1周一至7周日，未设置时为空',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（Asia/Shanghai）',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_worker_profile_account (account_id),
    CONSTRAINT ck_worker_dispatch_enabled CHECK (dispatch_enabled IN (0, 1)),
    CONSTRAINT ck_worker_schedule_json CHECK ((work_intervals IS NULL AND rest_weekdays IS NULL) OR (work_intervals IS NOT NULL AND rest_weekdays IS NOT NULL AND JSON_TYPE(work_intervals) = 'ARRAY' AND JSON_LENGTH(work_intervals) BETWEEN 1 AND 14 AND JSON_TYPE(rest_weekdays) = 'ARRAY' AND JSON_LENGTH(rest_weekdays) <= 7))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='服务人员表';

-- 4. customer_address-客户地址表
CREATE TABLE customer_address (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '客户地址ID',
    customer_id BIGINT NOT NULL COMMENT '客户业务ID',
    contact_name VARCHAR(40) NOT NULL COMMENT '地址簿联系人',
    contact_phone CHAR(11) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '地址簿联系电话',
    province_code VARCHAR(6) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '省代码',
    province_name VARCHAR(40) NOT NULL COMMENT '省名称快照',
    city_code VARCHAR(6) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '市代码',
    city_name VARCHAR(40) NOT NULL COMMENT '市名称快照',
    district_code VARCHAR(6) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '区代码',
    district_name VARCHAR(40) NOT NULL COMMENT '区名称快照',
    detail VARCHAR(200) NOT NULL COMMENT '详细地址',
    longitude DECIMAL(10,7) NULL COMMENT '系统解析经度，未解析时为空',
    latitude DECIMAL(9,7) NULL COMMENT '系统解析纬度，未解析时为空',
    is_default TINYINT NOT NULL DEFAULT 0 COMMENT '是否为默认地址',
    deleted_at DATETIME NULL COMMENT '逻辑删除时间；历史订单使用独立快照',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（Asia/Shanghai）',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    CONSTRAINT ck_customer_address_phone CHECK (contact_phone REGEXP '^1[0-9]{10}$'),
    CONSTRAINT ck_customer_address_default CHECK (is_default IN (0, 1)),
    CONSTRAINT ck_customer_address_coordinates CHECK ((longitude IS NULL AND latitude IS NULL) OR (longitude IS NOT NULL AND latitude IS NOT NULL AND longitude BETWEEN -180 AND 180 AND latitude BETWEEN -90 AND 90))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客户地址表';

-- 5. service_category-服务分类表
CREATE TABLE service_category (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '服务分类ID',
    name VARCHAR(60) NOT NULL COMMENT '分类名称',
    sort_no INT NOT NULL DEFAULT 0 COMMENT '展示顺序',
    status VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL DEFAULT 'OFF_SHELF' COMMENT '状态：ON_SHELF/OFF_SHELF',
    deleted_at DATETIME NULL COMMENT '逻辑删除时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（Asia/Shanghai）',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    CONSTRAINT ck_service_category_status CHECK (status IN ('ON_SHELF', 'OFF_SHELF')),
    CONSTRAINT ck_service_category_sort CHECK (sort_no BETWEEN 0 AND 9999)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='服务分类表';

-- 6. service_item-服务项目表
CREATE TABLE service_item (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '服务项目ID',
    category_id BIGINT NOT NULL COMMENT '服务分类ID',
    name VARCHAR(60) NOT NULL COMMENT '项目名称',
    service_kind VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '服务类型：CLEANING/REPAIR/OTHER',
    description VARCHAR(1000) NOT NULL COMMENT '项目说明',
    status VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL DEFAULT 'OFF_SHELF' COMMENT '状态：ON_SHELF/OFF_SHELF',
    deleted_at DATETIME NULL COMMENT '逻辑删除时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（Asia/Shanghai）',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    CONSTRAINT ck_service_item_kind CHECK (service_kind IN ('CLEANING', 'REPAIR', 'OTHER')),
    CONSTRAINT ck_service_item_status CHECK (status IN ('ON_SHELF', 'OFF_SHELF'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='服务项目表';

-- 7. service_skill-技能表
CREATE TABLE service_skill (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '技能ID',
    name VARCHAR(60) NOT NULL COMMENT '技能名称',
    description VARCHAR(300) NOT NULL COMMENT '技能说明',
    deleted_at DATETIME NULL COMMENT '逻辑删除时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（Asia/Shanghai）',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（Asia/Shanghai）',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='技能表';

-- 8. client_entry-服务入口表
CREATE TABLE client_entry (
    code VARCHAR(64) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '客户端稳定入口编码',
    service_kind VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '服务类型：CLEANING/REPAIR/OTHER',
    group_code VARCHAR(64) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '入口分组编码',
    group_name VARCHAR(80) NOT NULL COMMENT '分组名称',
    group_description VARCHAR(1000) NOT NULL DEFAULT '' COMMENT '分组说明',
    group_sort INT NOT NULL DEFAULT 0 COMMENT '分组排序',
    name VARCHAR(80) NOT NULL COMMENT '入口名称',
    description VARCHAR(2000) NOT NULL DEFAULT '' COMMENT '入口说明',
    sort_no INT NOT NULL DEFAULT 0 COMMENT '组内排序',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT '入口是否启用展示',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（Asia/Shanghai）',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（Asia/Shanghai）',
    PRIMARY KEY (code),
    CONSTRAINT ck_client_entry_code CHECK (code REGEXP '^[A-Z][A-Z0-9_]{0,63}$'),
    CONSTRAINT ck_client_entry_group_code CHECK (group_code REGEXP '^[A-Z][A-Z0-9_]{0,63}$'),
    CONSTRAINT ck_client_entry_kind CHECK (service_kind IN ('CLEANING', 'REPAIR', 'OTHER')),
    CONSTRAINT ck_client_entry_enabled CHECK (enabled IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='服务入口表';

-- 9. service_sku-服务规格表
CREATE TABLE service_sku (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '服务规格SKU ID',
    item_id BIGINT NOT NULL COMMENT '服务项目ID',
    name VARCHAR(80) NOT NULL COMMENT '规格名称',
    standard_price DECIMAL(11,2) NOT NULL COMMENT '标准价，范围与OpenAPI Money一致',
    minimum_offer_price DECIMAL(11,2) NOT NULL COMMENT '最低优惠报价；不支持优惠时等于标准价',
    duration_minutes SMALLINT UNSIGNED NOT NULL COMMENT '预计服务分钟数，30分钟整数倍',
    unit VARCHAR(20) NOT NULL COMMENT '计价单位展示文案',
    status VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL DEFAULT 'OFF_SHELF' COMMENT '状态：ON_SHELF/OFF_SHELF',
    supports_offer TINYINT NOT NULL DEFAULT 0 COMMENT '是否支持优惠预约',
    description VARCHAR(2000) NOT NULL COMMENT '服务说明',
    included VARCHAR(1000) NOT NULL COMMENT '包含内容',
    excluded VARCHAR(1000) NOT NULL COMMENT '不包含内容',
    customer_supplies_parts TINYINT NOT NULL DEFAULT 0 COMMENT '是否由客户自备配件',
    client_entry_code VARCHAR(64) CHARACTER SET ascii COLLATE ascii_bin NULL COMMENT '绑定的客户端入口编码',
    deleted_at DATETIME NULL COMMENT '逻辑删除时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（Asia/Shanghai）',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_service_sku_client_entry (client_entry_code),
    CONSTRAINT ck_service_sku_money CHECK (standard_price > 0 AND minimum_offer_price > 0 AND minimum_offer_price <= standard_price),
    CONSTRAINT ck_service_sku_duration CHECK (duration_minutes BETWEEN 30 AND 720 AND MOD(duration_minutes, 30) = 0),
    CONSTRAINT ck_service_sku_status CHECK (status IN ('ON_SHELF', 'OFF_SHELF')),
    CONSTRAINT ck_service_sku_offer CHECK (supports_offer IN (0, 1) AND ((supports_offer = 1 AND minimum_offer_price < standard_price) OR (supports_offer = 0 AND minimum_offer_price = standard_price))),
    CONSTRAINT ck_service_sku_parts CHECK (customer_supplies_parts IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='服务规格表';

-- 10. service_sku_skill-规格技能表
CREATE TABLE service_sku_skill (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联记录ID',
    sku_id BIGINT NOT NULL COMMENT '业务取值：SKU ID',
    skill_id BIGINT NOT NULL COMMENT '所需技能ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_service_sku_skill_pair (sku_id, skill_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='规格技能表';

-- 11. worker_skill-人员技能表
CREATE TABLE worker_skill (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联记录ID',
    worker_id BIGINT NOT NULL COMMENT '服务人员业务ID',
    skill_id BIGINT NOT NULL COMMENT '技能ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '授予时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_worker_skill_pair (worker_id, skill_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='人员技能表';

-- 12. worker_leave-请假表
CREATE TABLE worker_leave (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '请假ID',
    worker_id BIGINT NOT NULL COMMENT '服务人员业务ID',
    start_time DATETIME NOT NULL COMMENT '请假开始时间（Asia/Shanghai）',
    end_time DATETIME NOT NULL COMMENT '请假结束时间（Asia/Shanghai）',
    reason VARCHAR(300) NOT NULL COMMENT '请假原因',
    status VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE/CANCELLED',
    cancelled_at DATETIME NULL COMMENT '撤销时间（Asia/Shanghai）',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（Asia/Shanghai）',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    CONSTRAINT ck_worker_leave_status CHECK (status IN ('ACTIVE', 'CANCELLED')),
    CONSTRAINT ck_worker_leave_range CHECK (start_time < end_time),
    CONSTRAINT ck_worker_leave_granularity CHECK (MINUTE(start_time) IN (0, 30) AND SECOND(start_time) = 0 AND MINUTE(end_time) IN (0, 30) AND SECOND(end_time) = 0),
    CONSTRAINT ck_worker_leave_cancelled CHECK ((status = 'ACTIVE' AND cancelled_at IS NULL) OR (status = 'CANCELLED' AND cancelled_at IS NOT NULL))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='请假表';

-- 13. service_order-订单表
CREATE TABLE service_order (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID，对外按十进制字符串传输',
    customer_id BIGINT NOT NULL COMMENT '下单客户业务ID',
    sku_id BIGINT NOT NULL COMMENT '下单SKU ID；每单一个SKU',
    address_id BIGINT NOT NULL COMMENT '来源地址簿ID；展示使用独立快照',
    booking_type VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '预约类型：STANDARD/OFFER',
    status VARCHAR(32) CHARACTER SET ascii COLLATE ascii_bin NOT NULL DEFAULT 'PENDING_PAYMENT' COMMENT '订单履约主状态',
    payment_status VARCHAR(24) CHARACTER SET ascii COLLATE ascii_bin NOT NULL DEFAULT 'UNPAID' COMMENT '支付状态：UNPAID/PAID/PARTIALLY_REFUNDED/REFUNDED',
    dispatch_status VARCHAR(24) CHARACTER SET ascii COLLATE ascii_bin NOT NULL DEFAULT 'NOT_REQUIRED' COMMENT '派单状态：NOT_REQUIRED/PENDING/SUCCEEDED/FAILED',
    start_time DATETIME NOT NULL COMMENT '预约开始时间（Asia/Shanghai）',
    end_time DATETIME NOT NULL COMMENT '预约服务结束时间（Asia/Shanghai）',
    buffer_end_time DATETIME NOT NULL COMMENT '尾部缓冲结束时间（Asia/Shanghai）',
    payment_deadline DATETIME NOT NULL COMMENT '支付截止时间（Asia/Shanghai）',
    offer_deadline DATETIME NULL COMMENT '优惠抢单截止时间（Asia/Shanghai）',
    dispatch_deadline DATETIME NULL COMMENT '标准派单截止时间（Asia/Shanghai）',
    confirmation_deadline DATETIME NULL COMMENT '客户确认截止时间（Asia/Shanghai）',
    offer_published_at DATETIME NULL COMMENT '优惠订单首次正式进入抢单池时间',
    next_dispatch_at DATETIME NULL COMMENT '标准派单下次可重试时间；非待派单时为空',
    dispatch_attempt_count INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '已执行标准派单轮数',
    dispatch_failure_reason VARCHAR(300) NULL COMMENT '标准派单最终失败原因',
    current_price DECIMAL(11,2) NOT NULL COMMENT '当前客户报价或标准价',
    deal_price DECIMAL(11,2) NULL COMMENT '人员落实后的成交价',
    price_version INT NOT NULL DEFAULT 1 COMMENT '报价版本，从1开始',
    state_version INT NOT NULL DEFAULT 1 COMMENT '通用条件更新版本',
    contact_name VARCHAR(40) NOT NULL COMMENT '本次订单联系人，不随资料或地址簿变化',
    contact_phone CHAR(11) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '本次订单联系电话',
    start_code CHAR(6) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '六位开始码，仅客户鉴权接口可返回',
    remark VARCHAR(300) NOT NULL DEFAULT '' COMMENT '客户备注',
    cancellation_reason VARCHAR(300) NULL COMMENT '取消原因',
    closed_at DATETIME NULL COMMENT '完成确认或取消的实际时间，用于统计和历史排序',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（Asia/Shanghai）',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    CONSTRAINT ck_order_booking_type CHECK (booking_type IN ('STANDARD', 'OFFER')),
    CONSTRAINT ck_order_status CHECK (status IN ('PENDING_PAYMENT', 'WAITING_DISPATCH', 'WAITING_ACCEPTANCE', 'PENDING_SERVICE', 'DEPARTED', 'ARRIVED', 'IN_SERVICE', 'PENDING_CONFIRMATION', 'COMPLETED', 'CANCELLED')),
    CONSTRAINT ck_order_payment_status CHECK (payment_status IN ('UNPAID', 'PAID', 'PARTIALLY_REFUNDED', 'REFUNDED')),
    CONSTRAINT ck_order_dispatch_status CHECK (dispatch_status IN ('NOT_REQUIRED', 'PENDING', 'SUCCEEDED', 'FAILED')),
    CONSTRAINT ck_order_time_range CHECK (start_time < end_time AND end_time < buffer_end_time),
    CONSTRAINT ck_order_time_granularity CHECK (MINUTE(start_time) IN (0, 30) AND SECOND(start_time) = 0 AND MINUTE(end_time) IN (0, 30) AND SECOND(end_time) = 0),
    CONSTRAINT ck_order_price CHECK (current_price > 0 AND (deal_price IS NULL OR deal_price > 0) AND price_version >= 1 AND state_version >= 1),
    CONSTRAINT ck_order_contact_phone CHECK (contact_phone REGEXP '^1[0-9]{10}$'),
    CONSTRAINT ck_order_start_code CHECK (start_code REGEXP '^[0-9]{6}$'),
    CONSTRAINT ck_order_closed CHECK ((status IN ('COMPLETED', 'CANCELLED') AND closed_at IS NOT NULL) OR (status NOT IN ('COMPLETED', 'CANCELLED') AND closed_at IS NULL)),
    CONSTRAINT ck_order_cancellation CHECK ((status = 'CANCELLED' AND cancellation_reason IS NOT NULL) OR (status <> 'CANCELLED' AND cancellation_reason IS NULL))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';

-- 14. order_detail-订单详情表
CREATE TABLE order_detail (
    order_id BIGINT NOT NULL COMMENT '订单ID，一对一',
    category_id BIGINT NOT NULL COMMENT '创建时分类ID',
    item_id BIGINT NOT NULL COMMENT '创建时项目ID',
    category_name VARCHAR(60) NOT NULL COMMENT '分类名称快照',
    item_name VARCHAR(60) NOT NULL COMMENT '项目名称快照',
    sku_name VARCHAR(80) NOT NULL COMMENT 'SKU名称快照',
    service_kind VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '业务性质快照',
    standard_price DECIMAL(11,2) NOT NULL COMMENT '标准价快照',
    minimum_offer_price DECIMAL(11,2) NOT NULL COMMENT '最低优惠报价快照',
    duration_minutes SMALLINT UNSIGNED NOT NULL COMMENT '预计时长快照',
    unit VARCHAR(20) NOT NULL COMMENT '计价单位快照',
    description VARCHAR(2000) NOT NULL COMMENT '服务说明快照',
    included VARCHAR(1000) NOT NULL COMMENT '包含内容快照',
    excluded VARCHAR(1000) NOT NULL COMMENT '不包含内容快照',
    customer_supplies_parts TINYINT NOT NULL COMMENT '客户自备配件标记快照',
    address_contact_name VARCHAR(40) NOT NULL COMMENT '创建时地址簿联系人快照',
    address_contact_phone CHAR(11) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '创建时地址簿电话快照',
    province_code VARCHAR(6) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '省代码快照',
    province_name VARCHAR(40) NOT NULL COMMENT '省名称快照',
    city_code VARCHAR(6) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '市代码快照',
    city_name VARCHAR(40) NOT NULL COMMENT '市名称快照',
    district_code VARCHAR(6) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '区代码快照',
    district_name VARCHAR(40) NOT NULL COMMENT '区名称快照',
    detail VARCHAR(200) NOT NULL COMMENT '详细地址快照',
    longitude DECIMAL(10,7) NULL COMMENT '经度快照',
    latitude DECIMAL(9,7) NULL COMMENT '纬度快照',
    was_default TINYINT NOT NULL COMMENT '创建时是否为默认地址',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '快照创建时间（Asia/Shanghai）',
    PRIMARY KEY (order_id),
    CONSTRAINT ck_order_detail_kind CHECK (service_kind IN ('CLEANING', 'REPAIR', 'OTHER')),
    CONSTRAINT ck_order_detail_money CHECK (standard_price > 0 AND minimum_offer_price > 0 AND minimum_offer_price <= standard_price),
    CONSTRAINT ck_order_detail_duration CHECK (duration_minutes BETWEEN 30 AND 720 AND MOD(duration_minutes, 30) = 0),
    CONSTRAINT ck_order_detail_parts CHECK (customer_supplies_parts IN (0, 1)),
    CONSTRAINT ck_order_detail_phone CHECK (address_contact_phone REGEXP '^1[0-9]{10}$'),
    CONSTRAINT ck_order_detail_default CHECK (was_default IN (0, 1)),
    CONSTRAINT ck_order_detail_coordinates CHECK ((longitude IS NULL AND latitude IS NULL) OR (longitude IS NOT NULL AND latitude IS NOT NULL AND longitude BETWEEN -180 AND 180 AND latitude BETWEEN -90 AND 90))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单详情表';

-- 15. order_required_skill-订单技能表
CREATE TABLE order_required_skill (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联记录ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    skill_id BIGINT NOT NULL COMMENT '创建时技能ID',
    skill_name VARCHAR(60) NOT NULL COMMENT '技能名称快照',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '快照创建时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_required_skill_pair (order_id, skill_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单技能表';

-- 16. scene_image-现场图片表
CREATE TABLE scene_image (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '现场图片ID',
    customer_id BIGINT NOT NULL COMMENT '上传客户业务ID',
    storage_key VARCHAR(255) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '内部对象存储定位，不是公开URL',
    content_sha256 CHAR(64) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '清理元数据后文件内容SHA-256',
    mime_type VARCHAR(32) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT 'image/jpeg、image/png或image/webp',
    size_bytes INT UNSIGNED NOT NULL COMMENT '文件字节数',
    width_px INT UNSIGNED NOT NULL COMMENT '解码宽度像素',
    height_px INT UNSIGNED NOT NULL COMMENT '解码高度像素',
    deleted_at DATETIME NULL COMMENT '未关联临时图片的逻辑删除时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_scene_image_storage_key (storage_key),
    CONSTRAINT ck_scene_image_sha256 CHECK (content_sha256 REGEXP '^[0-9a-f]{64}$'),
    CONSTRAINT ck_scene_image_mime CHECK (mime_type IN ('image/jpeg', 'image/png', 'image/webp')),
    CONSTRAINT ck_scene_image_size CHECK (size_bytes BETWEEN 1 AND 5242880),
    CONSTRAINT ck_scene_image_pixels CHECK (width_px > 0 AND height_px > 0 AND CAST(width_px AS DECIMAL(20,0)) * CAST(height_px AS DECIMAL(20,0)) <= 40000000)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='现场图片表';

-- 17. order_scene_image-订单图片表
CREATE TABLE order_scene_image (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联记录ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    image_id BIGINT NOT NULL COMMENT '现场图片ID',
    sort_no TINYINT UNSIGNED NOT NULL COMMENT '订单内顺序1至3',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_scene_image_pair (order_id, image_id),
    CONSTRAINT ck_order_scene_image_sort CHECK (sort_no BETWEEN 1 AND 3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单图片表';

-- 18. payment_transaction-支付流水表
CREATE TABLE payment_transaction (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '模拟资金流水ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    business_no VARCHAR(80) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '幂等业务号，全局唯一',
    type VARCHAR(24) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '流水类型：PAYMENT/TOP_UP/PARTIAL_REFUND/FULL_REFUND',
    amount DECIMAL(11,2) NOT NULL COMMENT '正金额；方向由流水类型决定',
    related_price_version INT NULL COMMENT '补差或调价退款对应报价版本',
    remark VARCHAR(300) NOT NULL DEFAULT '' COMMENT '流水说明',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_payment_business_no (business_no),
    CONSTRAINT ck_payment_type CHECK (type IN ('PAYMENT', 'TOP_UP', 'PARTIAL_REFUND', 'FULL_REFUND')),
    CONSTRAINT ck_payment_amount CHECK (amount > 0),
    CONSTRAINT ck_payment_price_version CHECK (related_price_version IS NULL OR related_price_version >= 1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付流水表';

-- 19. order_price_history-报价记录表
CREATE TABLE order_price_history (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '报价变化历史ID',
    order_id BIGINT NOT NULL COMMENT '优惠订单ID',
    previous_price DECIMAL(11,2) NOT NULL COMMENT '变更前价格',
    new_price DECIMAL(11,2) NOT NULL COMMENT '变更后价格',
    price_version INT NOT NULL COMMENT '变更后的报价版本',
    operator_account_id BIGINT NOT NULL COMMENT '发起调价的客户账号ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '变更时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_price_version (order_id, price_version),
    CONSTRAINT ck_order_price_history_money CHECK (previous_price > 0 AND new_price > 0 AND previous_price <> new_price),
    CONSTRAINT ck_order_price_history_version CHECK (price_version >= 2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='报价记录表';

-- 20. dispatch_attempt-派单记录表
CREATE TABLE dispatch_attempt (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '调度尝试ID',
    order_id BIGINT NOT NULL COMMENT '订单ID，便于管理端按单查询',
    worker_id BIGINT NULL COMMENT '候选服务人员；无候选时为空',
    result VARCHAR(24) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '派单结果：ASSIGNED/INELIGIBLE/SLOT_CONFLICT/NO_CANDIDATE',
    reason VARCHAR(300) NOT NULL COMMENT '筛选或尝试结果原因',
    service_minutes INT UNSIGNED NULL COMMENT '候选排序时当日已分配服务分钟数快照',
    order_count INT UNSIGNED NULL COMMENT '候选排序时当日订单数快照',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '尝试时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    CONSTRAINT ck_dispatch_attempt_result CHECK (result IN ('ASSIGNED', 'INELIGIBLE', 'SLOT_CONFLICT', 'NO_CANDIDATE')),
    CONSTRAINT ck_dispatch_attempt_candidate CHECK ((result = 'NO_CANDIDATE' AND worker_id IS NULL) OR (result <> 'NO_CANDIDATE' AND worker_id IS NOT NULL))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='派单记录表';

-- 21. order_assignment-分配记录表
CREATE TABLE order_assignment (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单分配记录ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    worker_id BIGINT NOT NULL COMMENT '服务人员业务ID',
    worker_name VARCHAR(40) NOT NULL COMMENT '分配时人员名称快照',
    booking_type VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT 'STANDARD/OFFER快照',
    status VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE/RELEASED/FINISHED',
    assigned_at DATETIME NOT NULL COMMENT '生效分配时间（Asia/Shanghai）',
    released_at DATETIME NULL COMMENT '取消释放时间（Asia/Shanghai）',
    release_reason VARCHAR(300) NULL COMMENT '释放原因',
    finished_at DATETIME NULL COMMENT '订单完成确认时间（Asia/Shanghai）',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（Asia/Shanghai）',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_assignment_order (order_id),
    CONSTRAINT ck_order_assignment_booking_type CHECK (booking_type IN ('STANDARD', 'OFFER')),
    CONSTRAINT ck_order_assignment_status CHECK (status IN ('ACTIVE', 'RELEASED', 'FINISHED')),
    CONSTRAINT ck_order_assignment_lifecycle CHECK ((status = 'ACTIVE' AND released_at IS NULL AND release_reason IS NULL AND finished_at IS NULL) OR (status = 'RELEASED' AND released_at IS NOT NULL AND release_reason IS NOT NULL AND finished_at IS NULL) OR (status = 'FINISHED' AND released_at IS NULL AND release_reason IS NULL AND finished_at IS NOT NULL))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='分配记录表';

-- 22. worker_time_slot-人员时间槽表
CREATE TABLE worker_time_slot (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '半小时时间槽ID',
    worker_id BIGINT NOT NULL COMMENT '服务人员业务ID',
    slot_start DATETIME NOT NULL COMMENT '槽开始时间（Asia/Shanghai），半小时对齐',
    status VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '状态：NON_WORKING/AVAILABLE/LEAVE/SERVICE/BUFFER',
    booking_type VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NULL COMMENT '占用类型STANDARD/OFFER，仅SERVICE/BUFFER存在',
    assignment_id BIGINT NULL COMMENT '占用分配记录ID，仅SERVICE/BUFFER存在',
    order_id BIGINT NULL COMMENT '占用订单ID，仅SERVICE/BUFFER存在',
    leave_id BIGINT NULL COMMENT '请假ID，仅LEAVE存在',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（Asia/Shanghai）',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_worker_time_slot_start (worker_id, slot_start),
    CONSTRAINT ck_worker_time_slot_granularity CHECK (MINUTE(slot_start) IN (0, 30) AND SECOND(slot_start) = 0),
    CONSTRAINT ck_worker_time_slot_status CHECK (status IN ('NON_WORKING', 'AVAILABLE', 'LEAVE', 'SERVICE', 'BUFFER')),
    CONSTRAINT ck_worker_time_slot_occupancy CHECK ((status IN ('SERVICE', 'BUFFER') AND booking_type IS NOT NULL AND booking_type IN ('STANDARD', 'OFFER') AND assignment_id IS NOT NULL AND order_id IS NOT NULL AND leave_id IS NULL) OR (status = 'LEAVE' AND booking_type IS NULL AND assignment_id IS NULL AND order_id IS NULL AND leave_id IS NOT NULL) OR (status IN ('NON_WORKING', 'AVAILABLE') AND booking_type IS NULL AND assignment_id IS NULL AND order_id IS NULL AND leave_id IS NULL))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='人员时间槽表';

-- 23. order_status_history-订单状态记录表
CREATE TABLE order_status_history (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单状态变化历史ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    from_status VARCHAR(32) CHARACTER SET ascii COLLATE ascii_bin NULL COMMENT '来源状态；创建记录为空',
    to_status VARCHAR(32) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '目标状态',
    actor_type VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '操作者类型：USER/SYSTEM',
    actor_account_id BIGINT NULL COMMENT '用户操作者账号ID；系统任务为空',
    actor_role VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NULL COMMENT '用户操作者角色；系统任务为空',
    reason VARCHAR(300) NULL COMMENT '状态变化原因',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    CONSTRAINT ck_order_status_history_from CHECK (from_status IS NULL OR from_status IN ('PENDING_PAYMENT', 'WAITING_DISPATCH', 'WAITING_ACCEPTANCE', 'PENDING_SERVICE', 'DEPARTED', 'ARRIVED', 'IN_SERVICE', 'PENDING_CONFIRMATION', 'COMPLETED', 'CANCELLED')),
    CONSTRAINT ck_order_status_history_to CHECK (to_status IN ('PENDING_PAYMENT', 'WAITING_DISPATCH', 'WAITING_ACCEPTANCE', 'PENDING_SERVICE', 'DEPARTED', 'ARRIVED', 'IN_SERVICE', 'PENDING_CONFIRMATION', 'COMPLETED', 'CANCELLED')),
    CONSTRAINT ck_order_status_history_actor_type CHECK (actor_type IN ('USER', 'SYSTEM')),
    CONSTRAINT ck_order_status_history_actor CHECK ((actor_type = 'USER' AND actor_account_id IS NOT NULL AND actor_role IS NOT NULL AND actor_role IN ('CUSTOMER', 'WORKER', 'ADMIN')) OR (actor_type = 'SYSTEM' AND actor_account_id IS NULL AND actor_role IS NULL))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单状态记录表';

-- 24. service_review-评价表
CREATE TABLE service_review (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '评价ID',
    order_id BIGINT NOT NULL COMMENT '已完成订单ID，每单最多一次',
    customer_id BIGINT NOT NULL COMMENT '评价客户业务ID',
    worker_id BIGINT NOT NULL COMMENT '被评价服务人员业务ID',
    score TINYINT UNSIGNED NOT NULL COMMENT '评分1至5',
    content VARCHAR(500) NOT NULL DEFAULT '' COMMENT '评价内容',
    tags JSON NOT NULL COMMENT '评价标签数组；无标签时保存空数组',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_service_review_order (order_id),
    CONSTRAINT ck_service_review_score CHECK (score BETWEEN 1 AND 5),
    CONSTRAINT ck_service_review_tags CHECK (JSON_TYPE(tags) = 'ARRAY' AND JSON_LENGTH(tags) <= 3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评价表';

-- 25. platform_setting-平台设置表
CREATE TABLE platform_setting (
    id TINYINT UNSIGNED NOT NULL COMMENT '固定单例ID=1',
    city_code VARCHAR(6) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '当前服务城市代码',
    work_start TIME NOT NULL DEFAULT '08:00:00' COMMENT '平台工作开始时间',
    work_end TIME NOT NULL DEFAULT '22:00:00' COMMENT '平台工作结束时间',
    slot_minutes SMALLINT UNSIGNED NOT NULL DEFAULT 30 COMMENT '时间槽分钟数',
    earliest_hours TINYINT UNSIGNED NOT NULL DEFAULT 2 COMMENT '最早预约提前小时数，可维护',
    latest_days TINYINT UNSIGNED NOT NULL DEFAULT 7 COMMENT '最远预约天数，可维护',
    offer_lead_hours TINYINT UNSIGNED NOT NULL DEFAULT 12 COMMENT '优惠预约最少提前小时数',
    offer_wait_minutes SMALLINT UNSIGNED NOT NULL DEFAULT 120 COMMENT '优惠最长等待分钟数',
    offer_safety_hours TINYINT UNSIGNED NOT NULL DEFAULT 6 COMMENT '服务前安全截止小时数',
    payment_timeout_minutes SMALLINT UNSIGNED NOT NULL DEFAULT 15 COMMENT '待支付超时分钟数',
    dispatch_wait_minutes SMALLINT UNSIGNED NOT NULL DEFAULT 5 COMMENT '标准派单最长等待分钟数',
    dispatch_scan_seconds SMALLINT UNSIGNED NOT NULL DEFAULT 30 COMMENT '派单扫描间隔秒数',
    standard_buffer_minutes SMALLINT UNSIGNED NOT NULL DEFAULT 120 COMMENT '标准订单尾部缓冲分钟数',
    offer_buffer_minutes SMALLINT UNSIGNED NOT NULL DEFAULT 60 COMMENT '优惠订单尾部缓冲分钟数',
    auto_confirm_hours TINYINT UNSIGNED NOT NULL DEFAULT 24 COMMENT '自动完成确认小时数',
    price_step DECIMAL(11,2) NOT NULL DEFAULT 5.00 COMMENT '优惠报价步长',
    schedule_window_days TINYINT UNSIGNED NOT NULL DEFAULT 30 COMMENT '时间槽滚动窗口天数',
    leave_lead_hours TINYINT UNSIGNED NOT NULL DEFAULT 2 COMMENT '请假最少提前小时数',
    scene_image_max_count TINYINT UNSIGNED NOT NULL DEFAULT 3 COMMENT '每单现场图片上限',
    scene_image_max_bytes INT UNSIGNED NOT NULL DEFAULT 5242880 COMMENT '单图字节上限',
    scene_image_mime_types JSON NOT NULL COMMENT '允许上传的图片MIME数组',
    updated_by BIGINT NULL COMMENT '最后修改管理员账号ID；初始化时为空',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（Asia/Shanghai）',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    CONSTRAINT ck_platform_setting_singleton CHECK (id = 1),
    CONSTRAINT ck_platform_setting_window CHECK (earliest_hours BETWEEN 2 AND 24 AND latest_days BETWEEN 1 AND 7 AND earliest_hours < latest_days * 24),
    CONSTRAINT ck_platform_setting_mime CHECK (JSON_TYPE(scene_image_mime_types) = 'ARRAY' AND JSON_LENGTH(scene_image_mime_types) BETWEEN 1 AND 3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='平台设置表';

-- 26. audit_log-操作日志表
CREATE TABLE audit_log (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '审计日志ID',
    actor_type VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '操作者类型：USER/SYSTEM',
    actor_account_id BIGINT NULL COMMENT '用户操作者账号ID；系统任务为空',
    action VARCHAR(100) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '稳定操作代码',
    target_type VARCHAR(24) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '审计目标类型',
    target_id BIGINT NOT NULL COMMENT '目标业务ID',
    order_id BIGINT NULL COMMENT '订单相关操作的明确订单ID',
    related_payment_id BIGINT NULL COMMENT '退款等操作关联的资金流水ID',
    payment_type VARCHAR(24) CHARACTER SET ascii COLLATE ascii_bin NULL COMMENT '退款等操作的流水类型',
    amount DECIMAL(11,2) NULL COMMENT '退款等操作金额',
    detail VARCHAR(2000) NOT NULL COMMENT '脱敏后的人类可读详情',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发生时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    CONSTRAINT ck_audit_actor_type CHECK (actor_type IN ('USER', 'SYSTEM')),
    CONSTRAINT ck_audit_actor CHECK ((actor_type = 'USER' AND actor_account_id IS NOT NULL) OR (actor_type = 'SYSTEM' AND actor_account_id IS NULL)),
    CONSTRAINT ck_audit_target_type CHECK (target_type IN ('ACCOUNT', 'WORKER', 'CATEGORY', 'SERVICE_ITEM', 'SKU', 'SKILL', 'ORDER', 'SETTINGS', 'SCENE_IMAGE')),
    CONSTRAINT ck_audit_payment_type CHECK (payment_type IS NULL OR payment_type IN ('PAYMENT', 'TOP_UP', 'PARTIAL_REFUND', 'FULL_REFUND')),
    CONSTRAINT ck_audit_payment_detail CHECK ((related_payment_id IS NULL AND payment_type IS NULL AND amount IS NULL) OR (related_payment_id IS NOT NULL AND payment_type IS NOT NULL AND amount IS NOT NULL AND amount > 0)),
    CONSTRAINT ck_audit_order_target CHECK (target_type <> 'ORDER' OR (order_id IS NOT NULL AND order_id = target_id))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志表';

-- 27. http_idempotency_record-幂等请求表
CREATE TABLE http_idempotency_record (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'HTTP幂等记录ID',
    scope_type VARCHAR(24) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT 'ACCOUNT或ANONYMOUS_REGISTER',
    account_id BIGINT NULL COMMENT '认证账号作用域的账号ID',
    anonymous_subject_hash CHAR(64) CHARACTER SET ascii COLLATE ascii_bin NULL COMMENT '匿名注册使用规范化用户名SHA-256，不保存密码或完整请求',
    scope_identity VARCHAR(90) CHARACTER SET ascii COLLATE ascii_bin GENERATED ALWAYS AS (CASE WHEN scope_type = 'ACCOUNT' THEN CONCAT('ACCOUNT:', account_id) ELSE CONCAT('ANON_REGISTER:', anonymous_subject_hash) END) STORED COMMENT '统一唯一作用域辅助列',
    http_method VARCHAR(10) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '大写HTTP方法',
    request_path VARCHAR(255) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '不含查询串的规范化接口路径',
    idempotency_key VARCHAR(128) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '业务取值：Idempotency-Key',
    request_fingerprint CHAR(64) CHARACTER SET ascii COLLATE ascii_bin NOT NULL COMMENT '规范化请求指纹；敏感内容使用HMAC-SHA-256，不保存原文',
    execution_status VARCHAR(16) CHARACTER SET ascii COLLATE ascii_bin NOT NULL DEFAULT 'PROCESSING' COMMENT '执行状态：PROCESSING/SUCCEEDED',
    response_http_status SMALLINT UNSIGNED NULL COMMENT '成功响应HTTP状态码',
    response_content_type VARCHAR(64) CHARACTER SET ascii COLLATE ascii_bin NULL COMMENT '成功响应Content-Type',
    response_json JSON NULL COMMENT '成功响应必要快照；不包含密码、令牌或无关敏感字段',
    expires_at DATETIME NOT NULL COMMENT '幂等记录过期时间，至少保留24小时',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始执行时间（Asia/Shanghai）',
    completed_at DATETIME NULL COMMENT '成功完成时间（Asia/Shanghai）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_http_idempotency_scope (scope_identity, http_method, request_path, idempotency_key),
    CONSTRAINT ck_http_idempotency_scope_type CHECK (scope_type IN ('ACCOUNT', 'ANONYMOUS_REGISTER')),
    CONSTRAINT ck_http_idempotency_scope CHECK ((scope_type = 'ACCOUNT' AND account_id IS NOT NULL AND anonymous_subject_hash IS NULL) OR (scope_type = 'ANONYMOUS_REGISTER' AND account_id IS NULL AND anonymous_subject_hash IS NOT NULL AND anonymous_subject_hash REGEXP '^[0-9a-f]{64}$')),
    CONSTRAINT ck_http_idempotency_method CHECK (http_method IN ('POST', 'PUT', 'PATCH', 'DELETE')),
    CONSTRAINT ck_http_idempotency_key CHECK (idempotency_key REGEXP '^[A-Za-z0-9_-]{8,128}$'),
    CONSTRAINT ck_http_idempotency_fingerprint CHECK (request_fingerprint REGEXP '^[0-9a-f]{64}$'),
    CONSTRAINT ck_http_idempotency_status CHECK (execution_status IN ('PROCESSING', 'SUCCEEDED')),
    CONSTRAINT ck_http_idempotency_response CHECK ((execution_status = 'PROCESSING' AND response_http_status IS NULL AND response_content_type IS NULL AND response_json IS NULL AND completed_at IS NULL) OR (execution_status = 'SUCCEEDED' AND response_http_status IS NOT NULL AND response_http_status BETWEEN 200 AND 299 AND response_content_type IS NOT NULL AND response_json IS NOT NULL AND completed_at IS NOT NULL)),
    CONSTRAINT ck_http_idempotency_expiry CHECK (expires_at >= DATE_ADD(created_at, INTERVAL 24 HOUR))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='幂等请求表';
