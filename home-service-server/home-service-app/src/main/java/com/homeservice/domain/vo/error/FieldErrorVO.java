package com.homeservice.domain.vo.error;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 字段错误响应类
 * 封装字段错误相关响应数据
 */
@Builder
public record FieldErrorVO(
        @JsonProperty(value = "field", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 100)
        @NotBlank
        String field,
        @JsonProperty(value = "message", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 300)
        @NotBlank
        String message) {}
