package com.homeservice.domain.po.order;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单价格历史持久化类
 * 映射order_price_history表数据
 */
@Data
@TableName(value = "order_price_history", autoResultMap = true)
public class OrderPriceHistory {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "order_id")
    private Long orderId;
    @TableField(value = "previous_price")
    private BigDecimal previousPrice;
    @TableField(value = "new_price")
    private BigDecimal newPrice;
    @TableField(value = "price_version")
    private Integer priceVersion;
    @TableField(value = "operator_account_id")
    private Long operatorAccountId;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

}
