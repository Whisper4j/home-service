package com.homeservice.domain.po.catalog;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务规格技能持久化类
 * 映射service_sku_skill表数据
 */
@Data
@TableName(value = "service_sku_skill", autoResultMap = true)
public class ServiceSkuSkill {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "sku_id")
    private Long skuId;
    @TableField(value = "skill_id")
    private Long skillId;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

}
