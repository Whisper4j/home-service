package com.homeservice.domain.dto.catalog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class SkillDTO {

    @Size(max = 60, message = "名称长度不能超过60")
    @NotBlank(message = "名称不能为空")
    private String name; // 名称

    @Size(max = 300, message = "说明长度不能超过300")
    @NotBlank(message = "说明不能为空")
    private String description; // 说明
}
