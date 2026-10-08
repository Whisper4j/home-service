package com.homeservice.support;

import java.security.SecureRandom;
import java.util.Base64;

/** 每次测试进程产生新密钥，仓库和测试资源都不保存可用密钥。 */
public final class TestSecrets {
    public static final String JWT_BASE64 = randomKey();
    private TestSecrets() {}
    private static String randomKey() {
        byte[] bytes = new byte[64];
        new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
