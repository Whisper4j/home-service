package com.homeservice.domain.dto.schedule;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 请假请求类
 * 接收请假相关请求参数
 */
@Builder
public record LeaveDTO(
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
        String reason) {
    /**
     * 校验开始值是否早于结束值
     */
    @AssertTrue(message = "请假起止时间须按半小时对齐且开始早于结束")
    @JsonIgnore
    public boolean isValidRange() {
        return startTime == null
                || endTime == null
                || startTime.isBefore(endTime)
                        && startTime.getMinute() % 30 == 0
                        && endTime.getMinute() % 30 == 0
                        && startTime.getSecond() == 0
                        && endTime.getSecond() == 0;
    }
}
