package com.homeservice.domain.dto.settings;
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


/** 当前仅预约窗口可配置，其他核心规则只读。只影响新预约，已创建订单的截止时间不改变。 */
@Builder
public record SettingsDTO(
    @JsonProperty(value = "earliestHours", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(2)
    @Max(24)
    Integer earliestHours,

    @JsonProperty(value = "latestDays", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(1)
    @Max(7)
    Integer latestDays
) {
    @AssertTrue(message = "最早预约时间必须小于最远预约窗口") @JsonIgnore
    public boolean isValidWindow() { return earliestHours == null || latestDays == null || earliestHours < latestDays*24; }
}
