package com.homeservice.handler;

import com.homeservice.common.domain.Result;
import com.homeservice.domain.vo.error.ErrorDetailsVO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 框架错误控制器类
 * 处理未进入控制器的框架错误请求
 */
@RestController
public class ApiErrorController implements ErrorController {
    /**
     * 创建失败响应结果
     */
    @RequestMapping("/error")
    public ResponseEntity<Result<ErrorDetailsVO>> error(HttpServletRequest request) {
        Object value = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int status = value instanceof Integer i && i >= 400 && i <= 599 ? i : 500;
        String code =
                status == 404 ? "NOT_FOUND" : status >= 500 ? "INTERNAL_ERROR" : "VALIDATION_ERROR";
        return ResponseEntity.status(status)
                .body(
                        Result.error(
                                code,
                                status == 404 ? "资源不存在" : "请求未能完成",
                                new ErrorDetailsVO()));
    }
}
