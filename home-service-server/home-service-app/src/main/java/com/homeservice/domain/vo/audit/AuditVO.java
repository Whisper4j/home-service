package com.homeservice.domain.vo.audit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.homeservice.handler.json.ApiId;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class AuditVO {

    @ApiId
    private Long id; // 主键ID

    private String actorType; // 操作者类型

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiId
    private Long actorId; // 操作者账号ID

    private String action; // 操作名称

    @ApiId
    private Long targetId; // 目标ID

    private String detail; // 详细地址

    private OffsetDateTime createdAt; // 创建时间

    private String targetType; // 目标类型

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiId
    private Long orderId; // 订单ID
}
