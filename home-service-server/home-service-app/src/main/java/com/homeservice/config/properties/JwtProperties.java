package com.homeservice.config.properties;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.Base64;

/**
 * JWT配置属性类
 * 绑定并校验JWT配置
 */
@Getter
@Setter
@Validated
@ConfigurationProperties("home.jwt")
public class JwtProperties {

    @NotBlank
    private String secretBase64;
    @NotNull
    private Duration tokenTtl = Duration.ofHours(2);

    /**
     * 校验JWT密钥强度
     */
    @AssertTrue(message = "JWT 密钥必须是至少 32 随机字节的 Base64")
    public boolean isStrongKey() {
        try {
            return Base64.getDecoder().decode(secretBase64).length >= 32;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    /**
     * 校验JWT有效期配置
     */
    @AssertTrue(message = "JWT 有效期必须至少 1 秒且为整秒")
    public boolean isValidTtl() {
        return tokenTtl != null && tokenTtl.getSeconds() > 0 && tokenTtl.getNano() == 0;
    }
}
