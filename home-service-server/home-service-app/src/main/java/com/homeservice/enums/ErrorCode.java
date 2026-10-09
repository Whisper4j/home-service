package com.homeservice.enums;

import com.homeservice.common.exception.ErrorType;

import lombok.RequiredArgsConstructor;

/**
 * 错误码枚举类
 * 定义错误码的固定取值
 */
@RequiredArgsConstructor
public enum ErrorCode implements ErrorType {
    VALIDATION_ERROR(400, "请求格式或参数无效"),
    UNAUTHENTICATED(401, "请先登录或重新认证"),
    TOKEN_EXPIRED(401, "登录已过期"),
    INVALID_CREDENTIALS(401, "用户名或密码错误"),
    FORBIDDEN(403, "没有访问权限"),
    ACCOUNT_DISABLED(403, "账号已禁用"),
    NOT_FOUND(404, "资源不存在"),
    IMAGE_NOT_AVAILABLE(404, "图片不可用"),
    USERNAME_EXISTS(409, "当前操作不符合业务约束"),
    IDEMPOTENCY_CONFLICT(409, "当前操作不符合业务约束"),
    REQUEST_IN_PROGRESS(409, "当前操作不符合业务约束"),
    STATE_CONFLICT(409, "当前操作不符合业务约束"),
    PRICE_CHANGED(409, "报价已变化，请重新确认"),
    SLOT_CONFLICT(409, "当前操作不符合业务约束"),
    SCHEDULE_CONFLICT(409, "当前操作不符合业务约束"),
    OFFER_CLOSED(409, "当前操作不符合业务约束"),
    ORDER_TAKEN(409, "当前操作不符合业务约束"),
    REVIEW_EXISTS(409, "当前操作不符合业务约束"),
    RESOURCE_IN_USE(409, "当前操作不符合业务约束"),
    CONFIG_CONFLICT(409, "当前操作不符合业务约束"),
    PRICE_OUT_OF_RANGE(422, "当前操作不符合业务约束"),
    OUTSIDE_SERVICE_AREA(422, "当前操作不符合业务约束"),
    BOOKING_WINDOW_INVALID(422, "当前操作不符合业务约束"),
    OFFER_NOT_SUPPORTED(422, "当前操作不符合业务约束"),
    WORKER_INELIGIBLE(422, "当前操作不符合业务约束"),
    START_CODE_INVALID(422, "当前操作不符合业务约束"),
    CATALOG_UNAVAILABLE(422, "当前操作不符合业务约束"),
    IMAGE_TOO_LARGE(413, "图片超过大小限制"),
    IMAGE_TYPE_UNSUPPORTED(415, "不支持的图片类型"),
    INTERNAL_ERROR(500, "服务暂时不可用");
    private final int status;
    private final String description;

    /**
     * 获取业务错误码
     */
    public String code() {
        return name();
    }

    /**
     * 获取对应的HTTP状态
     */
    public int httpStatus() {
        return status;
    }

    /**
     * 获取业务错误提示
     */
    public String message() {
        return description;
    }
}
