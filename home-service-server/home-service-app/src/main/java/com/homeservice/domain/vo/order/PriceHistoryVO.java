package com.homeservice.domain.vo.order;

import com.homeservice.handler.json.ApiId;
import com.homeservice.handler.json.ApiMoney;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class PriceHistoryVO {

    @ApiId
    private Long id; // 主键ID

    @ApiId
    private Long orderId; // 订单ID

    @ApiMoney
    private BigDecimal previousPrice; // 原价格

    @ApiMoney
    private BigDecimal newPrice; // 新价格

    private Integer priceVersion; // 价格版本

    private OffsetDateTime createdAt; // 创建时间
}
