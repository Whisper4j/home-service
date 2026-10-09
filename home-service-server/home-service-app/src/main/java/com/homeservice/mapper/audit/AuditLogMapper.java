package com.homeservice.mapper.audit;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homeservice.domain.po.audit.AuditLog;

/**
 * 审计日志数据访问接口
 * 提供审计日志的基础数据库访问能力
 */
public interface AuditLogMapper extends BaseMapper<AuditLog> {}
