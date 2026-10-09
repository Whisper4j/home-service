package com.homeservice.domain.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class CancelOrderDTO {

    @Size(max = 300, message = "原因长度不能超过300")
    @NotBlank(message = "原因不能为空")
    private String reason; // 原因
}
