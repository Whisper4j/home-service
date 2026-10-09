package com.homeservice.domain.query.schedule;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 服务人员月份查询类
 * 封装服务人员月份相关查询条件
 */
@Data
public class WorkerMonthQuery {

    @NotNull
    @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])$")
    private String month;

}
