package com.homeservice.domain.po.order;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单状态历史持久化类
 * 映射order_status_history表数据
 */
@Data
@TableName(value = "order_status_history", autoResultMap = true)
public class OrderStatusHistory {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "order_id")
    private Long orderId;
    @TableField(value = "from_status")
    private OrderStatus fromStatus;
    @TableField(value = "to_status")
    private OrderStatus toStatus;
    @TableField(value = "actor_type")
    private ActorType actorType;
    @TableField(value = "actor_account_id")
    private Long actorAccountId;
    @TableField(value = "actor_role")
    private Role actorRole;
    @TableField(value = "reason")
    private String reason;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

}
