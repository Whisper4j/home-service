package com.homeservice.domain.vo.catalog;

import com.homeservice.handler.json.ApiId;

import lombok.Data;

@Data
public class SkillVO {

    @ApiId
    private Long id; // 主键ID

    private String name; // 名称

    private String description; // 说明
}
