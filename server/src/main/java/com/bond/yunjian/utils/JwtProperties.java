package com.bond.yunjian.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 登录鉴权配置，绑定 {@code yunjian.jwt.*}。
 * 令牌有效期与 Redis 登录会话有效期共用同一配置项，避免两者 TTL 不一致。
 */
@Data
@ConfigurationProperties(prefix = JwtProperties.PREFIX)
public class JwtProperties {

    public static final String PREFIX = "yunjian.jwt";

    /** HS256 签名密钥，长度不得少于 32 字节 */
    private String secret;

    /** 有效期，单位：秒 */
    private Long expireSeconds = 7200L;
}
