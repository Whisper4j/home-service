package com.homeservice.domain.query.catalog;

import com.homeservice.enums.CatalogStatus;

import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryQuery extends com.homeservice.common.domain.PageQuery {
    @Size(min = 1, max = 100, message = "搜索关键词长度必须在1到100之间")
    private String keyword; // 搜索关键词
    private CatalogStatus status; // 状态

}
