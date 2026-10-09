package com.homeservice.domain.vo.notification;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * WebSocket认证确认响应类
 * 封装WebSocket认证确认相关响应数据
 */
@Builder
public record WsAuthAck(
        @JsonProperty(value = "type", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        WsAuthAckType type,
        @JsonProperty(value = "occurredAt", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime occurredAt) {}
