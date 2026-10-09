package com.homeservice.domain.po.catalog;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.homeservice.enums.CatalogStatus;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName("service_category")
public class ServiceCategory {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private String name; // 名称
    private Integer sortNo; // 排序号
    private CatalogStatus status; // 状态
    @TableLogic(value = "null", delval = "CURRENT_TIMESTAMP")
    private LocalDateTime deletedAt; // 删除时间（NULL有效）
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt; // 更新时间

}
