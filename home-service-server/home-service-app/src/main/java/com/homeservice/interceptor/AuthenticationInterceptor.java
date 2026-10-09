package com.homeservice.interceptor;

import com.homeservice.config.properties.AuthProperties;
import com.homeservice.enums.*;
import com.homeservice.exception.ApiException;
import com.homeservice.utils.*;

import jakarta.servlet.http.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import java.util.Collections;

/**
 * 身份认证拦截器类
 * 校验接口令牌并控制三端角色访问
 */
@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements AsyncHandlerInterceptor {

    private final AccountAuthenticator authenticator;
    private final AuthProperties properties;
    private final AntPathMatcher matcher = new AntPathMatcher();

    /**
     * 校验HTTP请求身份和角色
     */
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) {
        UserContext.clear();
        String path = request.getRequestURI().substring(request.getContextPath().length());
        for (String endpoint : properties.getPublicEndpoints()) {
            int space = endpoint.indexOf(' ');
            if (request.getMethod().equals(endpoint.substring(0, space))
                    && matcher.match(endpoint.substring(space + 1), path)) return true;
        }
        var headers = Collections.list(request.getHeaders("Authorization"));
        if (headers.size() != 1) throw new ApiException(ErrorCode.UNAUTHENTICATED);
        var principal = authenticator.authenticate(JwtTool.bearer(headers.get(0)));
        Role required =
                path.startsWith("/api/customer/")
                        ? Role.CUSTOMER
                        : path.startsWith("/api/worker/")
                                ? Role.WORKER
                                : path.startsWith("/api/admin/") ? Role.ADMIN : null;
        if (required == null || required != principal.role())
            throw new ApiException(ErrorCode.FORBIDDEN);
        UserContext.set(principal);
        return true;
    }

    /**
     * 清理请求完成后的用户上下文
     */
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {
        UserContext.clear();
    }

    /**
     * 清理异步请求移交后的用户上下文
     */
    public void afterConcurrentHandlingStarted(
            HttpServletRequest request, HttpServletResponse response, Object handler) {
        UserContext.clear();
    }
}
