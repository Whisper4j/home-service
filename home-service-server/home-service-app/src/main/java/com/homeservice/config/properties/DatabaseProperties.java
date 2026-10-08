package com.homeservice.config.properties;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
@Getter @Setter @Validated
@ConfigurationProperties("home.database")
public class DatabaseProperties {
    @NotBlank private String host;
    @Min(1) @Max(65535) private int port;
    @NotBlank @Pattern(regexp = "[A-Za-z0-9_]+") private String name;
    @NotBlank private String username;
    @NotBlank private String password;
    @NotBlank private String sslMode;
    private boolean allowPublicKeyRetrieval;
}
