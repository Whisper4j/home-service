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
/** worker_skill 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "worker_skill", autoResultMap = true)
public class WorkerSkill {
    /** 关联记录ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 服务人员业务ID */
    @TableField(value = "worker_id")
    private Long workerId;

    /** 技能ID */
    @TableField(value = "skill_id")
    private Long skillId;

    /** 授予时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
}
