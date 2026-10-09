package com.homeservice.domain.vo.order;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.domain.vo.dispatch.AssignmentVO;
import com.homeservice.domain.vo.payment.PaymentVO;
import com.homeservice.domain.vo.review.ReviewVO;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;
import java.util.List;

/**
 * 订单历史响应类
 * 封装订单历史相关响应数据
 */
@Builder
public record OrderHistoryVO(
        @JsonProperty(value = "payments", required = true)
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
        @NotNull
        @Valid
        List<@NotNull @Valid PaymentVO> payments,
        @JsonProperty(value = "priceHistory", required = true)
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
        @NotNull
        @Valid
        List<@NotNull @Valid PriceHistoryVO> priceHistory,
        @JsonProperty(value = "assignments", required = true)
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
        @NotNull
        @Valid
        List<@NotNull @Valid AssignmentVO> assignments,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @Valid
        ReviewVO review,
        @JsonProperty(value = "statusHistory", required = true)
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
        @NotNull
        @Valid
        List<@NotNull @Valid OrderStatusHistoryVO> statusHistory) {}
