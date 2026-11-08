package com.bond.yunjian.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户资料响应。不返回密码、逻辑删除标记等内部字段。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO {

    private Long id;

    private String account;

    private String username;

    private String avatar;

    private String signature;

    private Integer gender;

    private LocalDateTime lastLoginTime;
}
