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
/** worker_time_slot 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "worker_time_slot", autoResultMap = true)
public class WorkerTimeSlot {
    /** 半小时时间槽ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 服务人员业务ID */
    @TableField(value = "worker_id")
    private Long workerId;

    /** 槽开始时间（Asia/Shanghai），半小时对齐 */
    @TableField(value = "slot_start")
    private LocalDateTime slotStart;

    /** 状态：NON_WORKING/AVAILABLE/LEAVE/SERVICE/BUFFER */
    @TableField(value = "status")
    private SlotStatus status;

    /** 占用类型STANDARD/OFFER，仅SERVICE/BUFFER存在 */
    @TableField(value = "booking_type")
    private BookingType bookingType;

    /** 占用分配记录ID，仅SERVICE/BUFFER存在 */
    @TableField(value = "assignment_id")
    private Long assignmentId;

    /** 占用订单ID，仅SERVICE/BUFFER存在 */
    @TableField(value = "order_id")
    private Long orderId;

    /** 请假ID，仅LEAVE存在 */
    @TableField(value = "leave_id")
    private Long leaveId;

    /** 创建时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

    /** 更新时间（Asia/Shanghai） */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
