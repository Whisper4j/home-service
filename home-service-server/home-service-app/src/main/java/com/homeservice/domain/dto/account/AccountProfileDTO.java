package com.homeservice.domain.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class AccountProfileDTO {

    @NotBlank(message = "显示名称不能为空")
    @Size(max = 40, message = "显示名称不能超过40个字符")
    private String displayName; // 显示名称

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[0-9]{10}$", message = "手机号格式不正确")
    private String phone; // 手机号
}
