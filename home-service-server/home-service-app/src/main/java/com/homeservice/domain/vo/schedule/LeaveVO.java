package com.homeservice.domain.vo.schedule;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 请假响应类
 * 封装请假相关响应数据
 */
@Builder
public record LeaveVO(
        @JsonProperty(value = "id", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long id,
        @JsonProperty(value = "startTime", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime startTime,
        @JsonProperty(value = "endTime", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime endTime,
        @JsonProperty(value = "reason", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 300)
        @NotBlank
        String reason,
        @JsonProperty(value = "status", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        LeaveStatus status) {}
