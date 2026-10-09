package com.homeservice.domain.vo.schedule;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;
import java.util.List;

/**
 * 服务人员日历响应类
 * 封装服务人员日历相关响应数据
 */
@Builder
public record WorkerCalendarVO(
        @JsonProperty(value = "from", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        LocalDate from,
        @JsonProperty(value = "to", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        LocalDate to,
        @JsonProperty(value = "schedule", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Valid
        ScheduleVO schedule,
        @JsonProperty(value = "days", required = true)
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
        @NotNull
        @Valid
        List<@NotNull @Valid CalendarDayVO> days) {}
