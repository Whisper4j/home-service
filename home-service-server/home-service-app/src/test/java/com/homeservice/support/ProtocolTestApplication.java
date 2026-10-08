package com.homeservice.support;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.homeservice.common.domain.R;
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

/** 仅测试 Servlet/WS 协议；真实数据库由 MySqlMappingIT 单独验收。 */
@Configuration(proxyBeanMethods = false)
@Profile("protocol")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, MybatisPlusAutoConfiguration.class})
@EnableConfigurationProperties({JwtProperties.class, AuthProperties.class, WebSocketProperties.class})
@Import({JacksonConfig.class, SecurityConfig.class, WebMvcConfig.class, AuthenticationInterceptor.class,
    AccountAuthenticator.class, JwtTool.class, GlobalExceptionHandler.class, RequestBindingAdvice.class,
    UserContextCleanupFilter.class, ApiErrorController.class, WebSocketConfig.class,
    AuthenticatedWebSocketHandler.class, WebSocketSessionRegistry.class, NotificationSender.class,
    ProtocolTestApplication.TestController.class})
public class ProtocolTestApplication {
    @Bean public IAccountQueryService testAccountQuery() {
        return id -> switch ((int) id) {
            case 1 -> Optional.of(new AccountIdentity(1L, Role.CUSTOMER, AccountStatus.ENABLED));
            case 2 -> Optional.of(new AccountIdentity(2L, Role.WORKER, AccountStatus.ENABLED));
            case 3 -> Optional.of(new AccountIdentity(3L, Role.ADMIN, AccountStatus.ENABLED));
            case 4 -> Optional.of(new AccountIdentity(4L, Role.CUSTOMER, AccountStatus.DISABLED));
            case 5 -> Optional.of(new AccountIdentity(5L, Role.ADMIN, AccountStatus.ENABLED));
            default -> Optional.empty();
        };
    }

    @RestController
    @org.springframework.boot.test.context.TestComponent
    public static class TestController {
        @GetMapping({"/api/customer/probe", "/api/worker/probe", "/api/admin/probe"})
        public R<String> identity() { return R.ok(Long.toString(UserContext.require().accountId())); }
        @GetMapping("/api/customer/probe/{id}")
        public R<String> path(@PathVariable Long id) { return R.ok(id.toString()); }
        @PostMapping("/api/customer/probe/money")
        public R<ClaimOfferDTO> money(@Valid @RequestBody ClaimOfferDTO body) { return R.ok(body); }
        @PostMapping("/api/admin/probe/sku")
        public R<SkuDTO> sku(@Valid @RequestBody SkuDTO body) { return R.ok(body); }
        @PostMapping("/api/customer/auth/login")
        public R<String> loginShape(@Valid @RequestBody LoginDTO body) { return R.ok(body.username()); }
        @GetMapping("/api/customer/skus")
        public R<PublicSkuQuery> publicCatalog(@Valid @ModelAttribute PublicSkuQuery query) { return R.ok(query); }
        @PostMapping("/api/customer/skus")
        public R<String> protectedWrite() { return R.ok(UserContext.require().role().name()); }
        @GetMapping("/api/customer/probe/orders")
        public R<OrderQuery> orders(@Valid @ModelAttribute OrderQuery query) { return R.ok(query); }
        @GetMapping("/api/customer/probe/business")
        public R<Void> business() { throw new ApiException(ErrorCode.STATE_CONFLICT); }
        @GetMapping("/api/customer/probe/failure")
        public R<Void> failure() { throw new IllegalStateException("SELECT password_hash secret-token"); }
        @GetMapping("/api/customer/probe/async")
        public Callable<R<String>> async() {
            long accountId = UserContext.require().accountId();
            return () -> {
                if (UserContext.get() != null) throw new IllegalStateException("ThreadLocal leaked");
                return R.ok(Long.toString(accountId));
            };
        }
    }
}
