package com.homeservice.domain.dto.account;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 登录请求类
 * 接收登录相关请求参数
 */
@Builder
public record LoginDTO(
        @JsonProperty(value = "username", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 32)
        @NotBlank
        @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{2,31}$")
        String username,
        @JsonProperty(value = "password", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 8, max = 72)
        @Utf8Password
        @JsonDeserialize(using = PasswordDeserializer.class)
        String password) {
    /**
     * 生成对象的安全文本描述
     */
    @Override
    public String toString() {
        return "LoginDTO[credentials=REDACTED]";
    }
}
