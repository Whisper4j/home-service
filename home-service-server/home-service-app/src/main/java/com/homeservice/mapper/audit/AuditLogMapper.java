package com.homeservice.mapper.audit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homeservice.domain.po.audit.AuditLog;
/** 基础持久化操作不等于业务规则，调用方须遵守 Service 事务边界。 */
public interface AuditLogMapper extends BaseMapper<AuditLog> {}
