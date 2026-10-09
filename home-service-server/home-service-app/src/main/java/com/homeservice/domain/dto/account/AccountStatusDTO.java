package com.homeservice.domain.dto.account;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 账号状态请求类
 * 接收账号状态相关请求参数
 */
@Builder
public record AccountStatusDTO(
        @JsonProperty(value = "status", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        AccountStatus status) {}
