package com.homeservice.domain.query.catalog;

import com.homeservice.common.constant.MessageConstant;

import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SkillQuery extends com.homeservice.common.domain.PageQuery {
    @Size(min = 1, max = 100, message = MessageConstant.SEARCH_KEYWORD_TOO_LONG)
    private String keyword; // 搜索关键词

}
