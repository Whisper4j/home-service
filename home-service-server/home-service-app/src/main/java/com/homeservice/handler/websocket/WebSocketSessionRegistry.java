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
/** 仅存已认证会话；无按角色广播接口，避免泄露未做资格筛选的优惠池。 */
@Component @RequiredArgsConstructor
public class WebSocketSessionRegistry {
    private record Binding(WebSocketSession session, AccountPrincipal principal) {}
    private final ConcurrentHashMap<String, Binding> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final AccountAuthenticator authenticator;
    private final Clock clock;
    public void register(WebSocketSession session, AccountPrincipal principal) {
        sessions.put(session.getId(), new Binding(new ConcurrentWebSocketSessionDecorator(session, 5000, 65536), principal));
    }
    public void remove(String sessionId) { sessions.remove(sessionId); }
    public int size() { return sessions.size(); }
    void sendCommitted(NotificationRecipient recipient, WsEvent event) {
        for (Binding binding : sessions.values()) {
            var principal = binding.principal();
            if (principal.accountId() != recipient.accountId() || principal.role() != recipient.role()) continue;
            try {
                if (!clock.instant().isBefore(principal.expiresAt())) throw new com.homeservice.exception.ApiException(com.homeservice.enums.ErrorCode.TOKEN_EXPIRED);
                authenticator.check(principal);
                binding.session().sendMessage(new TextMessage(objectMapper.writeValueAsString(event)));
            } catch (Exception e) {
                remove(binding.session().getId());
                int code = e instanceof com.homeservice.exception.ApiException a && a.getErrorType().httpStatus() == 403 ? 4403 : 4401;
                try { binding.session().close(new CloseStatus(code, "Session unavailable")); } catch (Exception ignored) { /* 已断开 */ }
            }
        }
    }
}
