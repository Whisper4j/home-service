package com.homeservice.common.exception;

import lombok.Getter;

/**
 * 业务异常类
 * 封装可预期的业务异常信息
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorType errorType;

    /**
     * 创建Business异常实例
     */
    public BusinessException(ErrorType errorType) {
        super(errorType.message());
        this.errorType = errorType;
    }
}
