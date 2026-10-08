package com.homeservice.domain.po.order;
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
/** order_required_skill 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "order_required_skill", autoResultMap = true)
public class OrderRequiredSkill {
    /** 关联记录ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 订单ID */
    @TableField(value = "order_id")
    private Long orderId;

    /** 创建时技能ID */
    @TableField(value = "skill_id")
    private Long skillId;

    /** 技能名称快照 */
    @TableField(value = "skill_name")
    private String skillName;

    /** 快照创建时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
}
