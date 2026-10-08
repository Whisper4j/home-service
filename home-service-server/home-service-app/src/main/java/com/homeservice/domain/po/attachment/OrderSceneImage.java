package com.homeservice.domain.po.attachment;
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
/** order_scene_image 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "order_scene_image", autoResultMap = true)
public class OrderSceneImage {
    /** 关联记录ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 订单ID */
    @TableField(value = "order_id")
    private Long orderId;

    /** 现场图片ID */
    @TableField(value = "image_id")
    private Long imageId;

    /** 订单内顺序1至3 */
    @TableField(value = "sort_no")
    private Integer sortNo;

    /** 绑定时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
}
