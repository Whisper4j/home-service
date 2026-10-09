package com.homeservice;

import static org.assertj.core.api.Assertions.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeservice.domain.po.account.*;
import com.homeservice.domain.po.catalog.ServiceSku;
import com.homeservice.domain.po.idempotency.HttpIdempotencyRecord;
import com.homeservice.domain.po.review.ServiceReview;
import com.homeservice.domain.po.settings.PlatformSetting;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.mapper.account.*;
import com.homeservice.mapper.catalog.ServiceSkuMapper;
import com.homeservice.mapper.idempotency.HttpIdempotencyRecordMapper;
import com.homeservice.mapper.review.ServiceReviewMapper;
import com.homeservice.mapper.settings.PlatformSettingMapper;
import com.homeservice.service.account.IAccountQueryService;
import com.homeservice.support.*;
import com.homeservice.utils.StrictBcryptPasswordEncoder;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

import javax.sql.DataSource;

/**
 * MySQL映射集成测试类
 * 验证MySQL映射相关行为
 */
@SpringBootTest(
        classes = HomeServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MySqlMappingIT {

    private final DataSource dataSource;
    private final ApplicationContext context;
    private final AuthAccountMapper accounts;
    private final WorkerProfileMapper workers;
    private final ServiceSkuMapper skus;
    private final ServiceReviewMapper reviews;
    private final PlatformSettingMapper settings;
    private final HttpIdempotencyRecordMapper idempotency;
    private final IAccountQueryService accountQuery;
    private final ObjectMapper json;
    private final PlatformTransactionManager transactionManager;
    private static boolean schemaVerified;

    /**
     * 验证secret场景
     */
    @DynamicPropertySource
    static void secret(DynamicPropertyRegistry properties) {
        properties.add("home.jwt.secret-base64", () -> TestSecrets.JWT_BASE64);
    }

    /**
     * 验证actualSchemaMatchesUniqueSQLBaseline前置AnyFixture场景
     */
    @Test
    @Order(1)
    void actualSchemaMatchesUniqueSqlBaselineBeforeAnyFixture() throws Exception {
        var expected = SqlBaseline.read();
        var db = new JdbcTemplate(dataSource);
        var tables =
                db.queryForList(
                        "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA=DATABASE()",
                        String.class);
        assertThat(new HashSet<>(tables)).isEqualTo(expected.keySet());
        assertThat(
                        db.queryForObject(
                                "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA=DATABASE() AND (ENGINE <> 'InnoDB' OR TABLE_COLLATION <> 'utf8mb4_0900_ai_ci')",
                                Integer.class))
                .isZero();
        assertThat(
                        db.queryForObject(
                                "SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS WHERE TABLE_SCHEMA=DATABASE() AND CONSTRAINT_TYPE='FOREIGN KEY'",
                                Integer.class))
                .isZero();
        for (var table : expected.values()) {
            var columns =
                    db.queryForList(
                            "SELECT COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_DEFAULT,EXTRA,COLLATION_NAME,GENERATION_EXPRESSION FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME=? ORDER BY ORDINAL_POSITION",
                            table.name());
            assertThat(columns).hasSize(table.columns().size());
            for (var column : columns) {
                var exp = table.columns().get(column.get("COLUMN_NAME"));
                assertThat(exp).isNotNull();
                assertThat(column.get("COLUMN_TYPE")).isEqualTo(exp.type());
                assertThat(column.get("IS_NULLABLE")).isEqualTo(exp.nullable() ? "YES" : "NO");
                assertThat(String.valueOf(column.get("EXTRA")).contains("auto_increment"))
                        .isEqualTo(exp.auto());
                assertThat(
                                String.valueOf(column.get("EXTRA"))
                                        .contains("on update CURRENT_TIMESTAMP"))
                        .isEqualTo(exp.definition().contains("ON UPDATE CURRENT_TIMESTAMP"));
                assertThat(SqlBaseline.normalize((String) column.get("COLUMN_DEFAULT")))
                        .isEqualTo(SqlBaseline.normalize(exp.defaultValue()));
                assertThat(column.get("COLLATION_NAME")).isEqualTo(exp.collation());
                if (exp.definition().contains("GENERATED ALWAYS")) {
                    String expression =
                            exp.definition()
                                    .substring(
                                            exp.definition().indexOf(" AS (") + 5,
                                            exp.definition().indexOf(") STORED"));
                    assertThat(SqlBaseline.normalize((String) column.get("GENERATION_EXPRESSION")))
                            .isEqualTo(SqlBaseline.normalize(expression));
                    assertThat(column.get("EXTRA")).isEqualTo("STORED GENERATED");
                } else assertThat(column.get("GENERATION_EXPRESSION")).isEqualTo("");
            }
            Map<String, List<String>> indexes = new TreeMap<>();
            for (var index :
                    db.queryForList(
                            "SELECT INDEX_NAME,COLUMN_NAME,NON_UNIQUE FROM information_schema.STATISTICS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME=? ORDER BY INDEX_NAME,SEQ_IN_INDEX",
                            table.name())) {
                assertThat(((Number) index.get("NON_UNIQUE")).intValue()).isZero();
                indexes.computeIfAbsent((String) index.get("INDEX_NAME"), x -> new ArrayList<>())
                        .add((String) index.get("COLUMN_NAME"));
            }
            assertThat(indexes).isEqualTo(table.indexes());
            Map<String, String> checks = new TreeMap<>();
            for (var check :
                    db.queryForList(
                            "SELECT t.CONSTRAINT_NAME,c.CHECK_CLAUSE,t.ENFORCED FROM information_schema.TABLE_CONSTRAINTS t JOIN information_schema.CHECK_CONSTRAINTS c USING(CONSTRAINT_SCHEMA,CONSTRAINT_NAME) WHERE t.TABLE_SCHEMA=DATABASE() AND t.TABLE_NAME=?",
                            table.name())) {
                assertThat(check.get("ENFORCED")).isEqualTo("YES");
                checks.put(
                        (String) check.get("CONSTRAINT_NAME"),
                        SqlBaseline.normalize((String) check.get("CHECK_CLAUSE")));
            }
            Map<String, String> normalized = new TreeMap<>();
            table.checks().forEach((k, v) -> normalized.put(k, SqlBaseline.normalize(v)));
            assertThat(checks).isEqualTo(normalized);
        }
        schemaVerified = true;
    }

    /**
     * 验证allRegisteredMappersExecute读取Only字段Projections场景
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    @Order(2)
    void allRegisteredMappersExecuteReadOnlyColumnProjections() throws Exception {
        assertThat(schemaVerified).isTrue();
        var entities = SqlBaseline.entities();
        assertThat(context.getBeansOfType(BaseMapper.class)).hasSize(27);
        for (var type : entities.values()) {
            Class<?> mapperType =
                    Class.forName(type.getName().replace(".domain.po.", ".mapper.") + "Mapper");
            BaseMapper mapper = (BaseMapper) context.getBean(mapperType);
            assertThat(mapper.selectList(new QueryWrapper<>().apply("1=0"))).isEmpty();
            assertThat(TableInfoHelper.getTableInfo(type).getFieldList()).isNotEmpty();
        }
        assertThat(accountQuery.getClass().getName()).doesNotContain("ProtocolTest");
    }

    /**
     * 验证minimumFixturesRoundTrip与RollbackOnTheSame连接场景
     */
    @Test
    @Order(3)
    void minimumFixturesRoundTripAndRollbackOnTheSameConnection() throws Exception {
        assertThat(schemaVerified).isTrue();
        String username = "it_" + UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        List<Long> accountIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        var transaction = new TransactionTemplate(transactionManager);
        transaction.executeWithoutResult(
                status -> {
                    try {
                        AuthAccount account = new AuthAccount();
                        account.setUsername(username);
                        account.setPasswordHash(
                                new StrictBcryptPasswordEncoder()
                                        .encode(UUID.randomUUID().toString()));
                        account.setRole(Role.WORKER);
                        account.setStatus(AccountStatus.ENABLED);
                        account.setDisplayName("映射测试夹具");
                        account.setPhone("13800000000");
                        assertThat(accounts.insert(account)).isEqualTo(1);
                        accountIds.add(account.getId());
                        AuthAccount loaded = accounts.selectById(account.getId());
                        assertThat(loaded.getRole()).isEqualTo(Role.WORKER);
                        assertThat(loaded.getCreatedAt()).isEqualTo(account.getCreatedAt());
                        assertThat(
                                        accountQuery
                                                .findByAccountId(account.getId())
                                                .orElseThrow()
                                                .getAccountId())
                                .isEqualTo(account.getId());
                        WorkerProfile worker = new WorkerProfile();
                        worker.setAccountId(account.getId());
                        worker.setCityCode("440100");
                        workers.insert(worker);
                        assertThat(workers.selectById(worker.getId()).getWorkIntervals()).isNull();
                        worker.setWorkIntervals(
                                List.of(
                                        new WorkIntervalValue(
                                                LocalTime.of(8, 0), LocalTime.of(12, 0))));
                        worker.setRestWeekdays(List.of());
                        workers.updateById(worker);
                        WorkerProfile schedule = workers.selectById(worker.getId());
                        assertThat(schedule.getWorkIntervals())
                                .containsExactly(
                                        new WorkIntervalValue(
                                                LocalTime.of(8, 0), LocalTime.of(12, 0)));
                        assertThat(schedule.getRestWeekdays()).isEmpty();
                        worker.setRestWeekdays(List.of(1, 7));
                        workers.updateById(worker);
                        assertThat(workers.selectById(worker.getId()).getRestWeekdays())
                                .containsExactly(1, 7);
                        // 验证 JSON NULL 更新必须显式指定 SQL NULL，不能依赖默认 NOT_NULL 更新策略。
                        workers.update(
                                null,
                                new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<
                                                WorkerProfile>()
                                        .eq("id", worker.getId())
                                        .set("work_intervals", null)
                                        .set("rest_weekdays", null));
                        assertThat(workers.selectById(worker.getId()).getRestWeekdays()).isNull();
                        ServiceSku sku = new ServiceSku();
                        sku.setItemId(1L);
                        sku.setName("映射测试");
                        sku.setStandardPrice(new BigDecimal("128.00"));
                        sku.setMinimumOfferPrice(new BigDecimal("100.00"));
                        sku.setDurationMinutes(120);
                        sku.setUnit("次");
                        sku.setStatus(CatalogStatus.ON_SHELF);
                        sku.setSupportsOffer(true);
                        sku.setDescription("测试");
                        sku.setIncluded("测试");
                        sku.setExcluded("测试");
                        skus.insert(sku);
                        skuIds.add(sku.getId());
                        assertThat(skus.selectById(sku.getId()).getStandardPrice())
                                .isEqualByComparingTo("128.00");
                        var page =
                                skus.selectPage(
                                        new com.baomidou.mybatisplus.extension.plugins.pagination
                                                .Page<>(2, 1),
                                        new QueryWrapper<ServiceSku>().eq("id", sku.getId()));
                        assertThat(page.getRecords()).isEmpty();
                        assertThat(page.getTotal()).isEqualTo(1);
                        assertThat(page.getPages()).isEqualTo(1);
                        skus.deleteById(sku.getId());
                        assertThat(skus.selectById(sku.getId())).isNull();
                        assertThat(
                                        new JdbcTemplate(dataSource)
                                                .queryForObject(
                                                        "SELECT deleted_at IS NOT NULL FROM service_sku WHERE id=?",
                                                        Boolean.class,
                                                        sku.getId()))
                                .isTrue();
                        // 关联测试只使用本事务自己的标识；本阶段无业务约束 Service，不构造真实履约订单。
                        ServiceReview review = new ServiceReview();
                        review.setOrderId(-account.getId());
                        review.setCustomerId(account.getId());
                        review.setWorkerId(worker.getId());
                        review.setScore(5);
                        review.setTags(List.of());
                        reviews.insert(review);
                        assertThat(reviews.selectById(review.getId()).getTags()).isEmpty();
                        review.setTags(List.of("PUNCTUAL", "FRIENDLY"));
                        reviews.updateById(review);
                        assertThat(reviews.selectById(review.getId()).getTags())
                                .containsExactly("PUNCTUAL", "FRIENDLY");
                        // 单例若已存在只读，不修改已有设置；空库才建立可回滚的一条最小设置。
                        if (settings.selectById(1) == null) {
                            PlatformSetting setting = new PlatformSetting();
                            setting.setId(1);
                            setting.setCityCode("440100");
                            setting.setSceneImageMimeTypes(
                                    List.of("image/jpeg", "image/png", "image/webp"));
                            settings.insert(setting);
                            var read = settings.selectById(1);
                            assertThat(read.getSceneImageMimeTypes())
                                    .containsExactly("image/jpeg", "image/png", "image/webp");
                            assertThat(read.getWorkStart()).isEqualTo(LocalTime.of(8, 0));
                            assertThat(read.getPriceStep()).isEqualByComparingTo("5.00");
                        }
                        HttpIdempotencyRecord record = new HttpIdempotencyRecord();
                        record.setScopeType("ACCOUNT");
                        record.setAccountId(account.getId());
                        record.setScopeIdentity("must-not-be-written");
                        record.setHttpMethod("POST");
                        record.setRequestPath("/api/customer/orders");
                        record.setIdempotencyKey("it_" + UUID.randomUUID());
                        record.setRequestFingerprint("a".repeat(64));
                        record.setExpiresAt(
                                LocalDateTime.now(ZoneId.of("Asia/Shanghai"))
                                        .plusDays(2)
                                        .withNano(0));
                        idempotency.insert(record);
                        assertThat(idempotency.selectById(record.getId()).getScopeIdentity())
                                .isEqualTo("ACCOUNT:" + account.getId());
                        record.setExecutionStatus("SUCCEEDED");
                        record.setResponseHttpStatus(200);
                        record.setResponseContentType("application/json");
                        record.setResponseJson(
                                new IdempotencyResponse(
                                        "SUCCESS",
                                        "成功",
                                        json.createObjectNode().put("success", true)));
                        record.setCompletedAt(
                                LocalDateTime.now(ZoneId.of("Asia/Shanghai")).withNano(0));
                        idempotency.updateById(record);
                        var replay = idempotency.selectById(record.getId());
                        assertThat(replay.getResponseJson().getData().get("success").asBoolean())
                                .isTrue();
                        assertThat(replay.getScopeIdentity())
                                .isEqualTo("ACCOUNT:" + account.getId());
                    } finally {
                        status.setRollbackOnly();
                    }
                });
        assertThat(accounts.selectById(accountIds.get(0))).isNull();
        assertThat(
                        new JdbcTemplate(dataSource)
                                .queryForObject(
                                        "SELECT COUNT(*) FROM service_sku WHERE id=?",
                                        Integer.class,
                                        skuIds.get(0)))
                .isZero();
    }
}
