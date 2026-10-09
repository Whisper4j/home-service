package com.homeservice.common.constant;

/**
 * 信息提示常量类。
 *
 * <p>对外提示必须使用本类中的常量；技术细节仅写入服务端日志，不直接返回客户端。
 */
public final class MessageConstant {

    private MessageConstant() {
    }

    // 通用结果与请求
    public static final String SUCCESS = "成功";
    public static final String REQUEST_INVALID = "请求信息有误，请检查后重试";
    public static final String REQUEST_FORMAT_INVALID = "请求格式有误，请检查后重试";
    public static final String REQUEST_INCOMPLETE = "请求信息不完整，请刷新后重试";
    public static final String PARAMETER_INVALID = "填写内容有误，请检查后重试";
    public static final String SERVICE_UNAVAILABLE = "服务暂时不可用，请稍后重试";
    public static final String RESOURCE_NOT_FOUND = "未找到相关内容";
    public static final String METHOD_NOT_SUPPORTED = "当前操作方式不受支持";
    public static final String MEDIA_TYPE_NOT_SUPPORTED = "提交的内容格式不受支持";
    public static final String RESPONSE_MEDIA_TYPE_NOT_SUPPORTED = "暂不支持所请求的响应格式";

    // 分页与筛选
    public static final String PAGE_NO_REQUIRED = "请提供页码";
    public static final String PAGE_NO_INVALID = "页码必须从1开始";
    public static final String PAGE_SIZE_REQUIRED = "请提供每页数量";
    public static final String PAGE_SIZE_INVALID = "每页数量必须在1到100之间";
    public static final String PAGE_COUNT_INVALID = "分页统计信息无效";
    public static final String SEARCH_KEYWORD_TOO_LONG = "搜索内容不能超过100个字符";
    public static final String DATE_RANGE_INVALID = "开始日期不能晚于结束日期";
    public static final String STATUS_FILTER_INVALID = "状态筛选条件有误，请重新选择";

    // 账号与认证
    public static final String USERNAME_REQUIRED = "请输入用户名";
    public static final String USERNAME_TOO_LONG = "用户名不能超过32个字符";
    public static final String USERNAME_INVALID = "用户名须以字母开头，并且只能包含字母、数字和下划线";
    public static final String PASSWORD_REQUIRED = "请输入密码";
    public static final String PASSWORD_LENGTH_INVALID = "密码长度必须在8到72个字符之间";
    public static final String PASSWORD_BYTES_TOO_LONG = "密码内容过长，请减少中文或特殊字符后重试";
    public static final String DISPLAY_NAME_REQUIRED = "请输入称呼或姓名";
    public static final String DISPLAY_NAME_TOO_LONG = "称呼或姓名不能超过40个字符";
    public static final String PHONE_REQUIRED = "请输入联系电话";
    public static final String PHONE_INVALID = "请输入正确的11位手机号";
    public static final String ACCOUNT_STATUS_REQUIRED = "请选择账号状态";
    public static final String DISPATCH_STATUS_REQUIRED = "请选择是否允许新分配";
    public static final String TOKEN_REQUIRED = "登录信息缺失，请重新登录";
    public static final String TOKEN_TOO_LONG = "登录信息无效，请重新登录";
    public static final String AUTH_TYPE_INVALID = "认证信息无效，请重新连接";
    public static final String UNAUTHENTICATED = "请先登录";
    public static final String TOKEN_EXPIRED = "登录已过期，请重新登录";
    public static final String INVALID_CREDENTIALS = "用户名或密码错误";
    public static final String FORBIDDEN = "没有权限执行此操作";
    public static final String ACCOUNT_DISABLED = "账号已停用，请联系管理员";
    public static final String USERNAME_EXISTS = "该用户名已被使用，请更换后重试";

