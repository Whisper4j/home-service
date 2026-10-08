package com.homeservice.handler.websocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeservice.domain.dto.notification.WsAuthFrame;
import com.homeservice.domain.vo.notification.WsAuthAck;
import com.homeservice.domain.value.AccountPrincipal;
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
/** 每个连接独立身份与计时器，绝不读取 HTTP ThreadLocal。仅接受一次 AUTH。 */
@Component @RequiredArgsConstructor
public class AuthenticatedWebSocketHandler extends TextWebSocketHandler {
    private static final class State {
        final WebSocketSession session;
        final Instant authDeadline;
        AccountPrincipal principal;
        ScheduledFuture<?> timeout;
        ScheduledFuture<?> expiry;
        State(WebSocketSession session, Instant authDeadline) { this.session = session; this.authDeadline = authDeadline; }
    }
    private final ConcurrentHashMap<String, State> connections = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final AccountAuthenticator authenticator;
    private final WebSocketSessionRegistry registry;
    private final ThreadPoolTaskScheduler websocketScheduler;
    private final Clock clock;
    @Override public void afterConnectionEstablished(WebSocketSession session) {
        session.setTextMessageSizeLimit(4096);
        State state = new State(session, clock.instant().plusSeconds(5));
        connections.put(session.getId(), state);
        synchronized (state) {
            state.timeout = websocketScheduler.schedule(() -> {
                synchronized (state) { if (state.principal == null) close(state, 4401, "AUTH timeout"); }
            }, state.authDeadline);
        }
    }
    @Override protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        State state = connections.get(session.getId());
        if (state == null) return;
        synchronized (state) {
            if (state.principal != null) { close(state, 4403, "Only initial AUTH is accepted"); return; }
            if (!clock.instant().isBefore(state.authDeadline)) { close(state, 4401, "AUTH timeout"); return; }
            try {
                WsAuthFrame frame = objectMapper.readValue(message.getPayload(), WsAuthFrame.class);
                if (!validator.validate(frame).isEmpty() || frame.type() != WsAuthType.AUTH) { close(state, 4401, "Invalid AUTH"); return; }
                var principal = authenticator.authenticate(frame.accessToken());
                if (!clock.instant().isBefore(principal.expiresAt())) { close(state, 4401, "Token expired"); return; }
                state.principal = principal;
                if (state.timeout != null) state.timeout.cancel(false);
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(new WsAuthAck(WsAuthAckType.AUTHENTICATED,
                    OffsetDateTime.ofInstant(clock.instant(), ZoneOffset.ofHours(8))))));
                registry.register(session, principal);
                state.expiry = websocketScheduler.schedule(() -> {
                    synchronized (state) { close(state, 4401, "Token expired"); }
                }, principal.expiresAt());
            } catch (ApiException e) { close(state, e.getErrorType().httpStatus() == 403 ? 4403 : 4401, "Authentication failed"); }
            catch (Exception e) { close(state, 4401, "Invalid AUTH"); }
        }
    }
    private void cleanup(State state) {
        connections.remove(state.session.getId(), state);
        registry.remove(state.session.getId());
        if (state.timeout != null) state.timeout.cancel(false);
        if (state.expiry != null) state.expiry.cancel(false);
    }
    private void close(State state, int code, String reason) {
        cleanup(state);
        try { state.session.close(new CloseStatus(code, reason)); } catch (Exception ignored) { /* 连接已断开 */ }
    }
    @Override public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        State state = connections.get(session.getId());
        if (state != null) synchronized (state) { cleanup(state); }
    }
    @Override public void handleTransportError(WebSocketSession session, Throwable exception) {
        State state = connections.get(session.getId());
        if (state != null) synchronized (state) { close(state, 4401, "Transport closed"); }
    }
    @Override protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        State state = connections.get(session.getId());
        if (state != null) synchronized (state) { close(state, 4401, "AUTH must be text"); }
    }
    public int connectionCount() { return connections.size(); }
}
