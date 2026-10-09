package com.homeservice.domain.po.schedule;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务人员时间槽持久化类
 * 映射worker_time_slot表数据
 */
@Data
@TableName(value = "worker_time_slot", autoResultMap = true)
public class WorkerTimeSlot {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "worker_id")
    private Long workerId;
    @TableField(value = "slot_start")
    private LocalDateTime slotStart;
    @TableField(value = "status")
    private SlotStatus status;
    @TableField(value = "booking_type")
    private BookingType bookingType;
    @TableField(value = "assignment_id")
    private Long assignmentId;
    @TableField(value = "order_id")
    private Long orderId;
    @TableField(value = "leave_id")
    private Long leaveId;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
