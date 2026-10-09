package com.homeservice.domain.po.schedule;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务人员请假持久化类
 * 映射worker_leave表数据
 */
@Data
@TableName(value = "worker_leave", autoResultMap = true)
public class WorkerLeave {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "worker_id")
    private Long workerId;
    @TableField(value = "start_time")
    private LocalDateTime startTime;
    @TableField(value = "end_time")
    private LocalDateTime endTime;
    @TableField(value = "reason")
    private String reason;
    @TableField(value = "status")
    private LeaveStatus status;
    @TableField(value = "cancelled_at")
    private LocalDateTime cancelledAt;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
