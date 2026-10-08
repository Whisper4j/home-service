package com.homeservice.domain.value;
import com.homeservice.enums.Role;
/** 已经由未来业务 Service 完成归属/优惠资格过滤的明确接收者。 */
public record NotificationRecipient(long accountId, Role role) {
    public NotificationRecipient { if (accountId <= 0 || role == null) throw new IllegalArgumentException("接收者身份无效"); }
}
