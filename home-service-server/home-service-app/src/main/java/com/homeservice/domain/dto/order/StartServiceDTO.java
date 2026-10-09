package com.homeservice.domain.dto.order;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class StartServiceDTO {

    @Size(max = 6, message = MessageConstant.START_CODE_INVALID)
    @NotBlank(message = MessageConstant.START_CODE_REQUIRED)
    @Pattern(regexp = "^[0-9]{6}$", message = MessageConstant.START_CODE_INVALID)
    private String startCode; // 开始编码
}
