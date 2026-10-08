package com.homeservice.config;
import com.homeservice.interceptor.AuthenticationInterceptor;
import com.homeservice.handler.json.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.*;
import java.time.*;
@Configuration(proxyBeanMethods = false) @RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthenticationInterceptor authenticationInterceptor;
    public void addInterceptors(InterceptorRegistry registry) { registry.addInterceptor(authenticationInterceptor).addPathPatterns("/api/**"); }
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, Long.class, IdDeserializer::parse);
        registry.addConverter(String.class, Integer.class, input -> {
            String value = input.strip();
            if (!value.matches("-?[0-9]+")) throw new IllegalArgumentException("需要整数");
            return Integer.valueOf(value);
        });
        registry.addConverter(String.class, Boolean.class, input -> {
            String value = input.strip();
            if (!value.equals("true") && !value.equals("false")) throw new IllegalArgumentException("需要 true/false");
            return Boolean.valueOf(value);
        });
        registry.addConverter(String.class, LocalDate.class, StrictLocalDateDeserializer::parse);
        registry.addConverter(String.class, LocalTime.class, HalfHourTimeDeserializer::parse);
        registry.addConverter(String.class, OffsetDateTime.class, ShanghaiDateTimeDeserializer::parse);
    }
}
