package com.homeservice.domain.vo.account;

import com.homeservice.enums.AccountStatus;
import com.homeservice.handler.json.ApiId;
import com.homeservice.handler.json.ApiIds;

import java.util.List;

import lombok.Data;

@Data
public class WorkerVO {

    @ApiId
    private Long id; // 主键ID

    @ApiId
    private Long accountId; // 账号ID

    private String username; // 用户名

    private AccountStatus status; // 状态

    private String displayName; // 显示名称

    private String phone; // 手机号

    private String cityCode; // 城市编码

    @ApiIds
    private List<Long> skillIds; // 技能ID列表

    private Boolean dispatchEnabled; // 是否参与派单

    private String cityName; // 城市名称
}
