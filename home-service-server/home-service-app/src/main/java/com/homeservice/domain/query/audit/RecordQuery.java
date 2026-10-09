package com.homeservice.domain.query.audit;

import com.homeservice.handler.json.ApiId;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecordQuery extends com.homeservice.common.domain.PageQuery {
    @ApiId
    @Positive(message = "订单ID必须大于0")
    private Long orderId; // 订单ID
    @Size(min = 1, max = 100, message = "搜索关键词长度必须在1到100之间")
    private String keyword; // 搜索关键词

}
