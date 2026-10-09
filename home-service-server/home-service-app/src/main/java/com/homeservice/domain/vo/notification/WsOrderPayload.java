package com.homeservice.domain.vo.notification;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.*;

/**
 * WebSocket订单载荷响应类
 * 封装WebSocket订单载荷相关响应数据
 */
@Builder
public record WsOrderPayload(
        @JsonProperty(value = "status", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OrderStatus status,
        @JsonProperty(value = "currentPrice", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiMoney
        @DecimalMin("0.00")
        @DecimalMax("999999999.99")
        @Digits(integer = 9, fraction = 2)
        BigDecimal currentPrice,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @Size(min = 1, max = 300)
        String reason) {}
