package com.homeservice.domain.dto.schedule;
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
/** 对应 OpenAPI ScheduleDTO；仅定义数据边界，不实现业务。 */
@Builder
public record ScheduleDTO(
    @JsonProperty(value = "intervals", required = true)
    @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 14)
    @Valid
    List<@NotNull @Valid WorkIntervalDTO> intervals,

    @JsonProperty(value = "restWeekdays", required = true)
    @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
    @NotNull
    @Size(min = 0, max = 7)
    @UniqueElements
    @Valid
    List<@NotNull @Min(1) @Max(7) Integer> restWeekdays
) {
    @AssertTrue(message = "工作区间不可重叠") @JsonIgnore
    public boolean isNonOverlapping() {
        if (intervals == null || intervals.stream().anyMatch(x -> x == null || x.start() == null || x.end() == null)) return true;
        var sorted = intervals.stream().sorted(java.util.Comparator.comparing(WorkIntervalDTO::start)).toList();
        for (int i=1; i<sorted.size(); i++) if (sorted.get(i).start().isBefore(sorted.get(i-1).end())) return false;
        return true;
    }
}
