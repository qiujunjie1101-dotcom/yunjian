package com.bond.yunjian.service.impl;

import cn.hutool.core.util.IdUtil;
import com.bond.yunjian.common.constant.AuthConstants;
import com.bond.yunjian.common.exception.BizException;
import com.bond.yunjian.common.result.ResultCode;
import com.bond.yunjian.dto.UserLoginDTO;
import com.bond.yunjian.entity.User;
import com.bond.yunjian.service.AuthService;
import com.bond.yunjian.service.LoginSessionService;
import com.bond.yunjian.service.UserService;
import com.bond.yunjian.utils.JwtUtil;
import com.bond.yunjian.utils.LoginToken;
import com.bond.yunjian.vo.UserLoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证业务实现：编排「查凭据 → 校验密码 → 更新登录痕迹 → 签令牌 → 登记会话」。
 * 本类不直接操作用户表，也不对外返回用户资料。
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final LoginSessionService loginSessionService;

    @Override
    public UserLoginVO login(UserLoginDTO loginDTO) {
        User user = userService.findByAccount(loginDTO.getAccount());
        // 账号不存在与密码错误返回同一错误码，避免账号枚举
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BizException(ResultCode.USER_LOGIN_FAILED);
        }

        // 用户资料的维护交由用户域；先完成数据库写入，再登记会话
        userService.updateLastLoginTime(user.getId());

        // 令牌与会话同源：jti 关联二者，TTL 由同一配置项保证一致
        String jti = IdUtil.simpleUUID();
        String token = jwtUtil.createToken(user.getId(), jti);
        loginSessionService.create(user.getId(), jti);

        return UserLoginVO.builder()
                .token(token)
                .tokenType(AuthConstants.TOKEN_TYPE)
                .expiresIn(jwtUtil.getExpireSeconds())
                .build();
    }

    @Override
    public void logout(String token) {
        LoginToken loginToken = jwtUtil.parseToken(token);
        // 移除会话
        loginSessionService.revoke(loginToken.userId(), loginToken.jti());
    }
}
