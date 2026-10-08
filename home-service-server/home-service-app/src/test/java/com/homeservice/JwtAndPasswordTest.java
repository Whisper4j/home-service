package com.homeservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeservice.config.properties.JwtProperties;
import com.homeservice.enums.*;
import com.homeservice.exception.ApiException;
import com.homeservice.support.TestSecrets;
import com.homeservice.utils.*;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import javax.crypto.spec.SecretKeySpec;
import java.time.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

class JwtAndPasswordTest {
    private final Instant now=Instant.parse("2026-10-08T01:00:00Z");
    private final SecretKeySpec key=new SecretKeySpec(Base64.getDecoder().decode(TestSecrets.JWT_BASE64),"HmacSHA256");
    private final JwtProperties properties=properties();
    private JwtProperties properties() { var p=new JwtProperties();p.setSecretBase64(TestSecrets.JWT_BASE64);p.setTokenTtl(Duration.ofHours(2));return p; }
    private JwtTool tool(Instant time) { return new JwtTool(key,properties,Clock.fixed(time,ZoneOffset.UTC),new ObjectMapper()); }
    @Test void bcryptMatchesAndRejectsUtf8OverflowWithoutTruncation() {
        var encoder=new StrictBcryptPasswordEncoder();
        String password="  secure-password  ";String hash=encoder.encode(password);
        assertThat(encoder.matches(password,hash)).isTrue();
        assertThat(encoder.matches(password.strip(),hash)).isFalse();
        String boundary="汉".repeat(24);
        assertThat(encoder.matches(boundary,encoder.encode(boundary))).isTrue();
        assertThatThrownBy(()->encoder.encode(boundary+"a")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(()->encoder.matches(boundary+"a",hash)).isInstanceOf(IllegalArgumentException.class);
    }
    @Test void jwtEnforcesSignatureAlgorithmExpiryAndPayload() {
        String token=tool(now).createToken(9007199254740993L,Role.WORKER);
        assertThat(tool(now).parse(token).accountId()).isEqualTo(9007199254740993L);
        assertCode(() -> tool(now.plusSeconds(7200)).parse(token),ErrorCode.TOKEN_EXPIRED);
        String[] parts=token.split("\\.");
        parts[1]=Base64.getUrlEncoder().withoutPadding().encodeToString("{\"accountId\":\"3\",\"role\":\"ADMIN\",\"exp\":9999999999}".getBytes(java.nio.charset.StandardCharsets.UTF_8));
        assertCode(()->tool(now).parse(String.join(".",parts)),ErrorCode.UNAUTHENTICATED);
        String wrongAlgorithm=Jwts.builder().claim("accountId","1").claim("role","CUSTOMER").expiration(Date.from(now.plusSeconds(60))).signWith(key,Jwts.SIG.HS512).compact();
        assertCode(()->tool(now).parse(wrongAlgorithm),ErrorCode.UNAUTHENTICATED);
        String missingExpiry=Jwts.builder().claim("accountId","1").claim("role","CUSTOMER").signWith(key,Jwts.SIG.HS256).compact();
        assertCode(()->tool(now).parse(missingExpiry),ErrorCode.UNAUTHENTICATED);
        String numericId=Jwts.builder().claim("accountId",1).claim("role","CUSTOMER").expiration(Date.from(now.plusSeconds(60))).signWith(key,Jwts.SIG.HS256).compact();
        assertCode(()->tool(now).parse(numericId),ErrorCode.UNAUTHENTICATED);
        String wrongRole=Jwts.builder().claim("accountId","1").claim("role","ROOT").expiration(Date.from(now.plusSeconds(60))).signWith(key,Jwts.SIG.HS256).compact();
        assertCode(()->tool(now).parse(wrongRole),ErrorCode.FORBIDDEN);
        assertCode(()->tool(now).parse("eyJhbGciOiJub25lIn0.e30."),ErrorCode.UNAUTHENTICATED);
    }
    private void assertCode(Runnable action,ErrorCode code) {
        assertThatThrownBy(action::run).isInstanceOfSatisfying(ApiException.class,e->assertThat(e.getErrorType()).isEqualTo(code));
    }
}