    // 地区、地址与人员
    public static final String CITY_REQUIRED = "请选择服务城市";
    public static final String CITY_INVALID = "所选服务城市无效，请重新选择";
    public static final String SKILL_REQUIRED = "请选择服务技能";
    public static final String SKILL_AT_LEAST_ONE = "请至少选择一项服务技能";
    public static final String SKILL_DUPLICATED = "请勿重复选择服务技能";
    public static final String SKILL_INVALID = "所选服务技能无效，请重新选择";
    public static final String CONTACT_NAME_REQUIRED = "请输入联系人姓名";
    public static final String CONTACT_NAME_TOO_LONG = "联系人姓名不能超过40个字符";
    public static final String CONTACT_PHONE_REQUIRED = "请输入联系人电话";
    public static final String CONTACT_PHONE_INVALID = "请输入正确的11位联系人手机号";
    public static final String ADDRESS_REGION_REQUIRED = "请选择完整的服务区域";
    public static final String ADDRESS_REGION_INVALID = "服务区域信息有误，请重新选择";
    public static final String ADDRESS_DETAIL_REQUIRED = "请输入详细地址";
    public static final String ADDRESS_DETAIL_TOO_LONG = "详细地址不能超过200个字符";
    public static final String ADDRESS_DEFAULT_REQUIRED = "请选择是否设为默认地址";
    public static final String OUTSIDE_SERVICE_AREA = "所选地址暂不在服务范围内";

    // 服务目录
    public static final String NAME_REQUIRED = "请输入名称";
    public static final String NAME_TOO_LONG_60 = "名称不能超过60个字符";
    public static final String NAME_TOO_LONG_80 = "名称不能超过80个字符";
    public static final String DESCRIPTION_REQUIRED = "请输入说明";
    public static final String DESCRIPTION_TOO_LONG_300 = "说明不能超过300个字符";
    public static final String DESCRIPTION_TOO_LONG_1000 = "说明不能超过1000个字符";
    public static final String DESCRIPTION_TOO_LONG_2000 = "说明不能超过2000个字符";
    public static final String INCLUDED_CONTENT_REQUIRED = "请输入服务包含内容";
    public static final String INCLUDED_CONTENT_TOO_LONG = "服务包含内容不能超过1000个字符";
    public static final String EXCLUDED_CONTENT_REQUIRED = "请输入服务不包含内容";
    public static final String EXCLUDED_CONTENT_TOO_LONG = "服务不包含内容不能超过1000个字符";
    public static final String CATALOG_SELECTION_INVALID = "所选服务目录信息无效，请重新选择";
    public static final String SERVICE_TYPE_REQUIRED = "请选择服务类型";
    public static final String CATALOG_STATUS_REQUIRED = "请选择目录状态";
    public static final String SORT_NO_REQUIRED = "请输入排序值";
    public static final String SORT_NO_INVALID = "排序值必须在0到9999之间";
    public static final String CLIENT_ENTRY_INVALID = "所选客户端入口无效，请重新选择";
    public static final String DURATION_REQUIRED = "请输入服务时长";
    public static final String DURATION_RANGE_INVALID = "服务时长必须在30到720分钟之间";
    public static final String DURATION_STEP_INVALID = "服务时长必须是30分钟的整数倍";
    public static final String UNIT_REQUIRED = "请输入计价单位";
    public static final String UNIT_TOO_LONG = "计价单位不能超过20个字符";
    public static final String SUPPORTS_OFFER_REQUIRED = "请选择是否支持优惠预约";
    public static final String CUSTOMER_SUPPLIES_PARTS_REQUIRED = "请选择是否由客户自备配件";
    public static final String CATALOG_UNAVAILABLE = "所选服务当前不可预约，请重新选择";
    public static final String RESOURCE_IN_USE = "当前内容正在使用，暂时无法操作";
    public static final String CONFIG_CONFLICT = "当前配置与已有数据冲突，请检查后重试";

