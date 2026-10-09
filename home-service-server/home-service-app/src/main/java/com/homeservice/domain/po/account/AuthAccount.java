package com.homeservice.domain.po.account;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.homeservice.enums.AccountStatus;
import com.homeservice.enums.Role;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.ToString;

@Data
@TableName("auth_account")
public class AuthAccount {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private String username; // 用户名
    @ToString.Exclude
    private String passwordHash; // 密码哈希（日志隐藏）
    private Role role; // 账号角色
    private AccountStatus status; // 状态
    private String displayName; // 显示名称
    private String phone; // 手机号
    private Boolean protectedAccount; // 是否保护账号
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt; // 更新时间

}
