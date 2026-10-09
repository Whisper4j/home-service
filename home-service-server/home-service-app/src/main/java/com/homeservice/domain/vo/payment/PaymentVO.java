package com.homeservice.domain.vo.payment;

import com.homeservice.handler.json.ApiId;
import com.homeservice.handler.json.ApiMoney;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class PaymentVO {

    @ApiId
    private Long id; // 主键ID

    @ApiId
    private Long orderId; // 订单ID

    private String businessNo; // 业务流水号

    private String type; // 类型

    @ApiMoney
    private BigDecimal amount; // 金额

    private OffsetDateTime createdAt; // 创建时间
}
