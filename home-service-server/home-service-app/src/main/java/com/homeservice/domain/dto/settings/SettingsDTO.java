package com.homeservice.domain.dto.settings;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 设置请求类
 * 接收设置相关请求参数
 */
@Builder
public record SettingsDTO(
        @JsonProperty(value = "earliestHours", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Min(2)
        @Max(24)
        Integer earliestHours,
        @JsonProperty(value = "latestDays", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Min(1)
        @Max(7)
        Integer latestDays) {
    /**
     * 校验预约时间窗口是否合法
     */
    @AssertTrue(message = "最早预约时间必须小于最远预约窗口")
    @JsonIgnore
    public boolean isValidWindow() {
        return earliestHours == null || latestDays == null || earliestHours < latestDays * 24;
    }
}
