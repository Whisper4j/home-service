package com.homeservice.domain.po.account;
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
/** worker_profile 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "worker_profile", autoResultMap = true)
public class WorkerProfile {
    /** 服务人员业务ID，与认证账号ID相互独立 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 认证账号ID */
    @TableField(value = "account_id")
    private Long accountId;

    /** 服务城市代码 */
    @TableField(value = "city_code")
    private String cityCode;

    /** 是否允许新的派单和抢单 */
    @TableField(value = "dispatch_enabled")
    private Boolean dispatchEnabled;

    /** 每日工作区间；未设置排班时为空 */
    @TableField(value = "work_intervals", typeHandler = WorkIntervalsTypeHandler.class)
    private List<WorkIntervalValue> workIntervals;

    /** 每周休息日；1周一至7周日，未设置时为空 */
    @TableField(value = "rest_weekdays", typeHandler = RestWeekdaysTypeHandler.class)
    private List<Integer> restWeekdays;

    /** 创建时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

    /** 更新时间（Asia/Shanghai） */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
