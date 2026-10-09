package com.homeservice.domain.po.dispatch;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单分配持久化类
 * 映射order_assignment表数据
 */
@Data
@TableName(value = "order_assignment", autoResultMap = true)
public class OrderAssignment {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "order_id")
    private Long orderId;
    @TableField(value = "worker_id")
    private Long workerId;
    @TableField(value = "worker_name")
    private String workerName;
    @TableField(value = "booking_type")
    private BookingType bookingType;
    @TableField(value = "status")
    private AssignmentStatus status;
    @TableField(value = "assigned_at")
    private LocalDateTime assignedAt;
    @TableField(value = "released_at")
    private LocalDateTime releasedAt;
    @TableField(value = "release_reason")
    private String releaseReason;
    @TableField(value = "finished_at")
    private LocalDateTime finishedAt;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
