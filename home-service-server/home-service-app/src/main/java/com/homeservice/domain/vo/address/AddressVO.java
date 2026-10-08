package com.homeservice.domain.vo.address;
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


/** 坐标由系统维护，当前未接地图服务，新地址坐标同时为空且可正常按城市预约。只改联系人、电话或默认标记保留已有系统坐标；行政区或详细地址变化后旧坐标同时失效为空，未来由地图服务重新解析。禁止随机坐标或用0,0冒充解析成功。地址解析与两位置间距离计算是不同能力，本接口不返回距离。 */
@Builder
public record AddressVO(
    @JsonProperty(value = "id", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @ApiId
    @Positive
    Long id,

    @JsonProperty(value = "contactName", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 40)
    @NotBlank
    String contactName,

    @JsonProperty(value = "contactPhone", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 11)
    @NotBlank
    @Pattern(regexp = "^1[0-9]{10}$")
    String contactPhone,

    @JsonProperty(value = "provinceCode", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 6)
    @NotBlank
    String provinceCode,

    @JsonProperty(value = "provinceName", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 40)
    @NotBlank
    String provinceName,

    @JsonProperty(value = "cityCode", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 6)
    @NotBlank
    String cityCode,

    @JsonProperty(value = "cityName", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 40)
    @NotBlank
    String cityName,

    @JsonProperty(value = "districtCode", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 6)
    @NotBlank
    String districtCode,

    @JsonProperty(value = "districtName", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 40)
    @NotBlank
    String districtName,

    @JsonProperty(value = "detail", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    @Size(min = 1, max = 200)
    @NotBlank
    String detail,

    @JsonProperty(value = "longitude", required = true)
    @DecimalMin("-180")
    @DecimalMax("180")
    BigDecimal longitude,

    @JsonProperty(value = "latitude", required = true)
    @DecimalMin("-90")
    @DecimalMax("90")
    BigDecimal latitude,

    @JsonProperty(value = "isDefault", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    Boolean isDefault
) {}
