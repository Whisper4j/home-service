package com.homeservice;

import static org.assertj.core.api.Assertions.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.*;
import com.homeservice.common.constant.MessageConstant;
import com.homeservice.common.domain.*;
import com.homeservice.domain.dto.account.*;
import com.homeservice.domain.dto.catalog.SkuDTO;
import com.homeservice.domain.dto.order.*;
import com.homeservice.domain.dto.schedule.*;
import com.homeservice.domain.vo.address.AddressVO;
import com.homeservice.domain.vo.error.ErrorDetailsVO;
import com.homeservice.domain.vo.order.*;
import com.homeservice.enums.*;
import com.homeservice.support.ProtocolTestBase;
import com.homeservice.utils.Pages;

import jakarta.validation.Validator;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Set;

/**
 * 契约模型测试类
 * 验证契约模型相关行为
 */
@RequiredArgsConstructor
class ContractModelsTest extends ProtocolTestBase {

    private final ObjectMapper json;
    private final Validator validator;
    private static final String SKU =
            """
        {"itemId":"1","name":"日常清洁","standardPrice":"100.00","minimumOfferPrice":"80.00",
         "durationMinutes":120,"unit":"次","skillIds":["1"],"status":"ON_SHELF","supportsOffer":true,
         "description":"说明","included":"包含","excluded":"排除","customerSuppliesParts":false,"clientEntryCode":null}
        """;

    /**
     * 验证money编号集合数量与SecondsHaveDifferentWire集合场景
     */
    @Test
    void moneyIdsCountsAndSecondsHaveDifferentWireTypes() throws Exception {
        OrderVO value = new OrderVO();
        value.setId(9007199254740993L);
        value.setCurrentPrice(new BigDecimal("128"));
        value.setCreatedAt(OffsetDateTime.parse("2026-10-08T00:01:02.123Z"));
        value.setPriceVersion(2);
        JsonNode tree =
                json.readTree(
                        json.writeValueAsString(
                                Result.success(PageDTO.of(List.of(value), 41, 20))));
        assertThat(tree.path("code").asText()).isEqualTo("SUCCESS");
        assertThat(tree.at("/data/list/0/id").asText()).isEqualTo("9007199254740993");
        assertThat(tree.at("/data/list/0/currentPrice").asText()).isEqualTo("128.00");
        assertThat(tree.at("/data/list/0/createdAt").asText())
                .isEqualTo("2026-10-08T08:01:02+08:00");
        assertThat(tree.at("/data/total").isIntegralNumber()).isTrue();
        assertThat(tree.at("/data/list/0/priceVersion").isIntegralNumber()).isTrue();
        Page<String> page = new Page<>(10, 20, 41);
        page.setRecords(List.of());
        assertThat(Pages.response(page, x -> x).getPages()).isEqualTo(3);
        assertThat(Pages.response(page, x -> x).getTotal()).isEqualTo(41);
    }

    /**
     * 验证requiredNullable与OptionalFieldsRemainDistinct场景
     */
    @Test
    void requiredNullableAndOptionalFieldsRemainDistinct() throws Exception {
        JsonNode address = json.readTree(json.writeValueAsString(new AddressVO()));
        assertThat(address.has("longitude")).isTrue();
        assertThat(address.get("longitude").isNull()).isTrue();
        JsonNode order = json.readTree(json.writeValueAsString(new OrderVO()));
        assertThat(order.has("dealPrice")).isFalse();
        assertThat(order.has("closedAt")).isFalse();
        assertThat(json.readTree(json.writeValueAsString(new ErrorDetailsVO())).size())
                .isZero();
        OrderHistoryVO history = new OrderHistoryVO();
        history.setAssignments(List.of());
        assertThat(
                        json.readTree(
                                        json.writeValueAsString(history))
                                .get("assignments")
                                .isArray())
                .isTrue();
        assertThat(json.readValue(SKU, SkuDTO.class).getClientEntryCode()).isNull();
        assertThatThrownBy(
                        () ->
                                json.readValue(
                                        SKU.replace(",\"clientEntryCode\":null", ""), SkuDTO.class))
                .isInstanceOf(JsonMappingException.class);
    }

