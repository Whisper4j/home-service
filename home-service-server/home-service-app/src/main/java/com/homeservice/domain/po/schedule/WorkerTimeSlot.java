package com.homeservice.domain.po.schedule;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.homeservice.enums.BookingType;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName("worker_time_slot")
public class WorkerTimeSlot {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private Long workerId; // 服务人员ID
    private LocalDateTime slotStart; // 时间槽开始时间
    private String status; // 状态
    private BookingType bookingType; // 预约类型
    private Long assignmentId; // 分配记录ID
    private Long orderId; // 订单ID
    private Long leaveId; // 请假记录ID
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt; // 更新时间

}
