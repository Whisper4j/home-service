package com.homeservice.mapper.review;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homeservice.domain.po.review.ServiceReview;
/** 基础持久化操作不等于业务规则，调用方须遵守 Service 事务边界。 */
public interface ServiceReviewMapper extends BaseMapper<ServiceReview> {}
