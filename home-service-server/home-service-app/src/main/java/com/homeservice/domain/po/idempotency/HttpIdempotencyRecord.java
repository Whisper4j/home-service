package com.homeservice.domain.po.idempotency;
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
/** http_idempotency_record 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "http_idempotency_record", autoResultMap = true)
public class HttpIdempotencyRecord {
    /** HTTP幂等记录ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** ACCOUNT或ANONYMOUS_REGISTER */
    @TableField(value = "scope_type")
    private IdempotencyScope scopeType;

    /** 认证账号作用域的账号ID */
    @TableField(value = "account_id")
    private Long accountId;

    /** 匿名注册使用规范化用户名SHA-256，不保存密码或完整请求 */
    @TableField(value = "anonymous_subject_hash")
    private String anonymousSubjectHash;

    /** 统一唯一作用域辅助列 */
    @TableField(value = "scope_identity", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private String scopeIdentity;

    /** 大写HTTP方法 */
    @TableField(value = "http_method")
    private WriteHttpMethod httpMethod;

    /** 不含查询串的规范化接口路径 */
    @TableField(value = "request_path")
    private String requestPath;

    /** 业务取值：Idempotency-Key */
    @TableField(value = "idempotency_key")
    private String idempotencyKey;

    /** 规范化请求指纹；敏感内容使用HMAC-SHA-256，不保存原文 */
    @TableField(value = "request_fingerprint")
    private String requestFingerprint;

    /** 执行状态：PROCESSING/SUCCEEDED */
    @TableField(value = "execution_status")
    private IdempotencyStatus executionStatus;

    /** 成功响应HTTP状态码 */
    @TableField(value = "response_http_status")
    private Integer responseHttpStatus;

    /** 成功响应Content-Type */
    @TableField(value = "response_content_type")
    private String responseContentType;

    /** 成功响应必要快照；不包含密码、令牌或无关敏感字段 */
    @TableField(value = "response_json", typeHandler = IdempotencyResponseTypeHandler.class)
    private IdempotencyResponse responseJson;

    /** 幂等记录过期时间，至少保留24小时 */
    @TableField(value = "expires_at")
    private LocalDateTime expiresAt;

    /** 开始执行时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

    /** 成功完成时间（Asia/Shanghai） */
    @TableField(value = "completed_at")
    private LocalDateTime completedAt;
}
