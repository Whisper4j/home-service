package com.homeservice.handler.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeservice.domain.dto.notification.WsAuthFrame;
import com.homeservice.domain.value.AccountPrincipal;
import com.homeservice.domain.vo.notification.WsAuthAck;
import com.homeservice.enums.*;
import com.homeservice.exception.ApiException;
import com.homeservice.utils.AccountAuthenticator;

import jakarta.validation.Validator;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.*;
import java.util.concurrent.*;

/**
 * WebSocket认证处理类
 * 处理WebSocket首帧认证和连接生命周期
 */
@Component
@RequiredArgsConstructor
public class AuthenticatedWebSocketHandler extends TextWebSocketHandler {
    private static final String AUTH_TYPE = "AUTH";
    private static final String AUTHENTICATED_TYPE = "AUTHENTICATED";

    /**
     * WebSocket连接状态类
     * 保存WebSocket连接的认证任务和到期任务
     */
    private static final class State {

        final WebSocketSession session;
        final Instant authDeadline;
        AccountPrincipal principal;
        ScheduledFuture<?> timeout;
        ScheduledFuture<?> expiry;

        /**
         * 创建状态实例
         */
        State(WebSocketSession session, Instant authDeadline) {
            this.session = session;
            this.authDeadline = authDeadline;
        }
    }

    private final ConcurrentHashMap<String, State> connections = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final AccountAuthenticator authenticator;
    private final WebSocketSessionRegistry registry;
    private final ThreadPoolTaskScheduler websocketScheduler;
    private final Clock clock;

    /**
     * 登记新建的WebSocket连接
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        session.setTextMessageSizeLimit(4096);
        State state = new State(session, clock.instant().plusSeconds(5));
        connections.put(session.getId(), state);
        synchronized (state) {
            state.timeout =
                    websocketScheduler.schedule(
                            () -> {
                                synchronized (state) {
                                    if (state.principal == null) close(state, 4401, "AUTH timeout");
                                }
                            },
                            state.authDeadline);
        }
    }

    /**
     * 处理WebSocket文本消息
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        State state = connections.get(session.getId());
        if (state == null) return;
        synchronized (state) {
            if (state.principal != null) {
                close(state, 4403, "Only initial AUTH is accepted");
                return;
            }
            if (!clock.instant().isBefore(state.authDeadline)) {
                close(state, 4401, "AUTH timeout");
                return;
            }
            try {
                WsAuthFrame frame = objectMapper.readValue(message.getPayload(), WsAuthFrame.class);
                if (!validator.validate(frame).isEmpty() || !AUTH_TYPE.equals(frame.getType())) {
                    close(state, 4401, "Invalid AUTH");
                    return;
                }
                var principal = authenticator.authenticate(frame.getAccessToken());
                if (!clock.instant().isBefore(principal.getExpiresAt())) {
                    close(state, 4401, "Token expired");
                    return;
                }
                state.principal = principal;
                if (state.timeout != null) state.timeout.cancel(false);
                WsAuthAck ack = new WsAuthAck();
                ack.setType(AUTHENTICATED_TYPE);
                ack.setOccurredAt(
                        OffsetDateTime.ofInstant(clock.instant(), ZoneOffset.ofHours(8)));
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(ack)));
                registry.register(session, principal);
                state.expiry =
                        websocketScheduler.schedule(
                                () -> {
                                    synchronized (state) {
                                        close(state, 4401, "Token expired");
                                    }
                                },
                                principal.getExpiresAt());
            } catch (ApiException e) {
                close(
                        state,
                        e.getErrorType().httpStatus() == 403 ? 4403 : 4401,
                        "Authentication failed");
            } catch (Exception e) {
                close(state, 4401, "Invalid AUTH");
            }
        }
    }

    /**
     * 清理WebSocket连接状态
     */
    private void cleanup(State state) {
        connections.remove(state.session.getId(), state);
        registry.remove(state.session.getId());
        if (state.timeout != null) state.timeout.cancel(false);
        if (state.expiry != null) state.expiry.cancel(false);
    }

    /**
     * 关闭WebSocket连接
     */
    private void close(State state, int code, String reason) {
        cleanup(state);
        try {
            state.session.close(new CloseStatus(code, reason));
        } catch (Exception ignored) {
            /* 连接已断开 */
        }
    }

    /**
     * 清理已经断开的WebSocket连接
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        State state = connections.get(session.getId());
        if (state != null)
            synchronized (state) {
                cleanup(state);
            }
    }

    /**
     * 处理WebSocket传输异常
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        State state = connections.get(session.getId());
        if (state != null)
            synchronized (state) {
                close(state, 4401, "Transport closed");
            }
    }

    /**
     * 拒绝WebSocket二进制消息
     */
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        State state = connections.get(session.getId());
        if (state != null)
            synchronized (state) {
                close(state, 4401, "AUTH must be text");
            }
    }

    /**
     * 获取待认证WebSocket连接数量
     */
    public int connectionCount() {
        return connections.size();
    }
}
