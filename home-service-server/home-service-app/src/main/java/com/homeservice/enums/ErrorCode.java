package com.homeservice.enums;

import com.homeservice.common.constant.MessageConstant;
import com.homeservice.common.exception.ErrorType;

import lombok.RequiredArgsConstructor;

/**
 * 错误码枚举类
 * 定义错误码的固定取值
 */
@RequiredArgsConstructor
public enum ErrorCode implements ErrorType {
    VALIDATION_ERROR(400, MessageConstant.PARAMETER_INVALID),
    UNAUTHENTICATED(401, MessageConstant.UNAUTHENTICATED),
    TOKEN_EXPIRED(401, MessageConstant.TOKEN_EXPIRED),
    INVALID_CREDENTIALS(401, MessageConstant.INVALID_CREDENTIALS),
    FORBIDDEN(403, MessageConstant.FORBIDDEN),
    ACCOUNT_DISABLED(403, MessageConstant.ACCOUNT_DISABLED),
    NOT_FOUND(404, MessageConstant.RESOURCE_NOT_FOUND),
    IMAGE_NOT_AVAILABLE(404, MessageConstant.IMAGE_NOT_AVAILABLE),
    USERNAME_EXISTS(409, MessageConstant.USERNAME_EXISTS),
    IDEMPOTENCY_CONFLICT(409, MessageConstant.IDEMPOTENCY_CONFLICT),
    REQUEST_IN_PROGRESS(409, MessageConstant.REQUEST_IN_PROGRESS),
    STATE_CONFLICT(409, MessageConstant.STATE_CONFLICT),
    PRICE_CHANGED(409, MessageConstant.PRICE_CHANGED),
    SLOT_CONFLICT(409, MessageConstant.SLOT_CONFLICT),
    SCHEDULE_CONFLICT(409, MessageConstant.SCHEDULE_CONFLICT),
    OFFER_CLOSED(409, MessageConstant.OFFER_CLOSED),
    ORDER_TAKEN(409, MessageConstant.ORDER_TAKEN),
    REVIEW_EXISTS(409, MessageConstant.REVIEW_EXISTS),
    RESOURCE_IN_USE(409, MessageConstant.RESOURCE_IN_USE),
    CONFIG_CONFLICT(409, MessageConstant.CONFIG_CONFLICT),
    PRICE_OUT_OF_RANGE(422, MessageConstant.PRICE_OUT_OF_RANGE),
    OUTSIDE_SERVICE_AREA(422, MessageConstant.OUTSIDE_SERVICE_AREA),
    BOOKING_WINDOW_INVALID(422, MessageConstant.BOOKING_WINDOW_INVALID),
    OFFER_NOT_SUPPORTED(422, MessageConstant.OFFER_NOT_SUPPORTED),
    WORKER_INELIGIBLE(422, MessageConstant.WORKER_INELIGIBLE),
    START_CODE_INVALID(422, MessageConstant.START_CODE_INVALID),
    CATALOG_UNAVAILABLE(422, MessageConstant.CATALOG_UNAVAILABLE),
    IMAGE_TOO_LARGE(413, MessageConstant.IMAGE_TOO_LARGE),
    IMAGE_TYPE_UNSUPPORTED(415, MessageConstant.IMAGE_TYPE_UNSUPPORTED),
    INTERNAL_ERROR(500, MessageConstant.SERVICE_UNAVAILABLE);
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
