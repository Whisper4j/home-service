package com.homeservice.domain.query.audit;

import com.homeservice.common.constant.MessageConstant;

import com.homeservice.handler.json.ApiId;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecordQuery extends com.homeservice.common.domain.PageQuery {
    @ApiId
    @Positive(message = MessageConstant.ORDER_SELECTION_INVALID)
    private Long orderId; // 订单ID
    @Size(min = 1, max = 100, message = MessageConstant.SEARCH_KEYWORD_TOO_LONG)
    private String keyword; // 搜索关键词

}
