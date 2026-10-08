package com.homeservice.domain.dto.notification;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.homeservice.handler.json.*;
import com.homeservice.validation.*;
import com.homeservice.enums.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.UniqueElements;
import lombok.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


/** 浏览器原生 WebSocket 不能设置 Authorization 请求头。连接同源 /ws 后 5 秒内发首帧 AUTH，服务端在校验前不发送业务数据；不得把 JWT 放 URL。认证失败或过期以 4401 关闭，账号禁用/角色非法以 4403 关闭。 */
@Builder
public record WsAuthFrame(
    @JsonProperty(value = "type", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    WsAuthType type,

    @JsonProperty(value = "accessToken", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 2048)
    @NotBlank
    String accessToken
) { @Override public String toString() { return "WsAuthFrame[credentials=REDACTED]"; } }
