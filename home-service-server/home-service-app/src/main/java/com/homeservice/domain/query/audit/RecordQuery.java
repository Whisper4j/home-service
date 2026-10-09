package com.homeservice.domain.query.audit;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 记录查询类
 * 封装记录相关查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RecordQuery extends com.homeservice.common.domain.PageQuery {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiId
    @Positive
    private Long orderId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(min = 1, max = 100)
    private String keyword;

}
