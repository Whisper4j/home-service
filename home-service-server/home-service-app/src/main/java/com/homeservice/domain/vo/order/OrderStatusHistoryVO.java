package com.homeservice.domain.vo.order;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 订单状态历史响应类
 * 封装订单状态历史相关响应数据
 */
@Builder
public record OrderStatusHistoryVO(
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
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        OrderStatus fromStatus,
        @JsonProperty(value = "toStatus", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OrderStatus toStatus,
        @JsonProperty(value = "actorType", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        ActorType actorType,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @ApiId
        @Positive
        Long actorId,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        Role actorRole,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @Size(min = 1, max = 300)
        String reason,
        @JsonProperty(value = "createdAt", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime createdAt) {}
