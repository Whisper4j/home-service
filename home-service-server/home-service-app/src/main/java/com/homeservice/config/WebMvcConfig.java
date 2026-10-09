package com.homeservice.config;

import com.homeservice.common.constant.MessageConstant;

import com.homeservice.handler.json.*;
import com.homeservice.interceptor.AuthenticationInterceptor;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.*;

import java.time.*;

/**
 * Web MVC配置类
 * 配置请求拦截器和严格参数转换规则
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;

    /**
     * 注册身份认证拦截器
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor).addPathPatterns("/api/**");
    }

    /**
     * 注册严格请求参数转换器
     */
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, Long.class, IdDeserializer::parse);
        registry.addConverter(
                String.class,
                Integer.class,
                input -> {
                    String value = input.strip();
                    if (!value.matches("-?[0-9]+")) throw new IllegalArgumentException(MessageConstant.INTEGER_REQUIRED);
                    return Integer.valueOf(value);
                });
        registry.addConverter(
                String.class,
                Boolean.class,
                input -> {
                    String value = input.strip();
                    if (!value.equals("true") && !value.equals("false"))
                        throw new IllegalArgumentException(MessageConstant.BOOLEAN_REQUIRED);
                    return Boolean.valueOf(value);
                });
        registry.addConverter(String.class, LocalDate.class, StrictLocalDateDeserializer::parse);
        registry.addConverter(String.class, LocalTime.class, HalfHourTimeDeserializer::parse);
        registry.addConverter(
                String.class, OffsetDateTime.class, ShanghaiDateTimeDeserializer::parse);
    }
}
