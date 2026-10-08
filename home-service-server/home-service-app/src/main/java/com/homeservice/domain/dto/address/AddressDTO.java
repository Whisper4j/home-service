package com.homeservice.domain.dto.address;
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


/** 客户仅提交联系人、电话、行政区、详细地址及默认标记，不接受经纬度。仅支持广东省广州市，编码和名称必须匹配。设置默认地址原子清除旧默认；首个地址自动默认，删除或取消默认后最早创建的其他地址成为默认。 */
@Builder
public record AddressDTO(
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

    @JsonProperty(value = "isDefault", required = true)
    @JsonSetter(nulls = Nulls.FAIL)
    @NotNull
    Boolean isDefault
) {}
