package com.homeservice.domain.vo.catalog;

import com.homeservice.enums.CatalogStatus;
import com.homeservice.enums.ServiceKind;
import com.homeservice.handler.json.ApiId;

import lombok.Data;

@Data
public class ServiceItemVO {

    @ApiId
    private Long id; // 主键ID

    @ApiId
    private Long categoryId; // 分类ID

    private String name; // 名称

    private ServiceKind serviceKind; // 服务类型

    private String description; // 说明

    private CatalogStatus status; // 状态
}
