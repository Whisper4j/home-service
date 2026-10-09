package com.homeservice.domain.vo.catalog;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 服务项目响应类
 * 封装服务项目相关响应数据
 */
@Builder
public record ServiceItemVO(
        @JsonProperty(value = "id", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long id,
        @JsonProperty(value = "categoryId", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long categoryId,
        @JsonProperty(value = "name", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 60)
        @NotBlank
        String name,
        @JsonProperty(value = "serviceKind", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        ServiceKind serviceKind,
        @JsonProperty(value = "description", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 1000)
        @NotBlank
        String description,
        @JsonProperty(value = "status", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        CatalogStatus status) {}
