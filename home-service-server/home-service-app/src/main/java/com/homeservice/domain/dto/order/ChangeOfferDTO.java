package com.homeservice.domain.dto.order;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.*;

/**
 * 调整优惠报价请求类
 * 接收调整优惠报价相关请求参数
 */
@Builder
public record ChangeOfferDTO(
        @JsonProperty(value = "newPrice", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiMoney
        @DecimalMin("0.00")
        @DecimalMax("999999999.99")
        @Digits(integer = 9, fraction = 2)
        BigDecimal newPrice,
        @JsonProperty(value = "expectedPrice", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiMoney
        @DecimalMin("0.00")
        @DecimalMax("999999999.99")
        @Digits(integer = 9, fraction = 2)
        BigDecimal expectedPrice,
        @JsonProperty(value = "priceVersion", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Min(1)
        @Max(2147483647)
        Integer priceVersion,
        @JsonProperty(value = "confirmSimulatedPayment", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        Boolean confirmSimulatedPayment) {}
