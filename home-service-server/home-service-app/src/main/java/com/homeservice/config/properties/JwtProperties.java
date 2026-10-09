package com.homeservice.config.properties;

import com.homeservice.common.constant.MessageConstant;

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

    @NotBlank(message = MessageConstant.CONFIG_VALUE_REQUIRED)
    private String secretBase64;
    @NotNull(message = MessageConstant.CONFIG_VALUE_REQUIRED)
    private Duration tokenTtl = Duration.ofHours(2);

    /**
     * 校验JWT密钥强度
     */
    @AssertTrue(message = MessageConstant.JWT_SECRET_INVALID)
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
    @AssertTrue(message = MessageConstant.JWT_TTL_INVALID)
    public boolean isValidTtl() {
        return tokenTtl != null && tokenTtl.getSeconds() > 0 && tokenTtl.getNano() == 0;
    }
}
