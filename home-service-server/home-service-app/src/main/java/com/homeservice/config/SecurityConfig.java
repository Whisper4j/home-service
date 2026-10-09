package com.homeservice.config;

import com.homeservice.common.constant.MessageConstant;

import com.homeservice.config.properties.JwtProperties;
import com.homeservice.utils.StrictBcryptPasswordEncoder;

import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 安全配置类
 * 配置密码编码器、JWT签名密钥和系统时钟
 */
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {
    /**
     * 创建系统时钟
     */
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    /**
     * 创建BCrypt密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StrictBcryptPasswordEncoder();
    }

    /**
     * 创建JWT签名密钥
     */
    @Bean
    public SecretKey jwtSigningKey(JwtProperties properties) {
        if (!properties.isStrongKey()) throw new IllegalArgumentException(MessageConstant.JWT_SECRET_INVALID);
        return new SecretKeySpec(
                Base64.getDecoder().decode(properties.getSecretBase64()), "HmacSHA256");
    }
}
