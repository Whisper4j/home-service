package com.homeservice.domain.po.audit;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 审计日志持久化类
 * 映射audit_log表数据
 */
@Data
@TableName(value = "audit_log", autoResultMap = true)
public class AuditLog {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "actor_type")
    private ActorType actorType;
    @TableField(value = "actor_account_id")
    private Long actorAccountId;
    @TableField(value = "action")
    private String action;
    @TableField(value = "target_type")
    private AuditTargetType targetType;
    @TableField(value = "target_id")
    private Long targetId;
    @TableField(value = "order_id")
    private Long orderId;
    @TableField(value = "related_payment_id")
    private Long relatedPaymentId;
    @TableField(value = "payment_type")
    private PaymentType paymentType;
    @TableField(value = "amount")
    private BigDecimal amount;
    @TableField(value = "detail")
    private String detail;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

}
