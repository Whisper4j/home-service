package com.homeservice.domain.vo.schedule;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.domain.dto.schedule.WorkIntervalDTO;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

import org.hibernate.validator.constraints.UniqueElements;

import java.time.*;
import java.util.List;

/**
 * 排班响应类
 * 封装排班相关响应数据
 */
@Builder
public record ScheduleVO(
        @JsonProperty(value = "configured", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        Boolean configured,
        @JsonProperty(value = "intervals", required = true)
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
        @NotNull
        @Valid
        List<@NotNull @Valid WorkIntervalDTO> intervals,
        @JsonProperty(value = "restWeekdays", required = true)
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
        @NotNull
        @UniqueElements
        @Valid
        List<@NotNull @Min(1) @Max(7) Integer> restWeekdays) {}
