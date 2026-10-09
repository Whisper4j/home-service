package com.homeservice.domain.dto.settings;

import com.homeservice.common.constant.MessageConstant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SettingsDTO {

    @NotNull(message = MessageConstant.EARLIEST_HOURS_REQUIRED)
    @Min(value = 2, message = MessageConstant.EARLIEST_HOURS_INVALID)
    @Max(value = 24, message = MessageConstant.EARLIEST_HOURS_INVALID)
    private Integer earliestHours; // 最早预约小时数

    @NotNull(message = MessageConstant.LATEST_DAYS_REQUIRED)
    @Min(value = 1, message = MessageConstant.LATEST_DAYS_INVALID)
    @Max(value = 7, message = MessageConstant.LATEST_DAYS_INVALID)
    private Integer latestDays; // 最远预约天数

    @AssertTrue(message = MessageConstant.BOOKING_WINDOW_INVALID)
    @JsonIgnore
    public boolean isValidWindow() {
        return earliestHours == null || latestDays == null || earliestHours < latestDays * 24;
    }
}
