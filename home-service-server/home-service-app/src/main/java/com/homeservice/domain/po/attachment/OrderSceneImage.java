package com.homeservice.domain.po.attachment;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单现场图片持久化类
 * 映射order_scene_image表数据
 */
@Data
@TableName(value = "order_scene_image", autoResultMap = true)
public class OrderSceneImage {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "order_id")
    private Long orderId;
    @TableField(value = "image_id")
    private Long imageId;
    @TableField(value = "sort_no")
    private Integer sortNo;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

}
