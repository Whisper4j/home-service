package com.homeservice.domain.dto.account;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 服务人员联系方式请求类
 * 接收服务人员联系方式相关请求参数
 */
@Builder
public record WorkerContactDTO(
        @JsonProperty(value = "phone", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 11)
        @NotBlank
        @Pattern(regexp = "^1[0-9]{10}$")
        String phone) {}
