package com.homeservice.domain.vo.catalog;

import com.homeservice.enums.CatalogStatus;
import com.homeservice.handler.json.ApiId;

import lombok.Data;

@Data
public class CategoryVO {

    @ApiId
    private Long id; // 主键ID

    private String name; // 名称

    private Integer sort; // 排序

    private CatalogStatus status; // 状态
}
