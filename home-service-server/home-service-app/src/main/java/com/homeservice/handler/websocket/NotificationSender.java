package com.homeservice.handler.websocket;

import com.homeservice.common.constant.MessageConstant;

import com.homeservice.domain.value.NotificationRecipient;
import com.homeservice.domain.vo.notification.WsEvent;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.*;

/**
 * 通知发送类
 * 在事务提交后向指定账号发送通知
 */
@Component
@RequiredArgsConstructor
public class NotificationSender {

    private final WebSocketSessionRegistry registry;

    /**
     * 登记事务提交后的定向通知
     */
    public void afterCommit(NotificationRecipient recipient, WsEvent event) {
        java.util.Objects.requireNonNull(recipient);
        java.util.Objects.requireNonNull(event);
        if (!TransactionSynchronizationManager.isActualTransactionActive()
                || !TransactionSynchronizationManager.isSynchronizationActive())
            throw new IllegalStateException(MessageConstant.NOTIFICATION_TRANSACTION_REQUIRED);
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    /**
                     * 登记事务提交后的定向通知
                     */
                    @Override
                    public void afterCommit() {
                        registry.sendCommitted(recipient, event);
                    }
                });
    }
}
