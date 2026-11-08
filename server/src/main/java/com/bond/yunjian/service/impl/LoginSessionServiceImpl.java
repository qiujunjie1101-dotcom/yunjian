package com.bond.yunjian.service.impl;

import com.bond.yunjian.common.constant.RedisKeyConstants;
import com.bond.yunjian.common.exception.SystemException;
import com.bond.yunjian.common.result.ResultCode;
import com.bond.yunjian.service.LoginSessionService;
import com.bond.yunjian.utils.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis 登录会话白名单实现。
 * 采用 StringRedisTemplate，键与值均为字符串，避免引入额外的序列化配置。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginSessionServiceImpl implements LoginSessionService {

    private final StringRedisTemplate stringRedisTemplate;
    private final JwtProperties jwtProperties;

    @Override
    public void create(Long userId, String jti) {
        try {
            stringRedisTemplate.opsForValue().set(
                    RedisKeyConstants.loginTokenKey(userId, jti),
                    String.valueOf(userId),
                    jwtProperties.getExpireSeconds(),
                    TimeUnit.SECONDS);
        } catch (DataAccessException e) {
            log.error("写入登录会话失败, userId={}, jti={}", userId, jti, e);
            throw new SystemException(ResultCode.CACHE_ERROR, "登录会话写入失败", e);
        }
    }

    @Override
    public boolean isActive(Long userId, String jti) {
        try {
            return Boolean.TRUE.equals(
                    stringRedisTemplate.hasKey(RedisKeyConstants.loginTokenKey(userId, jti)));
        } catch (DataAccessException e) {
            log.error("查询登录会话失败, userId={}, jti={}", userId, jti, e);
            throw new SystemException(ResultCode.CACHE_ERROR, "登录会话查询失败", e);
        }
    }

    @Override
    public void revoke(Long userId, String jti) {
        try {
            stringRedisTemplate.delete(RedisKeyConstants.loginTokenKey(userId, jti));
        } catch (DataAccessException e) {
            log.error("注销登录会话失败, userId={}, jti={}", userId, jti, e);
            throw new SystemException(ResultCode.CACHE_ERROR, "登录会话注销失败", e);
        }
    }
}
