package com.homeservice.domain.po.audit;

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
@TableName("audit_log")
public class AuditLog {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private String actorType; // 操作者类型
    private Long actorAccountId; // 操作者账号ID
    private String action; // 操作名称
    private String targetType; // 目标类型
    private Long targetId; // 目标ID
    private Long orderId; // 订单ID
    private Long relatedPaymentId; // 关联支付ID
    private String paymentType; // 支付类型
    private BigDecimal amount; // 金额
    private String detail; // 详细地址
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间

}
