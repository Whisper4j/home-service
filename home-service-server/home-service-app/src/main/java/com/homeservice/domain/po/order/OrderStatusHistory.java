package com.homeservice.domain.po.order;
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
/** order_status_history 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "order_status_history", autoResultMap = true)
public class OrderStatusHistory {
    /** 订单状态变化历史ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 订单ID */
    @TableField(value = "order_id")
    private Long orderId;

    /** 来源状态；创建记录为空 */
    @TableField(value = "from_status")
    private OrderStatus fromStatus;

    /** 目标状态 */
    @TableField(value = "to_status")
    private OrderStatus toStatus;

    /** 操作者类型：USER/SYSTEM */
    @TableField(value = "actor_type")
    private ActorType actorType;

    /** 用户操作者账号ID；系统任务为空 */
    @TableField(value = "actor_account_id")
    private Long actorAccountId;

    /** 用户操作者角色；系统任务为空 */
    @TableField(value = "actor_role")
    private Role actorRole;

    /** 状态变化原因 */
    @TableField(value = "reason")
    private String reason;

    /** 发生时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
}
