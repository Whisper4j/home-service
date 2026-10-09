package com.homeservice.domain.po.payment;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付流水持久化类
 * 映射payment_transaction表数据
 */
@Data
@TableName(value = "payment_transaction", autoResultMap = true)
public class PaymentTransaction {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "order_id")
    private Long orderId;
    @TableField(value = "business_no")
    private String businessNo;
    @TableField(value = "type")
    private PaymentType type;
    @TableField(value = "amount")
    private BigDecimal amount;
    @TableField(value = "related_price_version")
    private Integer relatedPriceVersion;
    @TableField(value = "remark")
    private String remark;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

}
