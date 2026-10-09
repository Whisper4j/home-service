package com.homeservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeservice.config.properties.JwtProperties;
import com.homeservice.domain.value.AccountPrincipal;
import com.homeservice.enums.*;
import com.homeservice.exception.ApiException;

import io.jsonwebtoken.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.time.*;
import java.time.Clock;
import java.util.*;

import javax.crypto.SecretKey;

/**
 * JWT工具类
 * 签发并校验HS256访问令牌
 */
@Component
@RequiredArgsConstructor
public class JwtTool {

    private final SecretKey jwtSigningKey;
    private final JwtProperties properties;
    private final Clock clock;
    private final ObjectMapper objectMapper;

    /**
     * 签发访问令牌
     */
    public String createToken(long accountId, Role role) {
        if (accountId <= 0 || role == null) throw new IllegalArgumentException("令牌主体无效");
        Instant now = clock.instant().truncatedTo(java.time.temporal.ChronoUnit.SECONDS);
        return Jwts.builder()
                .subject(Long.toString(accountId))
                .claim("accountId", Long.toString(accountId))
                .claim("role", role.getValue())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(properties.getTokenTtl())))
                .signWith(jwtSigningKey, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 解析并校验输入数据
     */
    public AccountPrincipal parse(String token) {
        if (token == null || token.isBlank() || token.length() > 2048)
            throw new ApiException(ErrorCode.UNAUTHENTICATED);
        try {
            var jwt =
                    Jwts.parser()
                            .verifyWith(jwtSigningKey)
                            .clock(() -> Date.from(clock.instant()))
                            .sig()
                            .clear()
                            .add(Jwts.SIG.HS256)
                            .and()
                            .build()
                            .parseSignedClaims(token);
            // 在验签之后复核 JSON 原始类型，禁止小数 exp/数字 ID 被 JWT 库隐式转换。
            var raw = objectMapper.readTree(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
            if (!raw.path("accountId").isTextual()
                    || !raw.path("role").isTextual()
                    || !raw.path("exp").isIntegralNumber()
                    || !raw.path("exp").canConvertToLong()
                    || !raw.path("accountId").asText().matches("[1-9][0-9]{0,18}"))
                throw new ApiException(ErrorCode.UNAUTHENTICATED);
            long accountId = Long.parseLong(raw.get("accountId").asText());
            if (jwt.getPayload().getExpiration() == null)
                throw new ApiException(ErrorCode.UNAUTHENTICATED);
            Role role;
            try {
                role = Role.valueOf(raw.get("role").asText());
            } catch (IllegalArgumentException e) {
                throw new ApiException(ErrorCode.FORBIDDEN);
            }
            Instant expiresAt = Instant.ofEpochSecond(raw.get("exp").longValue());
            if (!clock.instant().isBefore(expiresAt))
                throw new ApiException(ErrorCode.TOKEN_EXPIRED);
            return new AccountPrincipal(accountId, role, expiresAt);
        } catch (ExpiredJwtException e) {
            throw new ApiException(ErrorCode.TOKEN_EXPIRED);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ErrorCode.UNAUTHENTICATED);
        }
    }

    /**
     * 解析Bearer访问令牌
     */
    public static String bearer(String header) {
        if (header == null
                || !header.matches(
                        "(?i)Bearer [A-Za-z0-9_\\-]+\\.[A-Za-z0-9_\\-]+\\.[A-Za-z0-9_\\-]+"))
            throw new ApiException(ErrorCode.UNAUTHENTICATED);
        return header.substring(7);
    }
}
