package com.homeservice.domain.po.dispatch;
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
/** dispatch_attempt 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "dispatch_attempt", autoResultMap = true)
public class DispatchAttempt {
    /** 调度尝试ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 订单ID，便于管理端按单查询 */
    @TableField(value = "order_id")
    private Long orderId;

    /** 候选服务人员；无候选时为空 */
    @TableField(value = "worker_id")
    private Long workerId;

    /** 派单结果：ASSIGNED/INELIGIBLE/SLOT_CONFLICT/NO_CANDIDATE */
    @TableField(value = "result")
    private DispatchAttemptResult result;

    /** 筛选或尝试结果原因 */
    @TableField(value = "reason")
    private String reason;

    /** 候选排序时当日已分配服务分钟数快照 */
    @TableField(value = "service_minutes")
    private Long serviceMinutes;

    /** 候选排序时当日订单数快照 */
    @TableField(value = "order_count")
    private Long orderCount;

    /** 尝试时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
}
