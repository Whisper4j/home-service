package com.homeservice.domain.vo.catalog;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 分类响应类
 * 封装分类相关响应数据
 */
@Builder
public record CategoryVO(
        @JsonProperty(value = "id", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long id,
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
