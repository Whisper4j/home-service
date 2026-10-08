package com.homeservice.domain.po.schedule;
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
/** worker_leave 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "worker_leave", autoResultMap = true)
public class WorkerLeave {
    /** 请假ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 服务人员业务ID */
    @TableField(value = "worker_id")
    private Long workerId;

    /** 请假开始时间（Asia/Shanghai） */
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    /** 请假结束时间（Asia/Shanghai） */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    /** 请假原因 */
    @TableField(value = "reason")
    private String reason;

    /** 状态：ACTIVE/CANCELLED */
    @TableField(value = "status")
    private LeaveStatus status;

    /** 撤销时间（Asia/Shanghai） */
    @TableField(value = "cancelled_at")
    private LocalDateTime cancelledAt;

    /** 创建时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

    /** 更新时间（Asia/Shanghai） */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
