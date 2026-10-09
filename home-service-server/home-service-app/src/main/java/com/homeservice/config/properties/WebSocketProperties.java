package com.homeservice.config.properties;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.net.URI;
import java.util.List;

/**
 * WebSocket配置属性类
 * 绑定并校验WebSocket配置
 */
@Getter
@Setter
@Validated
@ConfigurationProperties("home.websocket")
public class WebSocketProperties {

    @NotNull(message = MessageConstant.CONFIG_VALUE_REQUIRED)
    private List<String> allowedOrigins = List.of();

    /**
     * 校验WebSocket来源配置
     */
    @AssertTrue(message = MessageConstant.WEBSOCKET_ORIGIN_INVALID)
    public boolean isExplicitOrigins() {
        if (allowedOrigins == null) return false;
        try {
            return allowedOrigins.stream()
                    .allMatch(
                            value -> {
                                URI uri = URI.create(value);
                                return ("http".equals(uri.getScheme())
                                                || "https".equals(uri.getScheme()))
                                        && uri.getHost() != null
                                        && uri.getUserInfo() == null
                                        && uri.getQuery() == null
                                        && uri.getFragment() == null
                                        && (uri.getPath() == null || uri.getPath().isEmpty());
                            });
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
