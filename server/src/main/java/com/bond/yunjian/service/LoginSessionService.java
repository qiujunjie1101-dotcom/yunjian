package com.bond.yunjian.service;

/**
 * 登录会话管理（Redis 白名单）。
 * 认证域通过它控制令牌的生效与失效，用户域不感知会话存在。
 */
public interface LoginSessionService {

    /** 登记一次登录产生的会话，TTL 与令牌有效期一致 */
    void create(Long userId, String jti);

    /** 判断该次登录的会话是否仍然有效（未登出、未过期、未被踢） */
    boolean isActive(Long userId, String jti);

    /** 注销指定会话，使对应令牌立即失效 */
    void revoke(Long userId, String jti);
}