    // 金额、订单与支付
    public static final String STANDARD_PRICE_REQUIRED = "请输入标准价格";
    public static final String MINIMUM_OFFER_PRICE_REQUIRED = "请输入最低优惠价";
    public static final String OFFER_PRICE_REQUIRED = "请输入优惠价";
    public static final String AMOUNT_NEGATIVE = "金额不能为负数";
    public static final String AMOUNT_TOO_LARGE = "输入的金额过大";
    public static final String AMOUNT_SCALE_INVALID = "金额最多保留两位小数";
    public static final String ORDER_SELECTION_INVALID = "所选订单信息无效，请刷新后重试";
    public static final String BOOKING_TYPE_REQUIRED = "请选择预约方式";
    public static final String BOOKING_START_TIME_REQUIRED = "请选择预约开始时间";
    public static final String BOOKING_START_TIME_INVALID = "预约时间必须按半小时选择";
    public static final String OFFER_PRICE_RULE_INVALID = "优惠预约必须填写报价，标准预约无需填写报价";
    public static final String REMARK_TOO_LONG = "备注不能超过300个字符";
    public static final String ORDER_CONTACT_INVALID = "联系人和联系电话必须同时填写";
    public static final String SCENE_IMAGE_TOO_MANY = "现场图片最多选择3张";
    public static final String SCENE_IMAGE_DUPLICATED = "请勿重复选择同一张现场图片";
    public static final String SCENE_IMAGE_INVALID = "现场图片信息无效，请重新上传";
    public static final String OFFER_CONTEXT_EXPIRED = "订单报价信息已失效，请刷新后重新确认";
    public static final String PAYMENT_CONFIRMATION_REQUIRED = "请重新打开报价窗口并确认本次调整";
    public static final String CANCEL_REASON_REQUIRED = "请输入取消原因";
    public static final String CANCEL_REASON_TOO_LONG = "取消原因不能超过300个字符";
    public static final String START_CODE_REQUIRED = "请输入6位服务开始码";
    public static final String START_CODE_INVALID = "服务开始码不正确，请与客户确认后重试";
    public static final String IDEMPOTENCY_CONFLICT = "请勿使用同一个请求标识提交不同内容";
    public static final String REQUEST_IN_PROGRESS = "请求正在处理中，请稍后查看结果";
    public static final String STATE_CONFLICT = "当前状态已发生变化，请刷新后重试";
    public static final String PRICE_CHANGED = "报价已发生变化，请查看最新报价后重新确认";
    public static final String PRICE_OUT_OF_RANGE = "报价不在允许范围内，请重新输入";
    public static final String SLOT_CONFLICT = "所选时间已不可用，请重新选择";
    public static final String OFFER_NOT_SUPPORTED = "所选服务暂不支持优惠预约";
    public static final String OFFER_CLOSED = "该优惠预约已经结束";
    public static final String ORDER_TAKEN = "该订单已被其他服务人员接取";
    public static final String WORKER_INELIGIBLE = "当前条件不满足接单要求";

    // 排班、请假与设置
    public static final String DATE_REQUIRED = "请选择日期";
    public static final String MONTH_REQUIRED = "请选择月份";
    public static final String MONTH_INVALID = "月份格式有误，请重新选择";
    public static final String START_TIME_REQUIRED = "请选择开始时间";
    public static final String END_TIME_REQUIRED = "请选择结束时间";
    public static final String WORK_INTERVAL_REQUIRED = "请至少设置一个工作时段";
    public static final String WORK_INTERVAL_COUNT_INVALID = "每天最多可设置14个工作时段";
    public static final String WORK_INTERVAL_ITEM_INVALID = "工作时段信息不完整，请重新设置";
    public static final String WORK_INTERVAL_INVALID = "工作时段须在08:00至22:00之间，且开始时间早于结束时间";
    public static final String WORK_INTERVAL_OVERLAPPED = "工作时段不能重叠";
    public static final String REST_WEEKDAYS_REQUIRED = "请选择每周休息日；没有休息日时可保持为空";
    public static final String REST_WEEKDAYS_TOO_MANY = "每周休息日不能超过7天";
    public static final String REST_WEEKDAYS_DUPLICATED = "请勿重复选择休息日";
    public static final String REST_WEEKDAY_INVALID = "所选休息日无效，请重新选择";
    public static final String LEAVE_TIME_INVALID = "请假起止时间须按半小时选择，且开始时间早于结束时间";
    public static final String LEAVE_REASON_REQUIRED = "请输入请假原因";
    public static final String LEAVE_REASON_TOO_LONG = "请假原因不能超过300个字符";
    public static final String SCHEDULE_CONFLICT = "排班或请假与现有任务冲突，请调整后重试";
    public static final String EARLIEST_HOURS_REQUIRED = "请输入最早可预约小时数";
    public static final String EARLIEST_HOURS_INVALID = "最早可预约时间必须在2到24小时之间";
    public static final String LATEST_DAYS_REQUIRED = "请输入最远可预约天数";
    public static final String LATEST_DAYS_INVALID = "最远可预约天数必须在1到7天之间";
    public static final String BOOKING_WINDOW_INVALID = "最早可预约时间必须早于最远预约时间";

