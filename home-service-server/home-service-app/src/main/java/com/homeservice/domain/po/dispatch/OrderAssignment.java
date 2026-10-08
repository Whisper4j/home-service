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
/** order_assignment 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "order_assignment", autoResultMap = true)
public class OrderAssignment {
    /** 订单分配记录ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 订单ID */
    @TableField(value = "order_id")
    private Long orderId;

    /** 服务人员业务ID */
    @TableField(value = "worker_id")
    private Long workerId;

    /** 分配时人员名称快照 */
    @TableField(value = "worker_name")
    private String workerName;

    /** STANDARD/OFFER快照 */
    @TableField(value = "booking_type")
    private BookingType bookingType;

    /** 状态：ACTIVE/RELEASED/FINISHED */
    @TableField(value = "status")
    private AssignmentStatus status;

    /** 生效分配时间（Asia/Shanghai） */
    @TableField(value = "assigned_at")
    private LocalDateTime assignedAt;

    /** 取消释放时间（Asia/Shanghai） */
    @TableField(value = "released_at")
    private LocalDateTime releasedAt;

    /** 释放原因 */
    @TableField(value = "release_reason")
    private String releaseReason;

    /** 订单完成确认时间（Asia/Shanghai） */
    @TableField(value = "finished_at")
    private LocalDateTime finishedAt;

    /** 创建时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

    /** 更新时间（Asia/Shanghai） */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
