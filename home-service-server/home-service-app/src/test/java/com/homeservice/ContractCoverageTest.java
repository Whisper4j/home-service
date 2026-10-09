package com.homeservice;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.homeservice.config.properties.AuthProperties;
import com.homeservice.enums.ErrorCode;

import org.junit.jupiter.api.Test;

import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 契约覆盖测试类
 * 验证契约覆盖相关行为
 */
class ContractCoverageTest {
    private static final Set<String> JAVA_ENUMS =
            Set.of(
                    "Role",
                    "AccountStatus",
                    "CatalogStatus",
                    "ServiceKind",
                    "BookingType",
                    "OrderStatus",
                    "ErrorCode");

    /**
     * 验证everyNamed契约ObjectHasEquivalentJava结构场景
     */
    @Test
    void everyNamedContractObjectHasEquivalentJavaShape() throws Exception {
        JsonNode contract =
                new YAMLMapper().readTree(Path.of("../../docs/api/openapi.yaml").toFile());
        Map<String, Class<?>> classes = new HashMap<>();
        try (var files = Files.walk(Path.of("src/main/java/com/homeservice/domain"))) {
            for (Path file : files.filter(x -> x.toString().endsWith(".java")).toList()) {
                String name =
                        Path.of("src/main/java")
                                .relativize(file)
                                .toString()
                                .replace('\\', '.')
                                .replace('/', '.')
                                .replace(".java", "");
                Class<?> type = Class.forName(name);
                classes.put(type.getSimpleName(), type);
            }
        }
        var fields = contract.path("components").path("schemas").fields();
        int objects = 0;
        while (fields.hasNext()) {
            var schema = fields.next();
            String name = schema.getKey();
            JsonNode node = schema.getValue();
            if (node.has("enum") && JAVA_ENUMS.contains(name)) {
                Class<?> type = Class.forName("com.homeservice.enums." + name);
                Set<String> actual =
                        Arrays.stream(type.getEnumConstants())
                                .map(
                                        value -> {
                                            try {
                                                return name.equals("ErrorCode")
                                                        ? ((ErrorCode) value).code()
                                                        : (String)
                                                                type.getMethod("getValue")
                                                                        .invoke(value);
                                            } catch (Exception e) {
                                                throw new IllegalStateException(e);
                                            }
                                        })
                                .collect(Collectors.toSet());
                for (Object value : type.getEnumConstants()) {
                    String description =
                            name.equals("ErrorCode")
                                    ? ((ErrorCode) value).message()
                                    : (String) type.getMethod("getDescription").invoke(value);
                    assertThat(description).as(name).containsPattern("[\\p{IsHan}]");
                }
                Set<String> expected = new HashSet<>();
                node.get("enum").forEach(x -> expected.add(x.asText()));
                assertThat(actual).as(name).isEqualTo(expected);
            }
            if (!node.path("type").asText().equals("object")
                    || name.endsWith("Response")
                    || name.endsWith("PageDTO")
                    || name.equals("PageQuery")) continue;
            objects++;
            Class<?> type = classes.get(name);
            assertThat(type).as(name).isNotNull();
            Set<String> expected = new HashSet<>();
            node.path("properties").fieldNames().forEachRemaining(expected::add);
            assertThat(type.isRecord()).as(name + " 应使用普通 Lombok 类").isFalse();
            Set<String> actual = new HashSet<>();
            for (Class<?> cursor = type;
                    cursor != Object.class;
                    cursor = cursor.getSuperclass())
                for (var field : cursor.getDeclaredFields())
                    if (!java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                        actual.add(field.getName());
            assertThat(actual).as(name).isEqualTo(expected);
        }
        assertThat(objects).isEqualTo(77);
        try (var files = Files.list(Path.of("src/main/java/com/homeservice/enums"))) {
            Set<String> actualEnums =
                    files.filter(x -> x.toString().endsWith(".java"))
                            .map(x -> x.getFileName().toString().replace(".java", ""))
                            .collect(Collectors.toSet());
            assertThat(actualEnums).isEqualTo(JAVA_ENUMS);
        }
        Set<String> whitelist = new HashSet<>();
        contract.path("paths")
                .fields()
                .forEachRemaining(
                        path ->
                                path.getValue()
                                        .fields()
                                        .forEachRemaining(
                                                operation -> {
                                                    if (operation.getValue().has("security")
                                                            && operation
                                                                    .getValue()
                                                                    .get("security")
                                                                    .isEmpty())
                                                        whitelist.add(
                                                                operation
                                                                                .getKey()
                                                                                .toUpperCase(
                                                                                        Locale.ROOT)
                                                                        + " /api"
                                                                        + path.getKey());
                                                }));
        assertThat(AuthProperties.CONTRACT_PUBLIC_ENDPOINTS).isEqualTo(whitelist);
    }
}
