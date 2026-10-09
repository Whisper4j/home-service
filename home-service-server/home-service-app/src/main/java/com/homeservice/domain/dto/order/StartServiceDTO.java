package com.homeservice.domain.dto.order;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 开始服务请求类
 * 接收开始服务相关请求参数
 */
@Builder
public record StartServiceDTO(
        @JsonProperty(value = "startCode", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 6)
        @NotBlank
        @Pattern(regexp = "^[0-9]{6}$")
        String startCode) {}
