package com.homeservice.domain.vo.notification;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * WebSocket事件响应类
 * 封装WebSocket事件相关响应数据
 */
@Builder
public record WsEvent(
        @JsonProperty(value = "eventId", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long eventId,
        @JsonProperty(value = "type", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        WsEventType type,
        @JsonProperty(value = "orderId", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long orderId,
        @JsonProperty(value = "priceVersion", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Min(1)
        @Max(2147483647)
        Integer priceVersion,
        @JsonProperty(value = "occurredAt", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime occurredAt,
        @JsonProperty(value = "payload", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Valid
        WsOrderPayload payload) {}
