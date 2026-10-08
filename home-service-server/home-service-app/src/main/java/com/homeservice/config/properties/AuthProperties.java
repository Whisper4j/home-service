package com.homeservice.config.properties;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Set;
@Getter @Setter @Validated
@ConfigurationProperties("home.auth")
public class AuthProperties {
    public static final Set<String> CONTRACT_PUBLIC_ENDPOINTS = Set.of(
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
    @NotNull private List<String> publicEndpoints = List.copyOf(CONTRACT_PUBLIC_ENDPOINTS);
    @AssertTrue(message = "认证白名单不得超出 OpenAPI 的公开方法与路径")
    public boolean isContractSubset() { return publicEndpoints != null && CONTRACT_PUBLIC_ENDPOINTS.containsAll(publicEndpoints); }
}
