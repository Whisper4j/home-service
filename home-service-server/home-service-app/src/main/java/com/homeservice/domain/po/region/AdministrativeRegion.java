package com.homeservice.domain.po.region;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName("administrative_region")
public class AdministrativeRegion {

    @TableId(type = IdType.INPUT)
    private String code; // 编码
    private String parentCode; // 上级地区编码
    private String name; // 名称
    private String level; // 地区级别
    private Boolean serviceEnabled; // 服务是否启用
    private Integer sortNo; // 排序号
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt; // 更新时间

}
