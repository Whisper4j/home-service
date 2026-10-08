package com.homeservice.handler;
import cn.hutool.core.util.IdUtil;
import com.homeservice.common.domain.R;
import com.homeservice.common.exception.BusinessException;
import com.homeservice.domain.vo.error.*;
import com.homeservice.enums.ErrorCode;
import com.homeservice.exception.ApiException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.*;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.util.*;
@RestControllerAdvice @Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<R<ErrorDetailsVO>> business(BusinessException e) {
        var details = e instanceof ApiException api ? api.getDetails() : ErrorDetailsVO.builder().build();
        return ResponseEntity.status(e.getErrorType().httpStatus()).body(new R<>(e.getErrorType().code(), e.getErrorType().message(), details));
    }
    @ExceptionHandler(BindException.class)
    public ResponseEntity<R<ErrorDetailsVO>> validation(BindException e) {
        var fields = e.getBindingResult().getAllErrors().stream().map(error -> new FieldErrorVO(
            error instanceof org.springframework.validation.FieldError f ? f.getField() : "request",
            error.getDefaultMessage() == null ? "参数无效" : error.getDefaultMessage())).toList();
        return business(new ApiException(ErrorCode.VALIDATION_ERROR, ErrorDetailsVO.builder().fieldErrors(fields).build()));
    }
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
        MissingServletRequestParameterException.class, ServletRequestBindingException.class, ConstraintViolationException.class,
        HandlerMethodValidationException.class, InvalidPropertyException.class})
    public ResponseEntity<R<ErrorDetailsVO>> invalid(Exception e) {
        // 不回显 Jackson/绑定异常原文，其中可能包含密码、Token 或完整请求。
        return business(new ApiException(ErrorCode.VALIDATION_ERROR,
            ErrorDetailsVO.builder().fieldErrors(List.of(new FieldErrorVO("request", "请求格式或字段值无效"))).build()));
    }
    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<R<ErrorDetailsVO>> missing(Exception e) { return business(new ApiException(ErrorCode.NOT_FOUND)); }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<R<ErrorDetailsVO>> method(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(405).allow(e.getSupportedHttpMethods() == null ? new HttpMethod[0] : e.getSupportedHttpMethods().toArray(HttpMethod[]::new))
            .body(new R<>(ErrorCode.VALIDATION_ERROR.code(), "不支持的请求方法", ErrorDetailsVO.builder().build()));
    }
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<R<ErrorDetailsVO>> media(Exception e) { return ResponseEntity.status(415).body(new R<>("VALIDATION_ERROR", "不支持的请求媒体类型", ErrorDetailsVO.builder().build())); }
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<R<ErrorDetailsVO>> accept(Exception e) { return ResponseEntity.status(406).contentType(MediaType.APPLICATION_JSON).body(new R<>("VALIDATION_ERROR", "不支持的响应媒体类型", ErrorDetailsVO.builder().build())); }
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<R<ErrorDetailsVO>> tooLarge(Exception e) { return business(new ApiException(ErrorCode.IMAGE_TOO_LARGE)); }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<R<ErrorDetailsVO>> unexpected(Exception e) {
        String incident = IdUtil.fastSimpleUUID();
        // 记录类型和应用栈位置，避免驱动异常消息中的 SQL/参数或请求凭证进入日志。
        String location = Arrays.stream(e.getStackTrace()).filter(x -> x.getClassName().startsWith("com.homeservice.")).findFirst().map(Object::toString).orElse("framework");
        log.error("未预期异常 incident={} type={} location={}", incident, e.getClass().getName(), location);
        return business(new ApiException(ErrorCode.INTERNAL_ERROR));
    }
}
