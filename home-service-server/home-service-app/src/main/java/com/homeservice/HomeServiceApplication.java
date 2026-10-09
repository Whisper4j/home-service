package com.homeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * 家政服务应用启动类
 * 启动家政预约与调度平台后端应用
 */
@SpringBootApplication
@ConfigurationPropertiesScan("com.homeservice.config.properties")
public class HomeServiceApplication {
    /**
     * 启动家政预约与调度平台后端应用
     */
    public static void main(String[] args) {
        SpringApplication.run(HomeServiceApplication.class, args);
    }
}
