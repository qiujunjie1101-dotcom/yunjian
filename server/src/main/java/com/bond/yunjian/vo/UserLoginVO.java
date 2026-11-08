package com.bond.yunjian.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录状态响应：只承载令牌信息。
 * 用户资料由 {@code GET /user/info} 单独提供，避免认证结果与用户资料耦合。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO {

    /** 访问令牌 */
    private String token;

    /** 令牌类型，固定为 Bearer */
    private String tokenType;

    /** 有效期，单位：秒 */
    private Long expiresIn;
}
