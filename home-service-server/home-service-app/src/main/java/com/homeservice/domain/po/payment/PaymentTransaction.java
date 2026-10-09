package com.homeservice.domain.po.payment;

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
@TableName("payment_transaction")
public class PaymentTransaction {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private Long orderId; // 订单ID
    private String businessNo; // 业务流水号
    private String type; // 类型
    private BigDecimal amount; // 金额
    private Integer relatedPriceVersion; // 关联价格版本
    private String remark; // 备注
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间

}
