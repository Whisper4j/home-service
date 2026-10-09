package com.homeservice.domain.po.order;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.homeservice.enums.OrderStatus;
import com.homeservice.enums.Role;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName("order_status_history")
public class OrderStatusHistory {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private Long orderId; // 订单ID
    private OrderStatus fromStatus; // 原订单状态
    private OrderStatus toStatus; // 新订单状态
    private String actorType; // 操作者类型
    private Long actorAccountId; // 操作者账号ID
    private Role actorRole; // 操作者角色
    private String reason; // 原因
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间

}
