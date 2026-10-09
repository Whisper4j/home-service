package com.homeservice.domain.dto.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class WsAuthFrame {

    @NotNull(message = "类型不能为空")
    @Pattern(regexp = "AUTH", message = "类型格式不正确")
    private String type; // 类型

    @Size(max = 2048, message = "访问令牌长度不能超过2048")
    @NotBlank(message = "访问令牌不能为空")
    private String accessToken; // 访问令牌

    @Override
    public String toString() {
        return "WsAuthFrame[credentials=REDACTED]";
    }
}
