package com.homeservice.config.properties;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

/**
 * 认证配置属性类
 * 绑定并校验认证配置
 */
@Getter
@Setter
@Validated
@ConfigurationProperties("home.auth")
public class AuthProperties {

    public static final Set<String> CONTRACT_PUBLIC_ENDPOINTS =
            Set.of(
                    "POST /api/customer/auth/login",
                    "POST /api/worker/auth/login",
                    "POST /api/admin/auth/login",
                    "POST /api/customer/auth/register",
                    "GET /api/customer/regions",
                    "GET /api/customer/booking-rules",
                    "GET /api/customer/categories",
                    "GET /api/customer/service-items",
                    "GET /api/customer/skus",
                    "GET /api/customer/skus/{id}",
                    "GET /api/customer/service-entries");
    @NotNull(message = MessageConstant.CONFIG_VALUE_REQUIRED)
    private List<String> publicEndpoints = List.copyOf(CONTRACT_PUBLIC_ENDPOINTS);

    /**
     * 校验认证白名单是否属于契约公开接口
     */
    @AssertTrue(message = MessageConstant.AUTH_WHITELIST_INVALID)
    public boolean isContractSubset() {
        return publicEndpoints != null && CONTRACT_PUBLIC_ENDPOINTS.containsAll(publicEndpoints);
    }
}