    /**
     * 验证optional输入CanBeMissingButNot显式空值场景
     */
    @Test
    void optionalInputCanBeMissingButNotExplicitNull() throws Exception {
        String minimal =
                "{\"skuId\":\"1\",\"addressId\":\"2\",\"bookingType\":\"STANDARD\",\"startTime\":\"2026-10-08T09:00:00+08:00\"}";
        assertThat(validator.validate(json.readValue(minimal, CreateOrderDTO.class))).isEmpty();
        assertThatThrownBy(
                        () ->
                                json.readValue(
                                        minimal.replace("}", ",\"remark\":null}"),
                                        CreateOrderDTO.class))
                .isInstanceOf(JsonMappingException.class);
    }

    /**
     * 验证unknownCoerced与MalformedInputsAreRejected场景
     */
    @Test
    void unknownCoercedAndMalformedInputsAreRejected() throws Exception {
        String valid = "{\"expectedPrice\":\"128.00\",\"priceVersion\":1}";
        assertThat(json.readValue(valid, ClaimOfferDTO.class).getExpectedPrice())
                .isEqualByComparingTo("128.00");
        for (String bad :
                List.of(
                        valid.replace("\"128.00\"", "128.00"),
                        valid.replace("\"128.00\"", "\"128.0\""),
                        valid.replace(":1}", ":1.0}"),
                        valid.replace(":1}", ":\"1\"}"),
                        valid.replace("}", ",\"unknown\":1}"),
                        valid.replace("}", ",\"priceVersion\":2}"))) {
            assertThatThrownBy(() -> json.readValue(bad, ClaimOfferDTO.class))
                    .as(bad)
                    .isInstanceOf(com.fasterxml.jackson.core.JsonProcessingException.class);
        }
        for (String id : List.of("0", "01", "-1", "1.0", "9223372036854775808"))
            assertThatThrownBy(
                            () ->
                                    json.readValue(
                                            SKU.replace(
                                                    "\"itemId\":\"1\"",
                                                    "\"itemId\":\"" + id + "\""),
                                            SkuDTO.class))
                    .isInstanceOf(JsonMappingException.class);
        assertThatThrownBy(
                        () ->
                                json.readValue(
                                        SKU.replace("\"itemId\":\"1\"", "\"itemId\":1"),
                                        SkuDTO.class))
                .isInstanceOf(JsonMappingException.class);
        assertThatThrownBy(
                        () ->
                                json.readValue(
                                        SKU.replace("\"ON_SHELF\"", "\"UNKNOWN\""), SkuDTO.class))
                .isInstanceOf(JsonMappingException.class);
        assertThatThrownBy(() -> json.readValue(SKU.replace("\"ON_SHELF\"", "0"), SkuDTO.class))
                .isInstanceOf(JsonMappingException.class);
        for (String time :
                List.of(
                        "2026-10-08T09:00:00",
                        "2026-10-08T09:00:00Z",
                        "2026-02-30T09:00:00+08:00",
                        "2026-10-08T09:00:00.001+08:00"))
            assertThatThrownBy(() -> json.readValue("\"" + time + "\"", OffsetDateTime.class))
                    .isInstanceOf(JsonMappingException.class);
    }

    /**
     * 验证stringsTrimBut密码PreservesBytes与Length场景
     */
    @Test
    void stringsTrimButPasswordPreservesBytesAndLength() throws Exception {
        LoginDTO login =
                json.readValue(
                        "{\"username\":\"  test_user  \",\"password\":\"  password  \"}",
                        LoginDTO.class);
        assertThat(login.getUsername()).isEqualTo("test_user");
        assertThat(login.getPassword()).isEqualTo("  password  ");
        assertThat(login.toString()).doesNotContain("password  ");
        assertThat(validator.validate(login("test_user", "汉".repeat(24)))).isEmpty();
        assertThat(validator.validate(login("test_user", "汉".repeat(25)))).isNotEmpty();
        assertThat(validator.validate(login("test_user", "short"))).isNotEmpty();
    }

