package com.homeservice.domain.po.review;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.homeservice.handler.mybatis.ReviewTagsTypeHandler;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
@TableName(value = "service_review", autoResultMap = true)
public class ServiceReview {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private Long orderId; // 订单ID
    private Long customerId; // 客户ID
    private Long workerId; // 服务人员ID
    private Integer score; // 评分
    private String content; // 评价内容
    @TableField(typeHandler = ReviewTagsTypeHandler.class)
    private List<String> tags; // 评价标签（JSON）
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间

}
