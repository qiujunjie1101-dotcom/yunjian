package com.bond.yunjian.utils;

/**
 * 令牌解析结果。用于隔离 jjwt 的 {@code Claims}，
 * 使业务层与拦截器不依赖第三方令牌库的类型。
 *
 * @param userId 用户 ID
 * @param jti    本次登录的唯一标识
 */
public record LoginToken(Long userId, String jti) {
}
