package com.homeservice.mapper.dispatch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homeservice.domain.po.dispatch.OrderAssignment;
/** 基础持久化操作不等于业务规则，调用方须遵守 Service 事务边界。 */
public interface OrderAssignmentMapper extends BaseMapper<OrderAssignment> {}
