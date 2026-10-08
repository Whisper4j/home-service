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


/** 08:00 <= start < end <= 22:00，区间不可重叠，允许相邻。 */
@Builder
public record WorkIntervalDTO(
    @JsonProperty(value = "start", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    LocalTime start,

    @JsonProperty(value = "end", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    LocalTime end
) {
    @AssertTrue(message = "工作区间必须在 08:00—22:00 且开始早于结束") @JsonIgnore
    public boolean isValidInterval() { return start == null || end == null || !start.isBefore(LocalTime.of(8,0)) && !end.isAfter(LocalTime.of(22,0)) && start.isBefore(end); }
}
