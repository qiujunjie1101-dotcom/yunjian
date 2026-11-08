package com.bond.yunjian.common.constant;

/**
 * Redis 键定义。业务代码只能通过本类的方法拼键，不得自行拼接前缀。
 */
public final class RedisKeyConstants {

    /** 登录会话键前缀，完整键为 前缀 + userId + ":" + jti */
    public static final String LOGIN_TOKEN_PREFIX = "yunjian:login:token:";

    private static final String SEPARATOR = ":";

    private RedisKeyConstants() {
    }

    /**
     * 一次登录对应一个会话键；多端登录时同一用户可存在多个不同 jti 的键。
     */
    public static String loginTokenKey(Long userId, String jti) {
        return LOGIN_TOKEN_PREFIX + userId + SEPARATOR + jti;
    }
}
