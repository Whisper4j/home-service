package com.homeservice.mapper.idempotency;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homeservice.domain.po.idempotency.HttpIdempotencyRecord;

/**
 * HTTP幂等记录数据访问接口
 * 提供HTTP幂等记录的基础数据库访问能力
 */
public interface HttpIdempotencyRecordMapper extends BaseMapper<HttpIdempotencyRecord> {}
