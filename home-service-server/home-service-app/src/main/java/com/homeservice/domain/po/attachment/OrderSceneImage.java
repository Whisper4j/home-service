package com.homeservice.domain.po.attachment;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName("order_scene_image")
public class OrderSceneImage {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private Long orderId; // 订单ID
    private Long imageId; // 图片ID
    private Integer sortNo; // 排序号
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间

}
