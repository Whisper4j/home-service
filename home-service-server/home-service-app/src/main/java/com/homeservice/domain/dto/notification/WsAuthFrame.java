package com.homeservice.domain.dto.notification;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * WebSocket认证帧请求类
 * 接收WebSocket认证帧相关请求参数
 */
@Builder
public record WsAuthFrame(
        @JsonProperty(value = "type", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        WsAuthType type,
        @JsonProperty(value = "accessToken", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 2048)
        @NotBlank
        String accessToken) {
    /**
     * 生成对象的安全文本描述
     */
    @Override
    public String toString() {
        return "WsAuthFrame[credentials=REDACTED]";
    }
}
