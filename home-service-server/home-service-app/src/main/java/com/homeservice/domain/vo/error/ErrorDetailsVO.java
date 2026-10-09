package com.homeservice.domain.vo.error;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

import org.hibernate.validator.constraints.UniqueElements;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

/**
 * 错误详情响应类
 * 封装错误详情相关响应数据
 */
@Builder
public record ErrorDetailsVO(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @Valid
        List<@NotNull @Valid FieldErrorVO> fieldErrors,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @ApiMoney
        @DecimalMin("0.00")
        @DecimalMax("999999999.99")
        @Digits(integer = 9, fraction = 2)
        BigDecimal currentPrice,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @Min(1)
        @Max(2147483647)
        Integer priceVersion,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        OrderStatus currentStatus,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @UniqueElements
        @ApiIds
        @Valid
        List<@NotNull @Positive Long> conflictingOrderIds) {}
