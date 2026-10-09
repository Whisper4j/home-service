package com.homeservice.domain.dto.account;

import com.homeservice.common.constant.MessageConstant;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.homeservice.handler.json.PasswordDeserializer;
import com.homeservice.validation.Utf8Password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterDTO {

    @Size(max = 32, message = MessageConstant.USERNAME_TOO_LONG)
    @NotBlank(message = MessageConstant.USERNAME_REQUIRED)
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{2,31}$", message = MessageConstant.USERNAME_INVALID)
    private String username; // 用户名

    @NotNull(message = MessageConstant.PASSWORD_REQUIRED)
    @Size(min = 8, max = 72, message = MessageConstant.PASSWORD_LENGTH_INVALID)
    @JsonDeserialize(using = PasswordDeserializer.class)
    private String password; // 密码

    @Size(max = 40, message = MessageConstant.DISPLAY_NAME_TOO_LONG)
    @NotBlank(message = MessageConstant.DISPLAY_NAME_REQUIRED)
    private String displayName; // 显示名称

    @Size(max = 11, message = MessageConstant.PHONE_INVALID)
    @NotBlank(message = MessageConstant.PHONE_REQUIRED)
    @Pattern(regexp = "^1[0-9]{10}$", message = MessageConstant.PHONE_INVALID)
    private String phone; // 手机号

    @Override
    public String toString() {
        return "RegisterDTO[credentials=REDACTED]";
    }
}
