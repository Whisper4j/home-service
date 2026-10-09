package com.homeservice.domain.value;

import com.homeservice.enums.Role;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountPrincipal {

    private long accountId; // 账号ID
    private Role role; // 账号角色
    private Instant expiresAt; // 令牌过期时间
}
