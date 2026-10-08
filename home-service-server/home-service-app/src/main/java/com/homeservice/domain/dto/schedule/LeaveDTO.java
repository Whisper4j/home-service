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


/** 30 分钟对齐、start < end、至少提前 2 小时且位于未来 30 天槽窗口；不得覆盖 SERVICE/BUFFER。 */
@Builder
public record LeaveDTO(
    @JsonProperty(value = "startTime", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime startTime,

    @JsonProperty(value = "endTime", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime endTime,

    @JsonProperty(value = "reason", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 300)
    @NotBlank
    String reason
) {
    @AssertTrue(message = "请假起止时间须按半小时对齐且开始早于结束") @JsonIgnore
    public boolean isValidRange() { return startTime == null || endTime == null || startTime.isBefore(endTime) && startTime.getMinute()%30==0 && endTime.getMinute()%30==0 && startTime.getSecond()==0 && endTime.getSecond()==0; }
}
