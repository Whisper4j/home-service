package com.homeservice.handler;
import com.homeservice.common.domain.R;
import com.homeservice.domain.vo.error.ErrorDetailsVO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/** Servlet 错误分派也返回统一结构，不暴露默认错误页和异常细节。 */
@RestController
public class ApiErrorController implements ErrorController {
    @RequestMapping("/error")
    public ResponseEntity<R<ErrorDetailsVO>> error(HttpServletRequest request) {
        Object value = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int status = value instanceof Integer i && i >= 400 && i <= 599 ? i : 500;
        String code = status == 404 ? "NOT_FOUND" : status >= 500 ? "INTERNAL_ERROR" : "VALIDATION_ERROR";
        return ResponseEntity.status(status).body(new R<>(code, status == 404 ? "资源不存在" : "请求未能完成", ErrorDetailsVO.builder().build()));
    }
}
