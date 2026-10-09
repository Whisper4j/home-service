package com.homeservice.domain.po.review;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 服务评价持久化类
 * 映射service_review表数据
 */
@Data
@TableName(value = "service_review", autoResultMap = true)
public class ServiceReview {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "order_id")
    private Long orderId;
    @TableField(value = "customer_id")
    private Long customerId;
    @TableField(value = "worker_id")
    private Long workerId;
    @TableField(value = "score")
    private Integer score;
    @TableField(value = "content")
    private String content;
    @TableField(value = "tags", typeHandler = ReviewTagsTypeHandler.class)
    private List<ReviewTag> tags;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

}
