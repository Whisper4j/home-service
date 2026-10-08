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

import com.homeservice.domain.vo.schedule.CalendarDayVO;
import com.homeservice.domain.vo.schedule.ScheduleVO;
/** 返回所选自然月与未来30天（含今天）交集；from/to为整个可查看窗口端点，days可为空。不扩大客户预约窗口，不增加逐日排班模型。 */
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
    List<@NotNull @Valid CalendarDayVO> days
) {}
