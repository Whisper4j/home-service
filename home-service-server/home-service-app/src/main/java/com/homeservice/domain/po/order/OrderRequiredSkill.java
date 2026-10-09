package com.homeservice.domain.po.order;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单所需技能持久化类
 * 映射order_required_skill表数据
 */
@Data
@TableName(value = "order_required_skill", autoResultMap = true)
public class OrderRequiredSkill {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "order_id")
    private Long orderId;
    @TableField(value = "skill_id")
    private Long skillId;
    @TableField(value = "skill_name")
    private String skillName;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

}
