package com.homeservice.domain.vo.schedule;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;
import com.homeservice.enums.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.UniqueElements;
import lombok.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.homeservice.domain.vo.schedule.SlotVO;
/** segments为08:00—22:00内按时间排序的合并区间：仅相邻status、orderId、assignmentId、bookingType均相同才合并。包含NON_WORKING、AVAILABLE、SERVICE、BUFFER、LEAVE。日期主状态优先未设置、已有服务/缓冲、请假、工作时间内空闲、休息，混合情况以区间为准。缓冲不可接单；空闲不保证容纳任意服务。 */
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
    List<@NotNull @Valid SlotVO> segments
) {}
