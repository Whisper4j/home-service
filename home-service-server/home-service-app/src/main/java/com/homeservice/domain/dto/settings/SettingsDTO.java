package com.homeservice.domain.dto.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SettingsDTO {

    @NotNull(message = "最早预约小时数不能为空")
    @Min(value = 2, message = "最早预约小时数不能小于2")
    @Max(value = 24, message = "最早预约小时数不能大于24")
    private Integer earliestHours; // 最早预约小时数

    @NotNull(message = "最远预约天数不能为空")
    @Min(value = 1, message = "最远预约天数不能小于1")
    @Max(value = 7, message = "最远预约天数不能大于7")
    private Integer latestDays; // 最远预约天数

    @AssertTrue(message = "最早预约时间必须小于最远预约窗口")
    @JsonIgnore
    public boolean isValidWindow() {
        return earliestHours == null || latestDays == null || earliestHours < latestDays * 24;
    }
}
