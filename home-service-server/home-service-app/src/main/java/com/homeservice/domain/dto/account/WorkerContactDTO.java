package com.homeservice.domain.dto.account;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class WorkerContactDTO {

    @Size(max = 11, message = MessageConstant.PHONE_INVALID)
    @NotBlank(message = MessageConstant.PHONE_REQUIRED)
    @Pattern(regexp = "^1[0-9]{10}$", message = MessageConstant.PHONE_INVALID)
    private String phone; // 手机号
}
