package com.homeservice.domain.po.dispatch;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 派单尝试持久化类
 * 映射dispatch_attempt表数据
 */
@Data
@TableName(value = "dispatch_attempt", autoResultMap = true)
public class DispatchAttempt {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "order_id")
    private Long orderId;
    @TableField(value = "worker_id")
    private Long workerId;
    @TableField(value = "result")
    private DispatchAttemptResult result;
    @TableField(value = "reason")
    private String reason;
    @TableField(value = "service_minutes")
    private Long serviceMinutes;
    @TableField(value = "order_count")
    private Long orderCount;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

}
