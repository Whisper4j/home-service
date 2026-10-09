package com.homeservice.domain.vo.schedule;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 时间槽响应类
 * 封装时间槽相关响应数据
 */
@Builder
public record SlotVO(
        @JsonProperty(value = "startTime", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime startTime,
        @JsonProperty(value = "endTime", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime endTime,
        @JsonProperty(value = "status", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        SlotStatus status,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        BookingType bookingType,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @ApiId
        @Positive
        Long assignmentId,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @ApiId
        @Positive
        Long orderId) {}
