package com.homeservice.domain.query.schedule;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

@Data
public class WorkerMonthQuery {

    @NotNull(message = "月份不能为空")
    @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])$", message = "月份格式不正确")
    private String month; // 月份

}
