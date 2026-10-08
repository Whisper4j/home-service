package com.homeservice.handler.websocket;

import com.homeservice.domain.value.NotificationRecipient;
import com.homeservice.domain.vo.notification.WsEvent;
import com.homeservice.enums.Role;
import org.junit.jupiter.api.*;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationSenderTest {
    @AfterEach void clear() { TransactionSynchronizationManager.clear(); }
    @Test void sendRequiresTransactionAndRunsOnlyAfterCommit() {
        var registry=mock(WebSocketSessionRegistry.class);var sender=new NotificationSender(registry);
        var recipient=new NotificationRecipient(1,Role.CUSTOMER);var event=WsEvent.builder().build();
        assertThatThrownBy(()->sender.afterCommit(recipient,event)).isInstanceOf(IllegalStateException.class);
        TransactionSynchronizationManager.initSynchronization();TransactionSynchronizationManager.setActualTransactionActive(true);
        sender.afterCommit(recipient,event);verifyNoInteractions(registry);
        var synchronization=TransactionSynchronizationManager.getSynchronizations().get(0);
        synchronization.afterCommit();verify(registry).sendCommitted(recipient,event);
    }
    @Test void rollbackNeverSends() {
        var registry=mock(WebSocketSessionRegistry.class);var sender=new NotificationSender(registry);
        TransactionSynchronizationManager.initSynchronization();TransactionSynchronizationManager.setActualTransactionActive(true);
        sender.afterCommit(new NotificationRecipient(2,Role.WORKER),WsEvent.builder().build());
        TransactionSynchronizationManager.getSynchronizations().forEach(s->s.afterCompletion(org.springframework.transaction.support.TransactionSynchronization.STATUS_ROLLED_BACK));
        verifyNoInteractions(registry);
    }
}
