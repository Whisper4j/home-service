package com.homeservice.domain.query.attachment;
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


/** 客户读取本人上传图片可省略 orderId；人员和管理员必须指定包含该图片的订单 ID，每次读取重新校验访问资格。 */
@Data
public class OrderSceneImageQuery {
    @NotNull
    @ApiId
    @Positive
    private Long orderId;
}
