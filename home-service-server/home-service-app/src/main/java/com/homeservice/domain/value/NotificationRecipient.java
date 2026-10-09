package com.homeservice.domain.value;

import com.homeservice.common.constant.MessageConstant;

import com.homeservice.enums.Role;

import lombok.Data;

@Data
public class NotificationRecipient {

    private final long accountId; // 接收账号ID
    private final Role role; // 接收账号角色

    public NotificationRecipient(long accountId, Role role) {
        if (accountId <= 0 || role == null) throw new IllegalArgumentException(MessageConstant.NOTIFICATION_RECIPIENT_INVALID);
        this.accountId = accountId;
        this.role = role;
    }
}
