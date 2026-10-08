package com.homeservice.mapper.catalog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.homeservice.domain.po.catalog.ServiceSku;
/** 基础持久化操作不等于业务规则，调用方须遵守 Service 事务边界。 */
public interface ServiceSkuMapper extends BaseMapper<ServiceSku> {}
