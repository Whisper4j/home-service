package com.homeservice.domain.dto.account;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.homeservice.handler.json.PasswordDeserializer;
import com.homeservice.validation.Utf8Password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class LoginDTO {

    @Size(max = 32, message = "用户名长度不能超过32")
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{2,31}$", message = "用户名格式不正确")
    private String username; // 用户名

    @NotNull(message = "密码不能为空")
    @Size(min = 8, max = 72, message = "密码长度必须在8到72之间")
    @Utf8Password(message = "密码的UTF-8编码不能超过72字节")
    @JsonDeserialize(using = PasswordDeserializer.class)
    private String password; // 密码

    @Override
    public String toString() {
        return "LoginDTO[credentials=REDACTED]";
    }
}
