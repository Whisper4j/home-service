package com.homeservice.domain.vo.order;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.*;

/**
 * 订单地址快照响应类
 * 封装订单地址快照相关响应数据
 */
@Builder
public record OrderAddressSnapshotVO(
        @JsonProperty(value = "contactName", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 40)
        @NotBlank
        String contactName,
        @JsonProperty(value = "contactPhone", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 11)
        @NotBlank
        @Pattern(regexp = "^1[0-9]{10}$")
        String contactPhone,
        @JsonProperty(value = "provinceCode", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 6)
        @NotBlank
        String provinceCode,
        @JsonProperty(value = "provinceName", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 40)
        @NotBlank
        String provinceName,
        @JsonProperty(value = "cityCode", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 6)
        @NotBlank
        String cityCode,
        @JsonProperty(value = "cityName", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 40)
        @NotBlank
        String cityName,
        @JsonProperty(value = "districtCode", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 6)
        @NotBlank
        String districtCode,
        @JsonProperty(value = "districtName", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 40)
        @NotBlank
        String districtName,
        @JsonProperty(value = "detail", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 200)
        @NotBlank
        String detail,
        @JsonProperty(value = "longitude", required = true)
        @DecimalMin("-180")
        @DecimalMax("180")
        BigDecimal longitude,
        @JsonProperty(value = "latitude", required = true)
        @DecimalMin("-90")
        @DecimalMax("90")
        BigDecimal latitude,
        @JsonProperty(value = "isDefault", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        Boolean isDefault) {}
