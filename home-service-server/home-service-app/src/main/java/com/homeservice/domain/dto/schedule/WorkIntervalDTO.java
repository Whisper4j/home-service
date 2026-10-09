package com.homeservice.domain.dto.schedule;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

import lombok.Data;

@Data
public class WorkIntervalDTO {

    @NotNull(message = "开始时间不能为空")
    private LocalTime start; // 开始时间

    @NotNull(message = "结束时间不能为空")
    private LocalTime end; // 结束时间

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
