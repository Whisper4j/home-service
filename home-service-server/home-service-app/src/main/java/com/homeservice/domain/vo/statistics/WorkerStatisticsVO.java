package com.homeservice.domain.vo.statistics;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 服务人员统计响应类
 * 封装服务人员统计相关响应数据
 */
@Builder
public record WorkerStatisticsVO(
        @JsonProperty(value = "asOf", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime asOf,
        @JsonProperty(value = "today", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        LocalDate today,
        @JsonProperty(value = "month", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])$")
        String month,
        @JsonProperty(value = "todayPendingCount", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Min(0)
        @Max(2147483647)
        Integer todayPendingCount,
        @JsonProperty(value = "monthCompletedCount", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Min(0)
        @Max(2147483647)
        Integer monthCompletedCount,
        @JsonProperty(value = "totalCompletedCount", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Min(0)
        @Max(2147483647)
        Integer totalCompletedCount,
        @JsonProperty(value = "monthBookedMinutes", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Min(0)
        @Max(2147483647)
        Integer monthBookedMinutes,
        @JsonProperty(value = "totalBookedMinutes", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Min(0)
        @Max(2147483647)
        Integer totalBookedMinutes) {}
