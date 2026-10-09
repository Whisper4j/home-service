package com.homeservice.domain.dto.review;

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
 * 评价请求类
 * 接收评价相关请求参数
 */
@Builder
public record ReviewDTO(
        @JsonProperty(value = "score", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Min(1)
        @Max(5)
        Integer score,
        @JsonProperty(value = "tags", required = true)
        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
        @NotNull
        @Size(min = 0, max = 3)
        @UniqueElements
        @Valid
        List<@NotNull ReviewTag> tags,
        @JsonProperty(value = "content", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 0, max = 500)
        String content) {}
