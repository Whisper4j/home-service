package com.homeservice.domain.po.order;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName("order_price_history")
public class OrderPriceHistory {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private Long orderId; // 订单ID
    private BigDecimal previousPrice; // 原价格
    private BigDecimal newPrice; // 新价格
    private Integer priceVersion; // 价格版本
    private Long operatorAccountId; // 操作人账号ID
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间

}
