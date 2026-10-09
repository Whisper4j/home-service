package com.homeservice.domain.po.account;

import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.domain.value.*;
import com.homeservice.enums.*;
import com.homeservice.handler.mybatis.*;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 认证账号持久化类
 * 映射auth_account表数据
 */
@Data
@TableName(value = "auth_account", autoResultMap = true)
public class AuthAccount {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "username")
    private String username;
    @TableField(value = "password_hash")
    @ToString.Exclude
    private String passwordHash;
    @TableField(value = "role")
    private Role role;
    @TableField(value = "status")
    private AccountStatus status;
    @TableField(value = "display_name")
    private String displayName;
    @TableField(value = "phone")
    private String phone;
    @TableField(value = "protected_account")
    private Boolean protectedAccount;
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
