package com.homeservice;

import static org.assertj.core.api.Assertions.assertThat;

import com.homeservice.common.constant.MessageConstant;
import com.homeservice.enums.ErrorCode;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 提示信息常量使用约束测试。
 * 防止新增校验或异常时再次散落直接面向用户的文字。
 */
class MessageConstantUsageTest {

    private static final Pattern LITERAL_VALIDATION_MESSAGE =
            Pattern.compile("message\\s*=\\s*\"");
    private static final Pattern LITERAL_EXCEPTION_MESSAGE =
            Pattern.compile("throw\\s+new\\s+[\\w$.<>]+\\s*\\(\\s*\"");

    @Test
    void validationAndExceptionMessagesUseMessageConstant() throws IOException {
        Path module = Path.of("").toAbsolutePath();
        Path server = Files.isDirectory(module.resolve("home-service-app")) ? module : module.getParent();

        for (Path sourceRoot :
                Set.of(
                        server.resolve("home-service-app/src/main/java"),
                        server.resolve("home-service-common/src/main/java"))) {
            try (var sources = Files.walk(sourceRoot)) {
                for (Path source : sources.filter(path -> path.toString().endsWith(".java")).toList()) {
                    if (source.getFileName().toString().equals("MessageConstant.java")) {
                        continue;
                    }
                    String content = Files.readString(source);
                    assertThat(content)
                            .as("校验提示应引用 MessageConstant: %s", source)
                            .doesNotContainPattern(LITERAL_VALIDATION_MESSAGE);
                    assertThat(content)
                            .as("异常提示应引用 MessageConstant: %s", source)
                            .doesNotContainPattern(LITERAL_EXCEPTION_MESSAGE);
                }
            }
        }
    }

    @Test
    void errorCodeMessagesComeFromMessageConstant() throws IllegalAccessException {
        Set<String> declaredMessages =
                java.util.Arrays.stream(MessageConstant.class.getDeclaredFields())
                        .filter(field -> Modifier.isStatic(field.getModifiers()))
                        .filter(field -> field.getType() == String.class)
                        .map(
                                field -> {
                                    try {
                                        return (String) field.get(null);
                                    } catch (IllegalAccessException e) {
                                        throw new IllegalStateException(e);
                                    }
                                })
                        .collect(Collectors.toSet());

        assertThat(java.util.Arrays.stream(ErrorCode.values()).map(ErrorCode::message))
                .allMatch(declaredMessages::contains);
    }
}
