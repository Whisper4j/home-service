package com.homeservice.domain.po.catalog;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 服务项目持久化类
 * 映射service_item表数据
 */
@Data
@TableName(value = "service_item", autoResultMap = true)
public class ServiceItem {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "category_id")
    private Long categoryId;
    @TableField(value = "name")
    private String name;
    @TableField(value = "service_kind")
    private ServiceKind serviceKind;
    @TableField(value = "description")
    private String description;
    @TableField(value = "status")
    private CatalogStatus status;
    @TableField(value = "deleted_at")
    @TableLogic(value = "null", delval = "CURRENT_TIMESTAMP")
    private LocalDateTime deletedAt;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
