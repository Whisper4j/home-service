package com.homeservice.domain.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

import java.util.List;

import lombok.Data;

import org.hibernate.validator.constraints.UniqueElements;

@Data
public class ReviewDTO {

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分不能小于1")
    @Max(value = 5, message = "评分不能大于5")
    private Integer score; // 评分

    @NotNull(message = "评价标签（JSON）不能为空")
    @Size(max = 3, message = "评价标签数量不能超过3")
    @UniqueElements(message = "评价标签（JSON）不能重复")
    @Valid
    private List<@NotNull(message = "评价标签元素不能为空") @Pattern(regexp = "PUNCTUAL|PROFESSIONAL|FRIENDLY", message = "评价标签无效") String> tags; // 评价标签

    @NotNull(message = "评价内容不能为空")
    @Size(max = 500, message = "评价内容长度不能超过500")
    private String content; // 评价内容
}
