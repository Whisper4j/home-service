package com.homeservice.domain.dto.catalog;

import com.homeservice.common.constant.MessageConstant;

import com.homeservice.enums.CatalogStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class CategoryDTO {

    @Size(max = 60, message = MessageConstant.NAME_TOO_LONG_60)
    @NotBlank(message = MessageConstant.NAME_REQUIRED)
    private String name; // 名称

    @NotNull(message = MessageConstant.SORT_NO_REQUIRED)
    @Min(value = 0, message = MessageConstant.SORT_NO_INVALID)
    @Max(value = 9999, message = MessageConstant.SORT_NO_INVALID)
    private Integer sort; // 排序

    @NotNull(message = MessageConstant.CATALOG_STATUS_REQUIRED)
    private CatalogStatus status; // 状态
}
