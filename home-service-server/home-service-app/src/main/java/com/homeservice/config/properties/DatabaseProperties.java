package com.homeservice.config.properties;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * 数据库配置属性类
 * 绑定并校验数据库配置
 */
@Getter
@Setter
@Validated
@ConfigurationProperties("home.database")
public class DatabaseProperties {

    @NotBlank(message = MessageConstant.CONFIG_VALUE_REQUIRED)
    private String host;
    @Min(value = 1, message = MessageConstant.CONFIG_VALUE_INVALID)
    @Max(value = 65535, message = MessageConstant.CONFIG_VALUE_INVALID)
    private int port;
    @NotBlank(message = MessageConstant.CONFIG_VALUE_REQUIRED)
    @Pattern(regexp = "[A-Za-z0-9_]+", message = MessageConstant.CONFIG_VALUE_INVALID)
    private String name;
    @NotBlank(message = MessageConstant.CONFIG_VALUE_REQUIRED)
    private String username;
    @NotBlank(message = MessageConstant.CONFIG_VALUE_REQUIRED)
    private String password;
    @NotBlank(message = MessageConstant.CONFIG_VALUE_REQUIRED)
    private String sslMode;
    private boolean allowPublicKeyRetrieval;

}
