package com.homeservice.domain.vo.review;

import com.homeservice.handler.json.ApiId;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ReviewVO {

    @ApiId
    private Long id; // 主键ID

    @ApiId
    private Long orderId; // 订单ID

    private OffsetDateTime createdAt; // 创建时间

    private Integer score; // 评分

    private List<String> tags; // 评价标签（JSON）

    private String content; // 评价内容
}
