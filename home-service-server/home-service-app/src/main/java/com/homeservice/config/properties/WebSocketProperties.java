package com.homeservice.config.properties;

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

    @NotNull
    private List<String> allowedOrigins = List.of();

    /**
     * 校验WebSocket来源配置
     */
    @AssertTrue(message = "Origin 只接受明确的 http(s) 源，不接受通配符、路径或凭据")
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
