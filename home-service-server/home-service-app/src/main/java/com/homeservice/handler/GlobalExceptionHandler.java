package com.homeservice.handler;

import cn.hutool.core.util.IdUtil;

import com.homeservice.common.domain.Result;
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
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.*;

/**
 * 全局异常处理类
 * 将接口异常转换为统一错误响应
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<ErrorDetailsVO>> business(BusinessException e) {
        var details = e instanceof ApiException api ? api.getDetails() : new ErrorDetailsVO();
        return ResponseEntity.status(e.getErrorType().httpStatus())
                .body(Result.error(e.getErrorType().code(), e.getErrorType().message(), details));
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result<ErrorDetailsVO>> validation(BindException e) {
        var fields = e.getBindingResult().getAllErrors().stream().map(this::fieldError).toList();
        ErrorDetailsVO details = new ErrorDetailsVO();
        details.setFieldErrors(fields);
        return business(new ApiException(ErrorCode.VALIDATION_ERROR, details));
    }

    /**
     * 处理请求格式异常
     */
    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        MethodArgumentTypeMismatchException.class,
        MissingServletRequestParameterException.class,
        ServletRequestBindingException.class,
        ConstraintViolationException.class,
        HandlerMethodValidationException.class,
        InvalidPropertyException.class
    })
    public ResponseEntity<Result<ErrorDetailsVO>> invalid(Exception e) {
        // 不回显 Jackson/绑定异常原文，其中可能包含密码、Token 或完整请求。
        FieldErrorVO field = new FieldErrorVO();
        field.setField("request");
        field.setMessage("请求格式或字段值无效");
        ErrorDetailsVO details = new ErrorDetailsVO();
        details.setFieldErrors(List.of(field));
        return business(new ApiException(ErrorCode.VALIDATION_ERROR, details));
    }

    /**
     * 处理资源不存在异常
     */
    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<Result<ErrorDetailsVO>> missing(Exception e) {
        return business(new ApiException(ErrorCode.NOT_FOUND));
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Result<ErrorDetailsVO>> method(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(405)
                .allow(
                        e.getSupportedHttpMethods() == null
                                ? new HttpMethod[0]
                                : e.getSupportedHttpMethods().toArray(HttpMethod[]::new))
                .body(
                        Result.error(
                                ErrorCode.VALIDATION_ERROR.code(),
                                "不支持的请求方法",
                                new ErrorDetailsVO()));
    }

    /**
     * 处理请求媒体类型不支持异常
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Result<ErrorDetailsVO>> media(Exception e) {
        return ResponseEntity.status(415)
                .body(
                        Result.error(
                                "VALIDATION_ERROR",
                                "不支持的请求媒体类型",
                                new ErrorDetailsVO()));
    }

    /**
     * 处理响应媒体类型不支持异常
     */
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<Result<ErrorDetailsVO>> accept(Exception e) {
        return ResponseEntity.status(406)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Result.error(
                                "VALIDATION_ERROR",
                                "不支持的响应媒体类型",
                                new ErrorDetailsVO()));
    }

    /**
     * 处理上传文件过大异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Result<ErrorDetailsVO>> tooLarge(Exception e) {
        return business(new ApiException(ErrorCode.IMAGE_TOO_LARGE));
    }

    /**
     * 处理未预期异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<ErrorDetailsVO>> unexpected(Exception e) {
        String incident = IdUtil.fastSimpleUUID();
        // 记录类型和应用栈位置，避免驱动异常消息中的 SQL/参数或请求凭证进入日志。
        String location =
                Arrays.stream(e.getStackTrace())
                        .filter(x -> x.getClassName().startsWith("com.homeservice."))
                        .findFirst()
                        .map(Object::toString)
                        .orElse("framework");
        log.error(
                "未预期异常 incident={} type={} location={}",
                incident,
                e.getClass().getName(),
                location);
        return business(new ApiException(ErrorCode.INTERNAL_ERROR));
    }

    private FieldErrorVO fieldError(org.springframework.validation.ObjectError error) {
        FieldErrorVO field = new FieldErrorVO();
        field.setField(
                error instanceof org.springframework.validation.FieldError value
                        ? value.getField()
                        : "request");
        field.setMessage(error.getDefaultMessage() == null ? "参数无效" : error.getDefaultMessage());
        return field;
    }
}
