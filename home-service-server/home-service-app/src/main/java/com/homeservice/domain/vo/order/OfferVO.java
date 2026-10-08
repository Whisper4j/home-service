package com.homeservice.domain.vo.order;
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

import com.homeservice.domain.vo.attachment.SceneImageVO;
/** 抢单前最小信息集，无客户 ID、门牌、电话、联系人或开始码。仅符合条件的人员可见。 sceneImages 仅允许当前符合接单资格的人员通过鉴权接口查看。提示客户避免拍入隐私；不得在抢单池暴露结构化门牌、联系方式或开始码。 */
@Builder
public record OfferVO(
    @JsonProperty(value = "id", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long id,

    @JsonProperty(value = "skuName", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 80)
    @NotBlank
    String skuName,

    @JsonProperty(value = "districtName", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 40)
    @NotBlank
    String districtName,

    @JsonProperty(value = "cityCode", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 6)
    @NotBlank
    String cityCode,

    @JsonProperty(value = "durationMinutes", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(30)
    @Max(720)
    Integer durationMinutes,

    @JsonProperty(value = "startTime", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime startTime,

    @JsonProperty(value = "endTime", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime endTime,

    @JsonProperty(value = "bufferEndTime", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime bufferEndTime,

    @JsonProperty(value = "currentPrice", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiMoney
    @DecimalMin("0.00")
    @DecimalMax("999999999.99")
    @Digits(integer = 9, fraction = 2)
    BigDecimal currentPrice,

    @JsonProperty(value = "priceVersion", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Min(1)
    @Max(2147483647)
    Integer priceVersion,

    @JsonProperty(value = "offerDeadline", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime offerDeadline,

    @JsonProperty(value = "description", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 2000)
    @NotBlank
    String description,

    @JsonProperty(value = "included", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 1000)
    @NotBlank
    String included,

    @JsonProperty(value = "excluded", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 1000)
    @NotBlank
    String excluded,

    @JsonProperty(value = "customerSuppliesParts", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    Boolean customerSuppliesParts,

    @JsonProperty(value = "sceneImages", required = true)
    @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
    @NotNull
    @Size(min = 0, max = 3)
    @Valid
    List<@NotNull @Valid SceneImageVO> sceneImages,

    @JsonProperty(value = "publishedAt", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    OffsetDateTime publishedAt,

    @JsonProperty(value = "cityName", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 40)
    @NotBlank
    String cityName
) {}
