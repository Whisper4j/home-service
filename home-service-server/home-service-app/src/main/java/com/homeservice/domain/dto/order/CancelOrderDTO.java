package com.homeservice.domain.dto.order;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 取消订单请求类
 * 接收取消订单相关请求参数
 */
@Builder
public record CancelOrderDTO(
        @JsonProperty(value = "reason", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 300)
        @NotBlank
        String reason) {}
