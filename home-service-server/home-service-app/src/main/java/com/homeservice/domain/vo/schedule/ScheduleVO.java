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

import com.homeservice.domain.dto.schedule.WorkIntervalDTO;
/** 对应 OpenAPI ScheduleVO；仅定义数据边界，不实现业务。 */
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
    List<@NotNull @Min(1) @Max(7) Integer> restWeekdays
) {}
