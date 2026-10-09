package com.homeservice.domain.vo.order;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 开始码响应类
 * 封装开始码相关响应数据
 */
@Builder
public record StartCodeVO(
        @JsonProperty(value = "startCode", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 6)
        @NotBlank
        @Pattern(regexp = "^[0-9]{6}$")
        String startCode) {}
