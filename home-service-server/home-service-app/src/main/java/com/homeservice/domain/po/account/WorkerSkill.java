package com.homeservice.domain.po.account;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName("worker_skill")
public class WorkerSkill {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private Long workerId; // 服务人员ID
    private Long skillId; // 技能ID
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间

}
