package com.homeservice.common.exception;
import lombok.Getter;
/** 业务异常携带稳定错误类型；不向公共模块反向引入业务模型。 */
@Getter
public class BusinessException extends RuntimeException {
    private final ErrorType errorType;
    public BusinessException(ErrorType errorType) {
        super(errorType.message());
        this.errorType = errorType;
    }
}
