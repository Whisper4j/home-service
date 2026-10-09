package com.homeservice.domain.vo.region;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;
import java.util.List;

/**
 * 地区响应类
 * 封装地区相关响应数据
 */
@Builder
public record RegionVO(
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
        @JsonProperty(value = "districts", required = true)
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
        @NotNull
        @Valid
        List<@NotNull @Valid DistrictVO> districts) {}
