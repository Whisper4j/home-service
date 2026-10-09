package com.homeservice.domain.dto.order;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class CancelOrderDTO {

    @Size(max = 300, message = MessageConstant.CANCEL_REASON_TOO_LONG)
    @NotBlank(message = MessageConstant.CANCEL_REASON_REQUIRED)
    private String reason; // 原因
}
