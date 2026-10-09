package com.homeservice.domain.dto.schedule;

import com.homeservice.common.constant.MessageConstant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

import lombok.Data;

@Data
public class WorkIntervalDTO {

    @NotNull(message = MessageConstant.START_TIME_REQUIRED)
    private LocalTime start; // 开始时间

    @NotNull(message = MessageConstant.END_TIME_REQUIRED)
    private LocalTime end; // 结束时间

    @AssertTrue(message = MessageConstant.WORK_INTERVAL_INVALID)
    @JsonIgnore
    public boolean isValidInterval() {
        return start == null
                || end == null
                || !start.isBefore(LocalTime.of(8, 0))
                        && !end.isAfter(LocalTime.of(22, 0))
                        && start.isBefore(end);
    }
}
