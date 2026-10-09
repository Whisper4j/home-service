package com.homeservice.exception;

import com.homeservice.common.exception.BusinessException;
import com.homeservice.domain.vo.error.ErrorDetailsVO;
import com.homeservice.enums.ErrorCode;

import lombok.Getter;

/**
 * 接口异常类
 * 封装接口业务错误码和错误详情
 */
@Getter
public class ApiException extends BusinessException {

    private final ErrorDetailsVO details;

    /**
     * 创建接口异常实例
     */
    public ApiException(ErrorCode code) {
        this(code, new ErrorDetailsVO());
    }

    /**
     * 创建接口异常实例
     */
    public ApiException(ErrorCode code, ErrorDetailsVO details) {
        super(code);
        this.details = details;
    }
}
