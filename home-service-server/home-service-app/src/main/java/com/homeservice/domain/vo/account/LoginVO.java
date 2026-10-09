package com.homeservice.domain.vo.account;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 登录响应类
 * 封装登录相关响应数据
 */
@Builder
public record LoginVO(
        @JsonProperty(value = "accessToken", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Size(min = 1, max = 2048)
        @NotBlank
        String accessToken,
        @JsonProperty(value = "tokenType", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        TokenType tokenType,
        @JsonProperty(value = "expiresAt", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        OffsetDateTime expiresAt,
        @JsonProperty(value = "account", required = true)
        @JsonSetter(nulls = Nulls.FAIL)
        @NotNull
        @Valid
        AccountVO account) {
    /**
     * 生成对象的安全文本描述
     */
    @Override
    public String toString() {
        return "LoginVO[credentials=REDACTED]";
    }
}
