package com.bond.yunjian.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("im_user") //对应你新建的表名
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String account;
    private String username;

    // 因为开启了驼峰映射，其实这句注解可以删，但保留更严谨
    private String password;

    private String avatar;
    private String signature;
    private Integer gender;

    private LocalDateTime lastLoginTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;

    private LocalDateTime deletedAt;
}
