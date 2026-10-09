package com.homeservice.domain.query.account;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 服务人员查询类
 * 封装服务人员相关查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkerQuery extends com.homeservice.common.domain.PageQuery {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(min = 1, max = 100)
    private String keyword;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiId
    @Positive
    private Long skillId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean dispatchEnabled;

}
