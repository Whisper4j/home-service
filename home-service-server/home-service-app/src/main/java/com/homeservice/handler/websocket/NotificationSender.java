package com.homeservice.handler.websocket;
import com.homeservice.domain.value.NotificationRecipient;
import com.homeservice.domain.vo.notification.WsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.*;
/** 调用者先筛选明确接收者，在业务事务中登记；只有提交成功才尝试通知。 */
@Component @RequiredArgsConstructor
public class NotificationSender {
    private final WebSocketSessionRegistry registry;
    public void afterCommit(NotificationRecipient recipient, WsEvent event) {
        java.util.Objects.requireNonNull(recipient);
        java.util.Objects.requireNonNull(event);
        if (!TransactionSynchronizationManager.isActualTransactionActive() || !TransactionSynchronizationManager.isSynchronizationActive())
            throw new IllegalStateException("通知必须在业务事务内登记并于提交后发送");
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override public void afterCommit() { registry.sendCommitted(recipient, event); }
        });
    }
}
