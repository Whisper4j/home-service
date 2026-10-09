package com.homeservice.domain.po.account;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务人员技能持久化类
 * 映射worker_skill表数据
 */
@Data
@TableName(value = "worker_skill", autoResultMap = true)
public class WorkerSkill {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "worker_id")
    private Long workerId;
    @TableField(value = "skill_id")
    private Long skillId;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

}
