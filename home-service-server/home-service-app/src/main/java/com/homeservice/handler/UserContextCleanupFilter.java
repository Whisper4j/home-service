package com.homeservice.handler;
import com.homeservice.utils.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
/** preHandle 抛错时该拦截器不会收到 afterCompletion，过滤器 finally 再提供请求级清理。 */
@Component @Order(Ordered.HIGHEST_PRECEDENCE)
public class UserContextCleanupFilter extends OncePerRequestFilter {
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        UserContext.clear();
        try { chain.doFilter(request, response); } finally { UserContext.clear(); }
    }
}
