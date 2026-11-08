package com.bond.yunjian.utils;

import com.bond.yunjian.common.constant.AuthConstants;
import com.bond.yunjian.common.exception.BizException;
import com.bond.yunjian.common.result.ResultCode;
import org.springframework.util.StringUtils;

/**
 * 从 Authorization 请求头中提取 Bearer 令牌，供登录拦截器与登出接口复用。
 */
public final class TokenExtractor {

    private TokenExtractor() {
    }

    public static String extract(String authorizationHeader) {
        if (!StringUtils.hasText(authorizationHeader)
                || !authorizationHeader.startsWith(AuthConstants.TOKEN_PREFIX)) {
            throw new BizException(ResultCode.USER_NOT_LOGIN);
        }
        String token = authorizationHeader.substring(AuthConstants.TOKEN_PREFIX.length()).trim();
        if (!StringUtils.hasText(token)) {
            throw new BizException(ResultCode.USER_TOKEN_INVALID);
        }
        return token;
    }
}
