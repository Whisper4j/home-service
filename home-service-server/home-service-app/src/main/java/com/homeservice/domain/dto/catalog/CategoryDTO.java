package com.homeservice.domain.dto.catalog;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 分类请求类
 * 接收分类相关请求参数
 */
@Builder
public record CategoryDTO(
        @JsonProperty(value = "name", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 60)
        @NotBlank
        String name,
        @JsonProperty(value = "sort", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Min(0)
        @Max(9999)
        Integer sort,
        @JsonProperty(value = "status", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        CatalogStatus status) {}
