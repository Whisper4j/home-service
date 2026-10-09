package com.homeservice.domain.dto.catalog;

import com.homeservice.enums.CatalogStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class CategoryDTO {

    @Size(max = 60, message = "名称长度不能超过60")
    @NotBlank(message = "名称不能为空")
    private String name; // 名称

    @NotNull(message = "排序不能为空")
    @Min(value = 0, message = "排序不能小于0")
    @Max(value = 9999, message = "排序不能大于9999")
    private Integer sort; // 排序

    @NotNull(message = "状态不能为空")
    private CatalogStatus status; // 状态
}
