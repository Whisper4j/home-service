package com.homeservice.domain.dto.review;

import com.homeservice.common.constant.MessageConstant;

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

    @NotNull(message = MessageConstant.SCORE_REQUIRED)
    @Min(value = 1, message = MessageConstant.SCORE_INVALID)
    @Max(value = 5, message = MessageConstant.SCORE_INVALID)
    private Integer score; // 评分

    @NotNull(message = MessageConstant.REVIEW_TAGS_REQUIRED)
    @Size(max = 3, message = MessageConstant.REVIEW_TAGS_TOO_MANY)
    @UniqueElements(message = MessageConstant.REVIEW_TAGS_DUPLICATED)
    @Valid
    private List<@NotNull(message = MessageConstant.REVIEW_TAG_INVALID) @Pattern(regexp = "PUNCTUAL|PROFESSIONAL|FRIENDLY", message = MessageConstant.REVIEW_TAG_INVALID) String> tags; // 评价标签

    @NotNull(message = MessageConstant.REVIEW_CONTENT_REQUIRED)
    @Size(max = 500, message = MessageConstant.REVIEW_CONTENT_TOO_LONG)
    private String content; // 评价内容
}
