package com.homeservice.domain.dto.account;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 账号资料请求类
 * 接收账号资料相关请求参数
 */
@Builder
public record AccountProfileDTO(
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
        String phone) {}
