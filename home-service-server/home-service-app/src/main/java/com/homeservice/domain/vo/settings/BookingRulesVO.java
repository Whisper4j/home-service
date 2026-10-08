package com.homeservice.domain.vo.settings;
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


/** 对应 OpenAPI BookingRulesVO；仅定义数据边界，不实现业务。 */
@Builder
public record BookingRulesVO(
    @JsonProperty(value = "cityCode", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 6)
    @NotBlank
    String cityCode,

    @JsonProperty(value = "workStart", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    LocalTime workStart,

    @JsonProperty(value = "workEnd", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    LocalTime workEnd,

    @JsonProperty(value = "slotMinutes", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(30)
    @Max(30)
    Integer slotMinutes,

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
    Integer latestDays,

    @JsonProperty(value = "offerLeadHours", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(12)
    @Max(12)
    Integer offerLeadHours,

    @JsonProperty(value = "offerWaitMinutes", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(120)
    @Max(120)
    Integer offerWaitMinutes,

    @JsonProperty(value = "offerSafetyHours", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(6)
    @Max(6)
    Integer offerSafetyHours,

    @JsonProperty(value = "paymentTimeoutMinutes", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(15)
    @Max(15)
    Integer paymentTimeoutMinutes,

    @JsonProperty(value = "dispatchWaitMinutes", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(5)
    @Max(5)
    Integer dispatchWaitMinutes,

    @JsonProperty(value = "dispatchScanSeconds", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(30)
    @Max(30)
    Integer dispatchScanSeconds,

    @JsonProperty(value = "standardBufferMinutes", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(120)
    @Max(120)
    Integer standardBufferMinutes,

    @JsonProperty(value = "offerBufferMinutes", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(60)
    @Max(60)
    Integer offerBufferMinutes,

    @JsonProperty(value = "autoConfirmHours", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(24)
    @Max(24)
    Integer autoConfirmHours,

    @JsonProperty(value = "priceStep", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiMoney
    @DecimalMin("0.00")
    @DecimalMax("999999999.99")
    @Digits(integer = 9, fraction = 2)
    BigDecimal priceStep,

    @JsonProperty(value = "cityName", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 40)
    @NotBlank
    String cityName,

    @JsonProperty(value = "scheduleWindowDays", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(30)
    @Max(30)
    Integer scheduleWindowDays,

    @JsonProperty(value = "leaveLeadHours", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(2)
    @Max(2)
    Integer leaveLeadHours,

    @JsonProperty(value = "sceneImageMaxCount", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(3)
    @Max(3)
    Integer sceneImageMaxCount,

    @JsonProperty(value = "sceneImageMaxBytes", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(5242880)
    @Max(5242880)
    Integer sceneImageMaxBytes,

    @JsonProperty(value = "sceneImageMimeTypes", required = true)
    @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Valid
    List<@NotNull ImageMimeType> sceneImageMimeTypes
) {}
