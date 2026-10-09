package com.homeservice.support;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.homeservice.common.domain.Result;
import com.homeservice.config.*;
import com.homeservice.config.properties.*;
import com.homeservice.domain.dto.account.LoginDTO;
import com.homeservice.domain.dto.catalog.SkuDTO;
import com.homeservice.domain.dto.order.ClaimOfferDTO;
import com.homeservice.domain.query.catalog.PublicSkuQuery;
import com.homeservice.domain.query.order.OrderQuery;
import com.homeservice.domain.value.AccountIdentity;
import com.homeservice.enums.*;
import com.homeservice.enums.Role;
import com.homeservice.exception.ApiException;
import com.homeservice.handler.*;
import com.homeservice.handler.websocket.*;
import com.homeservice.interceptor.AuthenticationInterceptor;
import com.homeservice.service.account.IAccountQueryService;
import com.homeservice.utils.*;

import jakarta.validation.Valid;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * 协议测试应用配置类
 * 提供认证和异常协议测试所需的测试组件
 */
@Configuration(proxyBeanMethods = false)
@Profile("protocol")
@EnableAutoConfiguration(
        exclude = {DataSourceAutoConfiguration.class, MybatisPlusAutoConfiguration.class})
@EnableConfigurationProperties({
    JwtProperties.class,
    AuthProperties.class,
    WebSocketProperties.class
})
@Import({
    JacksonConfig.class,
    SecurityConfig.class,
    WebMvcConfig.class,
    AuthenticationInterceptor.class,
    AccountAuthenticator.class,
    JwtTool.class,
    GlobalExceptionHandler.class,
    RequestBindingAdvice.class,
    UserContextCleanupFilter.class,
    ApiErrorController.class,
    WebSocketConfig.class,
    AuthenticatedWebSocketHandler.class,
    WebSocketSessionRegistry.class,
    NotificationSender.class,
    ProtocolTestApplication.TestController.class
})
public class ProtocolTestApplication {
    /**
     * 验证test账号查询场景
     */
    @Bean
    public IAccountQueryService testAccountQuery() {
        return id ->
                switch ((int) id) {
                    case 1 ->
                            Optional.of(
                                    new AccountIdentity(1L, Role.CUSTOMER, AccountStatus.ENABLED));
                    case 2 ->
                            Optional.of(
                                    new AccountIdentity(2L, Role.WORKER, AccountStatus.ENABLED));
                    case 3 ->
                            Optional.of(new AccountIdentity(3L, Role.ADMIN, AccountStatus.ENABLED));
                    case 4 ->
                            Optional.of(
                                    new AccountIdentity(4L, Role.CUSTOMER, AccountStatus.DISABLED));
                    case 5 ->
                            Optional.of(new AccountIdentity(5L, Role.ADMIN, AccountStatus.ENABLED));
                    default -> Optional.empty();
                };
    }

    /**
     * 测试控制器类
     * 提供MVC基础设施测试使用的接口
     */
    @RestController
    @org.springframework.boot.test.context.TestComponent
    public static class TestController {
        /**
         * 验证identity场景
         */
        @GetMapping({"/api/customer/probe", "/api/worker/probe", "/api/admin/probe"})
        public Result<String> identity() {
            return Result.success(Long.toString(UserContext.require().accountId()));
        }

        /**
         * 验证path场景
         */
        @GetMapping("/api/customer/probe/{id}")
        public Result<String> path(@PathVariable Long id) {
            return Result.success(id.toString());
        }

        /**
         * 验证money场景
         */
        @PostMapping("/api/customer/probe/money")
        public Result<ClaimOfferDTO> money(@Valid @RequestBody ClaimOfferDTO body) {
            return Result.success(body);
        }

        /**
         * 验证sku场景
         */
        @PostMapping("/api/admin/probe/sku")
        public Result<SkuDTO> sku(@Valid @RequestBody SkuDTO body) {
            return Result.success(body);
        }

        /**
         * 验证login结构场景
         */
        @PostMapping("/api/customer/auth/login")
        public Result<String> loginShape(@Valid @RequestBody LoginDTO body) {
            return Result.success(body.username());
        }

        /**
         * 验证public目录场景
         */
        @GetMapping("/api/customer/skus")
        public Result<PublicSkuQuery> publicCatalog(@Valid @ModelAttribute PublicSkuQuery query) {
            return Result.success(query);
        }

        /**
         * 验证protected写入场景
         */
        @PostMapping("/api/customer/skus")
        public Result<String> protectedWrite() {
            return Result.success(UserContext.require().role().name());
        }

        /**
         * 验证orders场景
         */
        @GetMapping("/api/customer/probe/orders")
        public Result<OrderQuery> orders(@Valid @ModelAttribute OrderQuery query) {
            return Result.success(query);
        }

        /**
         * 处理业务异常
         */
        @GetMapping("/api/customer/probe/business")
        public Result<Void> business() {
            throw new ApiException(ErrorCode.STATE_CONFLICT);
        }

        /**
         * 验证failure场景
         */
        @GetMapping("/api/customer/probe/failure")
        public Result<Void> failure() {
            throw new IllegalStateException("SELECT password_hash secret-token");
        }

        /**
         * 验证async场景
         */
        @GetMapping("/api/customer/probe/async")
        public Callable<Result<String>> async() {
            long accountId = UserContext.require().accountId();
            return () -> {
                if (UserContext.get() != null)
                    throw new IllegalStateException("ThreadLocal leaked");
                return Result.success(Long.toString(accountId));
            };
        }
    }
}
