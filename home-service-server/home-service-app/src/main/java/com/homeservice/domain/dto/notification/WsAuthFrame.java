package com.homeservice.domain.dto.notification;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class WsAuthFrame {

    @NotNull(message = MessageConstant.AUTH_TYPE_INVALID)
    @Pattern(regexp = "AUTH", message = MessageConstant.AUTH_TYPE_INVALID)
    private String type; // 类型

    @Size(max = 2048, message = MessageConstant.TOKEN_TOO_LONG)
    @NotBlank(message = MessageConstant.TOKEN_REQUIRED)
    private String accessToken; // 访问令牌

    @Override
    public String toString() {
        return "WsAuthFrame[credentials=REDACTED]";
    }
}
