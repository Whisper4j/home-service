package com.homeservice.config.properties;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.nio.file.Path;
/** 仅准备私有存储配置；未来上传需内容检查、EXIF 清理和业务资格鉴权。 */
@Getter @Setter @Validated
@ConfigurationProperties("home.upload")
public class UploadProperties { @NotNull private Path directory = Path.of("data/uploads"); }
