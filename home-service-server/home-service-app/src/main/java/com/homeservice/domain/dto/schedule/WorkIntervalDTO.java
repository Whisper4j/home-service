package com.homeservice.domain.dto.schedule;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 工作时段请求类
 * 接收工作时段相关请求参数
 */
@Builder
public record WorkIntervalDTO(
        @JsonProperty(value = "start", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        LocalTime start,
        @JsonProperty(value = "end", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        LocalTime end) {
    /**
     * 校验工作时段是否合法
     */
    @AssertTrue(message = "工作区间必须在 08:00—22:00 且开始早于结束")
    @JsonIgnore
    public boolean isValidInterval() {
        return start == null
                || end == null
                || !start.isBefore(LocalTime.of(8, 0))
                        && !end.isAfter(LocalTime.of(22, 0))
                        && start.isBefore(end);
    }
}
