package com.homeservice.domain.query.catalog;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 服务规格查询类
 * 封装服务规格相关查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkuQuery extends com.homeservice.common.domain.PageQuery {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(min = 1, max = 100)
    private String keyword;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiId
    @Positive
    private Long categoryId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiId
    @Positive
    private Long itemId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CatalogStatus status;

}
