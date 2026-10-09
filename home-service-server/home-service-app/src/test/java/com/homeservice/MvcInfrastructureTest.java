package com.homeservice;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.homeservice.enums.*;
import com.homeservice.support.ProtocolTestBase;
import com.homeservice.utils.*;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.*;
import org.springframework.test.web.servlet.MockMvc;

/**
 * MVC基础设施测试类
 * 验证MVC基础设施相关行为
 */
@RequiredArgsConstructor
class MvcInfrastructureTest extends ProtocolTestBase {

    private final MockMvc mvc;
    private final JwtTool jwt;

    /**
     * 解析Bearer访问令牌
     */
    private String bearer(long id, Role role) {
        return "Bearer " + jwt.createToken(id, role);
    }

    /**
     * 验证clean上下文场景
     */
    @AfterEach
    void cleanContext() {
        assertThat(UserContext.get()).isNull();
    }

    /**
     * 验证authenticationUses角色与当前账号状态场景
     */
    @Test
    void authenticationUsesRoleAndCurrentAccountState() throws Exception {
        mvc.perform(get("/api/customer/probe"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHENTICATED"));
        mvc.perform(get("/api/customer/probe").header("Authorization", bearer(1, Role.CUSTOMER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("1"));
        mvc.perform(get("/api/worker/probe").header("Authorization", bearer(2, Role.WORKER)))
                .andExpect(status().isOk());
        mvc.perform(get("/api/admin/probe").header("Authorization", bearer(3, Role.ADMIN)))
                .andExpect(status().isOk());
        mvc.perform(get("/api/worker/probe").header("Authorization", bearer(1, Role.CUSTOMER)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("FORBIDDEN"));
        mvc.perform(get("/api/customer/probe").header("Authorization", bearer(4, Role.CUSTOMER)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("ACCOUNT_DISABLED"));
        mvc.perform(get("/api/customer/probe").header("Authorization", bearer(5, Role.CUSTOMER)))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/customer/probe").header("Authorization", bearer(99, Role.CUSTOMER)))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/api/customer/probe").header("Authorization", "Basic invalid"))
                .andExpect(status().isUnauthorized());
        mvc.perform(
                        get("/api/customer/probe")
                                .header(
                                        "Authorization",
                                        bearer(1, Role.CUSTOMER),
                                        bearer(1, Role.CUSTOMER)))
                .andExpect(status().isUnauthorized());
    }

    /**
     * 验证whitelistIs方法Scoped与MissingPathsAreNotSuccess场景
     */
    @Test
    void whitelistIsMethodScopedAndMissingPathsAreNotSuccess() throws Exception {
        mvc.perform(get("/api/customer/skus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.pageSize").value(20));
        mvc.perform(post("/api/customer/skus")).andExpect(status().isUnauthorized());
        mvc.perform(
                        post("/api/customer/auth/login")
                                .contentType("application/json")
                                .content(
                                        "{\"username\":\"  test_user \",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("test_user"));
        mvc.perform(get("/api/customer/missing").header("Authorization", bearer(1, Role.CUSTOMER)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    /**
     * 验证jsonErrors与BusinessFailuresUseRealHTTP状态场景
     */
    @Test
    void jsonErrorsAndBusinessFailuresUseRealHttpStatus() throws Exception {
        String auth = bearer(1, Role.CUSTOMER);
        mvc.perform(
                        post("/api/customer/probe/money")
                                .header("Authorization", auth)
                                .contentType("application/json")
                                .content("{\"expectedPrice\":128.00,\"priceVersion\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.fieldErrors[0].field").exists());
        mvc.perform(
                        post("/api/customer/probe/money")
                                .header("Authorization", auth)
                                .contentType("application/json")
                                .content("{\"expectedPrice\":\"128.00\",\"priceVersion\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.data.fieldErrors[0].field").value("priceVersion"))
                .andExpect(jsonPath("$.data.fieldErrors[0].message").value("价格版本不能小于1"));
        mvc.perform(get("/api/customer/probe/business").header("Authorization", auth))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("STATE_CONFLICT"));
        mvc.perform(get("/api/customer/probe/failure").header("Authorization", auth))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"))
                .andExpect(
                        content()
                                .string(
                                        org.hamcrest.Matchers.not(
                                                org.hamcrest.Matchers.containsString(
                                                        "secret-token"))));
        mvc.perform(put("/api/customer/probe").header("Authorization", auth))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    /**
     * 验证query与Path绑定CannotBypassValidation场景
     */
    @Test
    void queryAndPathBindingCannotBypassValidation() throws Exception {
        for (String value : new String[] {"0", "1.5", "2147483648"})
            mvc.perform(get("/api/customer/skus").param("pageNo", value))
                    .andExpect(status().isBadRequest());
        mvc.perform(get("/api/customer/skus").param("pageSize", "101"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/customer/skus").param("status", "ON_SHELF"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/customer/skus").param("sortBy", "id"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/customer/skus").param("itemId", "01"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/customer/probe/01").header("Authorization", bearer(1, Role.CUSTOMER)))
                .andExpect(status().isBadRequest());
        mvc.perform(
                        get("/api/customer/probe/orders")
                                .header("Authorization", bearer(1, Role.CUSTOMER))
                                .param("status", "UNKNOWN"))
                .andExpect(status().isBadRequest());
        mvc.perform(
                        get("/api/customer/probe/orders")
                                .header("Authorization", bearer(1, Role.CUSTOMER))
                                .param("from", "2026-02-30"))
                .andExpect(status().isBadRequest());
        mvc.perform(
                        get("/api/customer/probe/orders")
                                .header("Authorization", bearer(1, Role.CUSTOMER))
                                .param("statuses", "COMPLETED,CANCELLED"))
                .andExpect(status().isOk());
        mvc.perform(
                        get("/api/customer/probe/orders")
                                .header("Authorization", bearer(1, Role.CUSTOMER))
                                .param("statuses", "COMPLETED,COMPLETED"))
                .andExpect(status().isBadRequest());
        mvc.perform(
                        get("/api/customer/probe/orders")
                                .header("Authorization", bearer(1, Role.CUSTOMER))
                                .param("statuses", "COMPLETED")
                                .param("status", "CANCELLED"))
                .andExpect(status().isBadRequest());
    }

    /**
     * 验证async派单DoesNotCarryThread本地To服务人员Thread场景
     */
    @Test
    void asyncDispatchDoesNotCarryThreadLocalToWorkerThread() throws Exception {
        var result =
                mvc.perform(
                                get("/api/customer/probe/async")
                                        .header("Authorization", bearer(1, Role.CUSTOMER)))
                        .andExpect(request().asyncStarted())
                        .andReturn();
        assertThat(UserContext.get()).isNull();
        mvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("1"));
    }
}
