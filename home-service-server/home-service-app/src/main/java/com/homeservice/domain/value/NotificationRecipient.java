package com.homeservice.domain.value;

import com.homeservice.enums.Role;

/**
 * 通知接收者值对象类
 * 表达通知接收者相关业务值
 */
public record NotificationRecipient(long accountId, Role role) {
    /**
     * 创建并校验通知接收者实例
     */
    public NotificationRecipient {
        if (accountId <= 0 || role == null) throw new IllegalArgumentException("接收者身份无效");
    }
}
