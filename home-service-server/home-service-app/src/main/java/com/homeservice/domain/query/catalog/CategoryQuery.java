package com.homeservice.domain.query.catalog;
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


/** 对应 OpenAPI CategoryQuery；仅定义数据边界，不实现业务。 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryQuery extends com.homeservice.common.domain.PageQuery {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(min = 1, max = 100)
    private String keyword;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CatalogStatus status;
}
