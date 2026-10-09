package com.homeservice.domain.po.idempotency;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * HTTP幂等记录持久化类
 * 映射http_idempotency_record表数据
 */
@Data
@TableName(value = "http_idempotency_record", autoResultMap = true)
public class HttpIdempotencyRecord {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "scope_type")
    private IdempotencyScope scopeType;
    @TableField(value = "account_id")
    private Long accountId;
    @TableField(value = "anonymous_subject_hash")
    private String anonymousSubjectHash;
    @TableField(
            value = "scope_identity",
            insertStrategy = FieldStrategy.NEVER,
            updateStrategy = FieldStrategy.NEVER)
    private String scopeIdentity;
    @TableField(value = "http_method")
    private WriteHttpMethod httpMethod;
    @TableField(value = "request_path")
    private String requestPath;
    @TableField(value = "idempotency_key")
    private String idempotencyKey;
    @TableField(value = "request_fingerprint")
    private String requestFingerprint;
    @TableField(value = "execution_status")
    private IdempotencyStatus executionStatus;
    @TableField(value = "response_http_status")
    private Integer responseHttpStatus;
    @TableField(value = "response_content_type")
    private String responseContentType;
    @TableField(value = "response_json", typeHandler = IdempotencyResponseTypeHandler.class)
    private IdempotencyResponse responseJson;
    @TableField(value = "expires_at")
    private LocalDateTime expiresAt;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
    @TableField(value = "completed_at")
    private LocalDateTime completedAt;

}
