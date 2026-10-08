package com.homeservice.domain.value;
import com.homeservice.enums.*;
/** 认证只读投影，不包含密码，也不是人员业务资料。 */
public record AccountIdentity(Long accountId, Role role, AccountStatus status) {}
