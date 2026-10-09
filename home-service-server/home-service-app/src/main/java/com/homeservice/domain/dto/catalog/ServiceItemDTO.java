package com.homeservice.domain.dto.catalog;

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

    @NotNull(message = "分类ID不能为空")
    @ApiId
    @Positive(message = "分类ID必须大于0")
    private Long categoryId; // 分类ID

    @Size(max = 60, message = "名称长度不能超过60")
    @NotBlank(message = "名称不能为空")
    private String name; // 名称

    @NotNull(message = "服务类型不能为空")
    private ServiceKind serviceKind; // 服务类型

    @Size(max = 1000, message = "说明长度不能超过1000")
    @NotBlank(message = "说明不能为空")
    private String description; // 说明

    @NotNull(message = "状态不能为空")
    private CatalogStatus status; // 状态
}
