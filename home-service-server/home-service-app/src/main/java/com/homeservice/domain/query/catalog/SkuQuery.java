package com.homeservice.domain.query.catalog;

import com.homeservice.common.constant.MessageConstant;

import com.homeservice.enums.CatalogStatus;
import com.homeservice.handler.json.ApiId;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SkuQuery extends com.homeservice.common.domain.PageQuery {
    @Size(min = 1, max = 100, message = MessageConstant.SEARCH_KEYWORD_TOO_LONG)
    private String keyword; // 搜索关键词
    @ApiId
    @Positive(message = MessageConstant.CATALOG_SELECTION_INVALID)
    private Long categoryId; // 分类ID
    @ApiId
    @Positive(message = MessageConstant.CATALOG_SELECTION_INVALID)
    private Long itemId; // 服务项目ID
    private CatalogStatus status; // 状态

}
