package com.homeservice.domain.vo.dispatch;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 派单尝试响应类
 * 封装派单尝试相关响应数据
 */
@Builder
public record DispatchAttemptVO(
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
        @ApiId
        @Positive
        Long workerId,
        @JsonProperty(value = "result", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        DispatchAttemptResult result,
        @JsonProperty(value = "reason", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 300)
        @NotBlank
        String reason,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @Min(0)
        @Max(2147483647)
        Integer serviceMinutes,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @Min(0)
        @Max(2147483647)
        Integer orderCount,
        @JsonProperty(value = "createdAt", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime createdAt) {}
