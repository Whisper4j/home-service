package com.homeservice.support;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.*;

@SpringBootTest(classes = ProtocolTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("protocol")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public abstract class ProtocolTestBase {
    @DynamicPropertySource static void secret(DynamicPropertyRegistry properties) {
        properties.add("home.jwt.secret-base64", () -> TestSecrets.JWT_BASE64);
        properties.add("home.jwt.token-ttl", () -> "2h");
    }
}
