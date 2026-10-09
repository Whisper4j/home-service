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
 * 日历日期响应类
 * 封装日历日期相关响应数据
 */
@Builder
public record CalendarDayVO(
        @JsonProperty(value = "date", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        LocalDate date,
        @JsonProperty(value = "status", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        CalendarDayStatus status,
        @JsonProperty(value = "segments", required = true)
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
        @NotNull
        @Valid
        List<@NotNull @Valid SlotVO> segments) {}
