package com.homeservice.support;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * 测试密钥工具类
 * 生成测试进程使用的随机JWT密钥
 */
public final class TestSecrets {

    public static final String JWT_BASE64 = randomKey();

    /**
     * 创建Test密钥实例
     */
    private TestSecrets() {
    }

    /**
     * 验证random密钥场景
     */
    private static String randomKey() {
        byte[] bytes = new byte[64];
        new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
