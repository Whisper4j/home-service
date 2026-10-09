package com.homeservice.domain.value;

import com.homeservice.enums.Role;

import java.time.Instant;

/**
 * 账号登录主体值对象类
 * 表达账号登录主体相关业务值
 */
public record AccountPrincipal(long accountId, Role role, Instant expiresAt) {}
