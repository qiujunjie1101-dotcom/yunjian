package com.bond.yunjian.config;

import com.bond.yunjian.common.constant.AuthConstants;
import com.bond.yunjian.common.context.UserContext;
import com.bond.yunjian.common.exception.BizException;
import com.bond.yunjian.common.result.ResultCode;
import com.bond.yunjian.service.LoginSessionService;
import com.bond.yunjian.utils.JwtUtil;
import com.bond.yunjian.utils.LoginToken;
import com.bond.yunjian.utils.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录态校验拦截器：提取令牌 → 验签解析 → 校验 Redis 会话白名单 → 写入用户上下文。
 */
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final LoginSessionService loginSessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 静态资源等非控制器请求直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String authorizationHeader = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);
        LoginToken loginToken = jwtUtil.parseToken(TokenExtractor.extract(authorizationHeader));
        if (!loginSessionService.isActive(loginToken.userId(), loginToken.jti())) {
            // 已登出或被踢下线：令牌即使尚未过期也不再放行
            throw new BizException(ResultCode.USER_NOT_LOGIN);
        }
        UserContext.setUserId(loginToken.userId());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, @Nullable Exception ex) {
        // 必须清理 ThreadLocal，避免线程复用导致身份串号
        UserContext.clear();
    }
}
