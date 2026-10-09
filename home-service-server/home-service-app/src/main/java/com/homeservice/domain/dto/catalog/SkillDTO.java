package com.homeservice.domain.dto.catalog;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class SkillDTO {

    @Size(max = 60, message = MessageConstant.NAME_TOO_LONG_60)
    @NotBlank(message = MessageConstant.NAME_REQUIRED)
    private String name; // 名称

    @Size(max = 300, message = MessageConstant.DESCRIPTION_TOO_LONG_300)
    @NotBlank(message = MessageConstant.DESCRIPTION_REQUIRED)
    private String description; // 说明
}
