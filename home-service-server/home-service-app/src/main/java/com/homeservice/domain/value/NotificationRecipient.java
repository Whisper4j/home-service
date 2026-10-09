package com.homeservice.domain.value;

import com.homeservice.enums.Role;

import lombok.Data;

@Data
public class NotificationRecipient {

    private final long accountId; // 接收账号ID
    private final Role role; // 接收账号角色

    public NotificationRecipient(long accountId, Role role) {
        if (accountId <= 0 || role == null) throw new IllegalArgumentException("接收者身份无效");
        this.accountId = accountId;
        this.role = role;
    }
}
