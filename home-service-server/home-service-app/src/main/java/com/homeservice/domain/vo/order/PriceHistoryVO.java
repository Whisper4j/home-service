package com.homeservice.domain.vo.order;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.*;

/**
 * 价格历史响应类
 * 封装价格历史相关响应数据
 */
@Builder
public record PriceHistoryVO(
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
        @JsonProperty(value = "previousPrice", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiMoney
        @DecimalMin("0.00")
        @DecimalMax("999999999.99")
        @Digits(integer = 9, fraction = 2)
        BigDecimal previousPrice,
        @JsonProperty(value = "newPrice", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiMoney
        @DecimalMin("0.00")
        @DecimalMax("999999999.99")
        @Digits(integer = 9, fraction = 2)
        BigDecimal newPrice,
        @JsonProperty(value = "priceVersion", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Min(1)
        @Max(2147483647)
        Integer priceVersion,
        @JsonProperty(value = "createdAt", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime createdAt) {}
