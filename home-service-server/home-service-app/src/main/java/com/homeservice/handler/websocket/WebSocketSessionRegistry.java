package com.homeservice.handler.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeservice.domain.value.*;
import com.homeservice.domain.vo.notification.WsEvent;
import com.homeservice.utils.AccountAuthenticator;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.time.Clock;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket会话管理类
 * 管理已认证WebSocket会话并发送定向消息
 */
@Component
@RequiredArgsConstructor
public class WebSocketSessionRegistry {
    /**
     * WebSocket会话绑定记录类
     * 保存WebSocket会话及其认证身份
     */
    private record Binding(WebSocketSession session, AccountPrincipal principal) {}

    private final ConcurrentHashMap<String, Binding> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final AccountAuthenticator authenticator;
    private final Clock clock;

    /**
     * 注册已认证WebSocket会话
     */
    public void register(WebSocketSession session, AccountPrincipal principal) {
        sessions.put(
                session.getId(),
                new Binding(
                        new ConcurrentWebSocketSessionDecorator(session, 5000, 65536), principal));
    }

    /**
     * 移除WebSocket会话
     */
    public void remove(String sessionId) {
        sessions.remove(sessionId);
    }

    /**
     * 获取已认证WebSocket会话数量
     */
    public int size() {
        return sessions.size();
    }

    /**
     * 向明确接收者发送已提交事件
     */
    void sendCommitted(NotificationRecipient recipient, WsEvent event) {
        for (Binding binding : sessions.values()) {
            var principal = binding.principal();
            if (principal.getAccountId() != recipient.getAccountId()
                    || principal.getRole() != recipient.getRole()) continue;
            try {
                if (!clock.instant().isBefore(principal.getExpiresAt()))
                    throw new com.homeservice.exception.ApiException(
                            com.homeservice.enums.ErrorCode.TOKEN_EXPIRED);
                authenticator.check(principal);
                binding.session()
                        .sendMessage(new TextMessage(objectMapper.writeValueAsString(event)));
            } catch (Exception e) {
                remove(binding.session().getId());
                int code =
                        e instanceof com.homeservice.exception.ApiException a
                                        && a.getErrorType().httpStatus() == 403
                                ? 4403
                                : 4401;
                try {
                    binding.session().close(new CloseStatus(code, "Session unavailable"));
                } catch (Exception ignored) {
                    /* 已断开 */
                }
            }
        }
    }
}
