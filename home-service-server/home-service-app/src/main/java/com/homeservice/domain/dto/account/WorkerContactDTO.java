package com.homeservice.domain.dto.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class WorkerContactDTO {

    @Size(max = 11, message = "手机号长度不能超过11")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[0-9]{10}$", message = "手机号格式不正确")
    private String phone; // 手机号
}
