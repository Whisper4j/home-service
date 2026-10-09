package com.homeservice.domain.dto.catalog;

import com.homeservice.common.constant.MessageConstant;

import com.homeservice.enums.CatalogStatus;
import com.homeservice.enums.ServiceKind;
import com.homeservice.handler.json.ApiId;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class ServiceItemDTO {

    @NotNull(message = MessageConstant.CATALOG_SELECTION_INVALID)
    @ApiId
    @Positive(message = MessageConstant.CATALOG_SELECTION_INVALID)
    private Long categoryId; // 分类ID

    @Size(max = 60, message = MessageConstant.NAME_TOO_LONG_60)
    @NotBlank(message = MessageConstant.NAME_REQUIRED)
    private String name; // 名称

    @NotNull(message = MessageConstant.SERVICE_TYPE_REQUIRED)
    private ServiceKind serviceKind; // 服务类型

    @Size(max = 1000, message = MessageConstant.DESCRIPTION_TOO_LONG_1000)
    @NotBlank(message = MessageConstant.DESCRIPTION_REQUIRED)
    private String description; // 说明

    @NotNull(message = MessageConstant.CATALOG_STATUS_REQUIRED)
    private CatalogStatus status; // 状态
}
