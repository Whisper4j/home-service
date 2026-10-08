package com.homeservice.domain.po.account;
import com.baomidou.mybatisplus.annotation.*;
import com.homeservice.enums.*;
import com.homeservice.domain.value.*;
import com.homeservice.handler.mybatis.*;
import lombok.Data;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
/** auth_account 的持久化映射；不直接作为接口输出。 */
@Data
@TableName(value = "auth_account", autoResultMap = true)
public class AuthAccount {
    /** 认证账号ID，对外按十进制字符串传输 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 登录用户名，区分大小写 */
    @TableField(value = "username")
    private String username;

    /** BCrypt哈希，不保存明文密码 */
    @TableField(value = "password_hash")
    @ToString.Exclude
    private String passwordHash;

    /** 角色：CUSTOMER/WORKER/ADMIN */
    @TableField(value = "role")
    private Role role;

    /** 账号状态：ENABLED/DISABLED */
    @TableField(value = "status")
    private AccountStatus status;

    /** 展示称呼 */
    @TableField(value = "display_name")
    private String displayName;

    /** 联系电话 */
    @TableField(value = "phone")
    private String phone;

    /** 是否为禁止禁用的初始化管理员 */
    @TableField(value = "protected_account")
    private Boolean protectedAccount;

    /** 创建时间（Asia/Shanghai） */
    @TableField(value = "created_at", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

    /** 更新时间（Asia/Shanghai） */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
