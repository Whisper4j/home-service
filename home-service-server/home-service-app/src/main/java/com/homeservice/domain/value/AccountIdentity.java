package com.homeservice.domain.value;

import com.homeservice.enums.*;

/**
 * 账号身份值对象类
 * 表达账号身份相关业务值
 */
public record AccountIdentity(Long accountId, Role role, AccountStatus status) {}
