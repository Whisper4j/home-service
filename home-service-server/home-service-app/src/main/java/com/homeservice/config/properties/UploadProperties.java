package com.homeservice.config.properties;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.nio.file.Path;

/**
 * 上传配置属性类
 * 绑定并校验上传配置
 */
@Getter
@Setter
@Validated
@ConfigurationProperties("home.upload")
public class UploadProperties {

    @NotNull(message = MessageConstant.CONFIG_VALUE_REQUIRED)
    private Path directory = Path.of("data/uploads");

}
