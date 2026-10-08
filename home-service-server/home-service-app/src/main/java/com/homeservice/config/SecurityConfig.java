package com.homeservice.config;
import com.homeservice.config.properties.JwtProperties;
import com.homeservice.utils.StrictBcryptPasswordEncoder;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Clock;
import java.util.Base64;
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {
    @Bean public Clock clock() { return Clock.systemUTC(); }
    @Bean public PasswordEncoder passwordEncoder() { return new StrictBcryptPasswordEncoder(); }
    @Bean public SecretKey jwtSigningKey(JwtProperties properties) {
        if (!properties.isStrongKey()) throw new IllegalArgumentException("JWT 密钥配置无效");
        return new SecretKeySpec(Base64.getDecoder().decode(properties.getSecretBase64()), "HmacSHA256");
    }
}
