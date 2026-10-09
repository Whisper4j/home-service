package com.homeservice.domain.vo.order;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 变更响应类
 * 封装变更相关响应数据
 */
@Builder
public record MutationVO(
        @JsonProperty(value = "success", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @AssertTrue
        Boolean success) {}
