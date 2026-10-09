package com.homeservice.domain.vo.payment;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.*;

/**
 * 支付响应类
 * 封装支付相关响应数据
 */
@Builder
public record PaymentVO(
        @JsonProperty(value = "id", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long id,
        @JsonProperty(value = "orderId", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long orderId,
        @JsonProperty(value = "businessNo", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 80)
        @NotBlank
        String businessNo,
        @JsonProperty(value = "type", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        PaymentType type,
        @JsonProperty(value = "amount", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiMoney
        @DecimalMin("0.00")
        @DecimalMax("999999999.99")
        @Digits(integer = 9, fraction = 2)
        BigDecimal amount,
        @JsonProperty(value = "createdAt", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime createdAt) {}
