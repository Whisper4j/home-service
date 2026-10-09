package com.homeservice.domain.query.account;

import com.homeservice.common.constant.MessageConstant;

import com.homeservice.handler.json.ApiId;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WorkerQuery extends com.homeservice.common.domain.PageQuery {
    @Size(min = 1, max = 100, message = MessageConstant.SEARCH_KEYWORD_TOO_LONG)
    private String keyword; // 搜索关键词
    @ApiId
    @Positive(message = MessageConstant.SKILL_INVALID)
    private Long skillId; // 技能ID
    private Boolean dispatchEnabled; // 是否参与派单

}
