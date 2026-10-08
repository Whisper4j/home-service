package com.homeservice.domain.po.audit;
import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.domain.value.*;
import com.homeservice.handler.mybatis.*;
import lombok.Data;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
/** audit_log 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "audit_log", autoResultMap = true)
public class AuditLog {
    /** 审计日志ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 操作者类型：USER/SYSTEM */
    @TableField(value = "actor_type")
    private ActorType actorType;

    /** 用户操作者账号ID；系统任务为空 */
    @TableField(value = "actor_account_id")
    private Long actorAccountId;

    /** 稳定操作代码 */
    @TableField(value = "action")
    private String action;

    /** 审计目标类型 */
    @TableField(value = "target_type")
    private AuditTargetType targetType;

    /** 目标业务ID */
    @TableField(value = "target_id")
    private Long targetId;

    /** 订单相关操作的明确订单ID */
    @TableField(value = "order_id")
    private Long orderId;

    /** 退款等操作关联的资金流水ID */
    @TableField(value = "related_payment_id")
    private Long relatedPaymentId;

    /** 退款等操作的流水类型 */
    @TableField(value = "payment_type")
    private PaymentType paymentType;

    /** 退款等操作金额 */
    @TableField(value = "amount")
    private BigDecimal amount;

    /** 脱敏后的人类可读详情 */
    @TableField(value = "detail")
    private String detail;

    /** 发生时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
}
