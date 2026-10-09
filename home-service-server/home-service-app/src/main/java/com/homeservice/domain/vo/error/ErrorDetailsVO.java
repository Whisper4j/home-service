package com.homeservice.domain.vo.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.homeservice.enums.OrderStatus;
import com.homeservice.handler.json.ApiIds;
import com.homeservice.handler.json.ApiMoney;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ErrorDetailsVO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FieldErrorVO> fieldErrors; // 字段错误列表

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiMoney
    private BigDecimal currentPrice; // 当前价格

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer priceVersion; // 价格版本

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OrderStatus currentStatus; // 当前状态

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiIds
    private List<Long> conflictingOrderIds; // 冲突订单ID列表
}
