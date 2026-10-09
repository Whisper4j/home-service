package com.homeservice.domain.vo.account;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class LoginVO {

    private String accessToken; // 访问令牌

    private String tokenType; // 令牌类型

    private OffsetDateTime expiresAt; // 过期时间

    private AccountVO account; // 账号

    @Override
    public String toString() {
        return "LoginVO[credentials=REDACTED]";
    }
}
