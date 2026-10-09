package com.homeservice.config;

import com.homeservice.config.properties.WebSocketProperties;
import com.homeservice.handler.websocket.AuthenticatedWebSocketHandler;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.*;
import org.springframework.http.server.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * WebSocket配置类
 * 配置WebSocket端点、握手策略和认证任务调度器
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final AuthenticatedWebSocketHandler handler;
    private final WebSocketProperties properties;

    /**
     * 注册WebSocket端点和握手处理器
     */
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        var registration =
                registry.addHandler(handler, "/ws")
                        .addInterceptors(
                                new HandshakeInterceptor() {
                                    /**
                                     * 校验WebSocket握手请求
                                     */
                                    public boolean beforeHandshake(
                                            ServerHttpRequest request,
                                            ServerHttpResponse response,
                                            WebSocketHandler ws,
                                            Map<String, Object> attributes) {
                                        if (request.getURI().getRawQuery() != null) {
                                            response.setStatusCode(
                                                    org.springframework.http.HttpStatus
                                                            .BAD_REQUEST);
                                            return false;
                                        }
                                        return true;
                                    }

                                    /**
                                     * 完成WebSocket握手后的收尾处理
                                     */
                                    public void afterHandshake(
                                            ServerHttpRequest request,
                                            ServerHttpResponse response,
                                            WebSocketHandler ws,
                                            Exception ex) {
                                    }
                                });
        // 空配置沿用 Spring 同源检查；只允许显式 Origin，不设置全局 CORS。
        if (!properties.getAllowedOrigins().isEmpty())
            registration.setAllowedOrigins(properties.getAllowedOrigins().toArray(String[]::new));
    }

    /**
     * 创建WebSocket认证任务调度器
     */
    @Bean
    public static ThreadPoolTaskScheduler websocketScheduler() {
        var scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2);
        scheduler.setThreadNamePrefix("ws-auth-");
        scheduler.setRemoveOnCancelPolicy(true);
        scheduler.setWaitForTasksToCompleteOnShutdown(false);
        return scheduler;
    }
}
