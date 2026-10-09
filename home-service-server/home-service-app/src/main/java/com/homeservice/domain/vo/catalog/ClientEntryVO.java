package com.homeservice.domain.vo.catalog;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 客户端入口响应类
 * 封装客户端入口相关响应数据
 */
@Builder
public record ClientEntryVO(
        @JsonProperty(value = "code", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 64)
        @NotBlank
        @Pattern(regexp = "^[A-Z][A-Z0-9_]{0,63}$")
        String code,
        @JsonProperty(value = "serviceKind", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        ServiceKind serviceKind,
        @JsonProperty(value = "groupCode", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 64)
        @NotBlank
        @Pattern(regexp = "^[A-Z][A-Z0-9_]{0,63}$")
        String groupCode,
        @JsonProperty(value = "groupName", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 80)
        @NotBlank
        String groupName,
        @JsonProperty(value = "groupDescription", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 0, max = 1000)
        String groupDescription,
        @JsonProperty(value = "groupSort", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        Integer groupSort,
        @JsonProperty(value = "name", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 80)
        @NotBlank
        String name,
        @JsonProperty(value = "description", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 0, max = 2000)
        String description,
        @JsonProperty(value = "sort", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        Integer sort,
        @JsonProperty(value = "available", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        Boolean available,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @Size(min = 1, max = 300)
        String unavailableReason,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @RejectExplicitNull
        @Valid
        SkuVO sku) {}
