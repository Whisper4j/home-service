package com.homeservice.domain.vo.account;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 账号响应类
 * 封装账号相关响应数据
 */
@Builder
public record AccountVO(
        @JsonProperty(value = "id", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @ApiId
        @Positive
        Long id,
        @JsonProperty(value = "username", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 32)
        @NotBlank
        String username,
        @JsonProperty(value = "displayName", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 40)
        @NotBlank
        String displayName,
        @JsonProperty(value = "role", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        Role role,
        @JsonProperty(value = "status", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        AccountStatus status,
        @JsonProperty(value = "phone", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 11)
        @NotBlank
        String phone) {}
