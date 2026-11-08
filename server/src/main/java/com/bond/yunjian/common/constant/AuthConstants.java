package com.bond.yunjian.common.constant;

/**
 * 认证相关常量，集中定义以避免请求头、前缀等字面量散落在各层。
 */
public final class AuthConstants {

    /** 承载令牌的请求头 */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /** 令牌前缀，含一个空格 */
    public static final String TOKEN_PREFIX = "Bearer ";

    /** 响应给客户端的令牌类型 */
    public static final String TOKEN_TYPE = "Bearer";

    private AuthConstants() {
    }
}
