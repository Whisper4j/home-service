package com.homeservice.domain.query.catalog;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 公开服务项目查询类
 * 封装公开服务项目相关查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PublicServiceItemQuery extends com.homeservice.common.domain.PageQuery {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(min = 1, max = 100)
    private String keyword;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiId
    @Positive
    private Long categoryId;

}
