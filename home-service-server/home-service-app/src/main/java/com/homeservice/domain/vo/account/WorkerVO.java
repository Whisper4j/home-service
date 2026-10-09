package com.homeservice.domain.vo.account;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

import org.hibernate.validator.constraints.UniqueElements;

import java.time.*;
import java.util.List;

/**
 * 服务人员响应类
 * 封装服务人员相关响应数据
 */
@Builder
public record WorkerVO(
        @JsonProperty(value = "id", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long id,
        @JsonProperty(value = "accountId", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long accountId,
        @JsonProperty(value = "username", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 32)
        @NotBlank
        String username,
        @JsonProperty(value = "status", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        AccountStatus status,
        @JsonProperty(value = "displayName", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 40)
        @NotBlank
        String displayName,
        @JsonProperty(value = "phone", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 11)
        @NotBlank
        @Pattern(regexp = "^1[0-9]{10}$")
        String phone,
        @JsonProperty(value = "cityCode", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 6)
        @NotBlank
        String cityCode,
        @JsonProperty(value = "skillIds", required = true)
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 2147483647)
        @UniqueElements
        @ApiIds
        @Valid
        List<@NotNull @Positive Long> skillIds,
        @JsonProperty(value = "dispatchEnabled", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        Boolean dispatchEnabled,
        @JsonProperty(value = "cityName", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 40)
        @NotBlank
        String cityName) {}
