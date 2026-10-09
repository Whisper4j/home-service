package com.homeservice.domain.vo.dispatch;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 分配响应类
 * 封装分配相关响应数据
 */
@Builder
public record AssignmentVO(
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
        @JsonProperty(value = "workerId", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long workerId,
        @JsonProperty(value = "workerName", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 40)
        @NotBlank
        String workerName,
        @JsonProperty(value = "bookingType", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        BookingType bookingType,
        @JsonProperty(value = "status", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        AssignmentStatus status,
        @JsonProperty(value = "assignedAt", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime assignedAt,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        OffsetDateTime releasedAt,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @Size(min = 1, max = 300)
        String releaseReason,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        OffsetDateTime finishedAt) {}
