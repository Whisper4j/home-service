package com.homeservice.domain.vo.attachment;
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


/** 现场图片不可变引用元数据，无公开 URL、客户身份或原始文件名。通过当前角色的鉴权内容接口读取；订单保存这些引用快照。 */
@Builder
public record SceneImageVO(
    @JsonProperty(value = "id", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long id,

    @JsonProperty(value = "mimeType", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    ImageMimeType mimeType,

    @JsonProperty(value = "size", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(1)
    @Max(5242880)
    Integer size,

    @JsonProperty(value = "createdAt", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime createdAt
) {}