    // 评价与附件
    public static final String SCORE_REQUIRED = "请选择评分";
    public static final String SCORE_INVALID = "评分必须在1到5分之间";
    public static final String REVIEW_TAGS_REQUIRED = "请选择评价标签；没有合适标签时可保持为空";
    public static final String REVIEW_TAGS_TOO_MANY = "评价标签最多选择3项";
    public static final String REVIEW_TAGS_DUPLICATED = "请勿重复选择评价标签";
    public static final String REVIEW_TAG_INVALID = "所选评价标签无效，请重新选择";
    public static final String REVIEW_CONTENT_REQUIRED = "请填写评价内容";
    public static final String REVIEW_CONTENT_TOO_LONG = "评价内容不能超过500个字符";
    public static final String REVIEW_EXISTS = "该订单已经评价，请勿重复提交";
    public static final String UPLOAD_FILE_REQUIRED = "请选择要上传的图片";
    public static final String IMAGE_TOO_LARGE = "图片超过大小限制，请压缩后重试";
    public static final String IMAGE_TYPE_UNSUPPORTED = "仅支持JPEG、PNG和WebP图片";
    public static final String IMAGE_NOT_AVAILABLE = "图片不可用，请重新上传";

    // 配置与内部保护；不会作为业务细节直接展示给普通用户
    public static final String AUTH_WHITELIST_INVALID = "认证白名单包含未公开的接口";
    public static final String JWT_SECRET_INVALID = "JWT密钥必须是至少32个随机字节的Base64文本";
    public static final String JWT_TTL_INVALID = "JWT有效期必须是大于0秒的整秒时长";
    public static final String WEBSOCKET_ORIGIN_INVALID = "WebSocket Origin必须是明确的HTTP或HTTPS来源";
    public static final String CONFIG_VALUE_REQUIRED = "必需配置不能为空";
    public static final String CONFIG_VALUE_INVALID = "配置值不符合要求";
    public static final String MYSQL_VERSION_UNSUPPORTED = "需要MySQL 8.0.16或更高版本";
    public static final String INTEGER_REQUIRED = "需要整数";
    public static final String BOOLEAN_REQUIRED = "需要true或false";
    public static final String ENUM_VALUE_INVALID = "枚举值无效";
    public static final String ID_FORMAT_INVALID = "ID格式无效";
    public static final String DATE_FORMAT_INVALID = "日期格式无效";
    public static final String DATE_TIME_FORMAT_INVALID = "日期时间格式无效";
    public static final String TIME_FORMAT_INVALID = "时间格式无效";
    public static final String MONEY_FORMAT_INVALID = "金额格式无效";
    public static final String JSON_WRITE_FAILED = "JSON持久化编码失败";
    public static final String JSON_READ_FAILED = "JSON持久化解码失败";
    public static final String JWT_SUBJECT_INVALID = "令牌主体无效";
    public static final String NOTIFICATION_RECIPIENT_INVALID = "通知接收者身份无效";
    public static final String NOTIFICATION_TRANSACTION_REQUIRED = "通知必须在业务事务提交后发送";
    public static final String PAGINATION_TOTAL_INVALID = "分页计数不可为负";
    public static final String PAGE_SIZE_ARGUMENT_INVALID = "每页数量必须为正数";

    // WebSocket 关闭原因
    public static final String WEBSOCKET_AUTH_TIMEOUT = "认证超时";
    public static final String WEBSOCKET_AUTH_INVALID = "认证信息无效";
    public static final String WEBSOCKET_TOKEN_EXPIRED = "登录已过期";
    public static final String WEBSOCKET_PROTOCOL_INVALID = "连接协议无效";
    public static final String WEBSOCKET_SESSION_UNAVAILABLE = "连接已失效";
}
