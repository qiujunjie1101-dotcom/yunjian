package com.bond.yunjian.service;

import com.bond.yunjian.dto.UserLoginDTO;
import com.bond.yunjian.vo.UserLoginVO;

/**
 * 认证域：只负责登录状态（凭据校验、令牌签发、会话注销）。
 * 用户账号与资料的读写一律由 {@link UserService} 承担。
 */
public interface AuthService {

    /**
     * 登录，返回令牌信息，不返回用户资料。
     */
    UserLoginVO login(UserLoginDTO loginDTO);

    /**
     * 注销当前令牌对应的登录会话。
     *
     * @param token 已去掉 Bearer 前缀的令牌
     */
    void logout(String token);
}
