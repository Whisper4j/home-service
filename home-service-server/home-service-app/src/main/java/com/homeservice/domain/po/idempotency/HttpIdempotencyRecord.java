package com.homeservice.domain.po.idempotency;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.homeservice.domain.value.IdempotencyResponse;
import com.homeservice.handler.mybatis.IdempotencyResponseTypeHandler;

import java.time.LocalDateTime;

import lombok.Data;

@Data
@TableName(value = "http_idempotency_record", autoResultMap = true)
public class HttpIdempotencyRecord {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private String scopeType; // 幂等作用域类型
    private Long accountId; // 账号ID
    private String anonymousSubjectHash; // 匿名主体摘要
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private String scopeIdentity; // 作用域标识（数据库生成）
    private String httpMethod; // HTTP方法
    private String requestPath; // 请求路径
    private String idempotencyKey; // 幂等键
    private String requestFingerprint; // 请求指纹
    private String executionStatus; // 执行状态
    private Integer responseHttpStatus; // 响应状态码
    private String responseContentType; // 响应媒体类型
    @TableField(typeHandler = IdempotencyResponseTypeHandler.class)
    private IdempotencyResponse responseJson; // 幂等响应（JSON）
    private LocalDateTime expiresAt; // 过期时间
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime completedAt; // 完成时间

}
