package com.bond.yunjian.utils;

import com.bond.yunjian.common.exception.BizException;
import com.bond.yunjian.common.result.ResultCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * JWT 签发与解析工具，只处理令牌本身，不感知 Redis 会话与用户业务。
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

    /** HS256 要求密钥长度不少于 256 bit */
    private static final int MIN_SECRET_BYTES = 32;

    private final JwtProperties jwtProperties;

    private SecretKey secretKey;

    /**
     * 启动时校验并缓存密钥，配置不合规直接快速失败，避免运行期才暴露问题。
     */
    @PostConstruct
    void init() {
        String secret = jwtProperties.getSecret();
        if (!StringUtils.hasText(secret)
                || secret.getBytes(StandardCharsets.UTF_8).length < MIN_SECRET_BYTES) {
            throw new IllegalStateException("yunjian.jwt.secret 未配置或长度不足 32 字节");
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 签发令牌。
     *
     * @param userId 用户 ID，写入 sub
     * @param jti    本次登录的唯一标识，写入 jti，用于关联 Redis 会话
     */
    public String createToken(Long userId, String jti) {
        long now = System.currentTimeMillis();
        long expireMillis = TimeUnit.SECONDS.toMillis(jwtProperties.getExpireSeconds());
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setId(jti)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expireMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 验签并解析令牌，返回业务侧的值对象。
     *
     * @throws BizException 过期返回 USER_NOT_LOGIN，非法返回 USER_TOKEN_INVALID
     */
    public LoginToken parseToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new BizException(ResultCode.USER_NOT_LOGIN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new BizException(ResultCode.USER_TOKEN_INVALID);
        }
        if (!StringUtils.hasText(claims.getSubject()) || !StringUtils.hasText(claims.getId())) {
            throw new BizException(ResultCode.USER_TOKEN_INVALID);
        }
        try {
            return new LoginToken(Long.valueOf(claims.getSubject()), claims.getId());
        } catch (NumberFormatException e) {
            throw new BizException(ResultCode.USER_TOKEN_INVALID);
        }
    }

    public long getExpireSeconds() {
        return jwtProperties.getExpireSeconds();
    }
}
