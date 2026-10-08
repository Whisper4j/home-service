package com.homeservice.exception;
import com.homeservice.common.exception.BusinessException;
import com.homeservice.domain.vo.error.ErrorDetailsVO;
import com.homeservice.enums.ErrorCode;
import lombok.Getter;
@Getter
public class ApiException extends BusinessException {
    private final ErrorDetailsVO details;
    public ApiException(ErrorCode code) { this(code, ErrorDetailsVO.builder().build()); }
    public ApiException(ErrorCode code, ErrorDetailsVO details) { super(code); this.details = details; }
}
