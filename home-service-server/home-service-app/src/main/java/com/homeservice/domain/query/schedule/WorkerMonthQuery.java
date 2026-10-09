package com.homeservice.domain.query.schedule;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

@Data
public class WorkerMonthQuery {

    @NotNull(message = MessageConstant.MONTH_REQUIRED)
    @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])$", message = MessageConstant.MONTH_INVALID)
    private String month; // 月份

}
