package com.homeservice.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.*;

/**
 * MyBatis配置类
 * 配置Mapper扫描、分页插件和字段填充组件
 */
@Configuration(proxyBeanMethods = false)
@MapperScan("com.homeservice.mapper")
public class MybatisConfig {
    /**
     * 创建MyBatis-Plus分页拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        var interceptor = new MybatisPlusInterceptor();
        var pagination = new PaginationInnerInterceptor(DbType.MYSQL);
        pagination.setMaxLimit(100L);
        pagination.setOverflow(false);
        interceptor.addInnerInterceptor(pagination);
        return interceptor;
    }
}
