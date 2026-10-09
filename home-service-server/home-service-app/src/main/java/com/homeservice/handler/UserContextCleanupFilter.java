package com.homeservice.handler;

import com.homeservice.utils.UserContext;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 用户上下文清理过滤器类
 * 在请求结束后清理用户上下文
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserContextCleanupFilter extends OncePerRequestFilter {
    /**
     * 执行请求并清理用户上下文
     */
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        UserContext.clear();
        try {
            chain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}
