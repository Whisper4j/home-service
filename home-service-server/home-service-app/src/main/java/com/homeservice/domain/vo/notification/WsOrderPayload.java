package com.homeservice.domain.vo.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.homeservice.enums.OrderStatus;
import com.homeservice.handler.json.ApiMoney;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class WsOrderPayload {

    private OrderStatus status; // 状态

    @ApiMoney
    private BigDecimal currentPrice; // 当前价格

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String reason; // 原因
}
