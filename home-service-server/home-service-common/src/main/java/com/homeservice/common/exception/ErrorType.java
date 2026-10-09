package com.homeservice.common.exception;

/**
 * 错误接口
 * 定义统一异常所需的错误码、HTTP状态和提示
 */
public interface ErrorType {
    /**
     * 获取业务错误码
     */
    String code();

    /**
     * 获取对应的HTTP状态
     */
    int httpStatus();

    /**
     * 获取业务错误提示
     */
    String message();
}