    /**
     * 验证报价校验只返回面向用户的提示，不暴露内部字段含义。
     */
    @Test
    void offerValidationMessagesAreFriendly() {
        ChangeOfferDTO missing = new ChangeOfferDTO();
        Set<String> missingMessages =
                validator.validate(missing).stream()
                        .map(jakarta.validation.ConstraintViolation::getMessage)
                        .collect(java.util.stream.Collectors.toSet());
        assertThat(missingMessages)
                .contains(
                        MessageConstant.OFFER_PRICE_REQUIRED,
                        MessageConstant.OFFER_CONTEXT_EXPIRED,
                        MessageConstant.PAYMENT_CONFIRMATION_REQUIRED)
                .noneMatch(message -> message.contains("new") || message.contains("版本"));

        ChangeOfferDTO tooLarge = new ChangeOfferDTO();
        tooLarge.setNewPrice(new BigDecimal("1000000000.00"));
        tooLarge.setExpectedPrice(new BigDecimal("128.00"));
        tooLarge.setPriceVersion(1);
        tooLarge.setConfirmSimulatedPayment(true);
        assertThat(validator.validate(tooLarge))
                .extracting(jakarta.validation.ConstraintViolation::getMessage)
                .contains(MessageConstant.AMOUNT_TOO_LARGE)
                .noneMatch(message -> message.contains("999999"));
    }

    /**
     * 验证collection与Cross字段ValidationIsReal场景
     */
    @Test
    void collectionAndCrossFieldValidationIsReal() throws Exception {
        AccountProfileDTO profile = new AccountProfileDTO();
        profile.setDisplayName("");
        profile.setPhone("123");
        assertThat(validator.validate(profile))
                .extracting(jakarta.validation.ConstraintViolation::getMessage)
                .contains(MessageConstant.DISPLAY_NAME_REQUIRED, MessageConstant.PHONE_INVALID);
        assertThat(validator.validate(json.readValue(SKU, SkuDTO.class))).isEmpty();
        assertThat(validator.validate(json.readValue(SKU.replace("120,", "121,"), SkuDTO.class)))
                .isNotEmpty();
        assertThat(
                        validator.validate(
                                json.readValue(
                                        SKU.replace("[\"1\"]", "[\"1\",\"1\"]"), SkuDTO.class)))
                .isNotEmpty();
        ScheduleDTO bad = new ScheduleDTO();
        bad.setIntervals(
                List.of(
                        interval(LocalTime.of(8, 0), LocalTime.of(12, 0)),
                        interval(LocalTime.of(11, 0), LocalTime.of(13, 0))));
        bad.setRestWeekdays(List.of(8));
        assertThat(validator.validate(bad)).hasSizeGreaterThanOrEqualTo(2);
        assertThat(
                        validator.validate(
                                json.readValue(
                                        "{\"score\":5,\"tags\":[\"UNKNOWN\"],\"content\":\"\"}",
                                        com.homeservice.domain.dto.review.ReviewDTO.class)))
                .isNotEmpty();
        assertThatThrownBy(
                        () ->
                                json.readValue(
                                        "{\"start\":\"08:15\",\"end\":\"12:00\"}",
                                        WorkIntervalDTO.class))
                .isInstanceOf(JsonMappingException.class);
        PageQuery page = new PageQuery();
        page.setPageSize(101);
        assertThat(validator.validate(page)).isNotEmpty();
    }

    private LoginDTO login(String username, String password) {
        LoginDTO dto = new LoginDTO();
        dto.setUsername(username);
        dto.setPassword(password);
        return dto;
    }

    private WorkIntervalDTO interval(LocalTime start, LocalTime end) {
        WorkIntervalDTO dto = new WorkIntervalDTO();
        dto.setStart(start);
        dto.setEnd(end);
        return dto;
    }
}
