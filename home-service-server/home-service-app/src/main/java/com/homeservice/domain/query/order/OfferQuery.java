package com.homeservice.domain.query.order;

import com.fasterxml.jackson.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;

import jakarta.validation.constraints.*;

import lombok.*;

import java.time.*;

/**
 * 优惠报价查询类
 * 封装优惠报价相关查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OfferQuery extends com.homeservice.common.domain.PageQuery {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(min = 1, max = 100)
    private String keyword;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate from;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate to;

    /**
     * 校验开始值是否早于结束值
     */
    @AssertTrue(message = "from 不得晚于 to")
    @JsonIgnore
    public boolean isValidRange() {
        return from == null || to == null || !from.isAfter(to);
    }
}
