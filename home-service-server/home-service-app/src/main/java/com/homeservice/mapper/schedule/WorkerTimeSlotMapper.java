package com.homeservice.mapper.schedule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homeservice.domain.po.schedule.WorkerTimeSlot;
/** 基础持久化操作不等于业务规则，调用方须遵守 Service 事务边界。 */
public interface WorkerTimeSlotMapper extends BaseMapper<WorkerTimeSlot> {}
