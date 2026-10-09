package com.homeservice.domain.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class StartServiceDTO {

    @Size(max = 6, message = "开始编码长度不能超过6")
    @NotBlank(message = "开始编码不能为空")
    @Pattern(regexp = "^[0-9]{6}$", message = "开始编码格式不正确")
    private String startCode; // 开始编码
}
