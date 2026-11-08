package com.bond.yunjian.controller;

import com.bond.yunjian.common.constant.AuthConstants;
import com.bond.yunjian.common.result.Result;
import com.bond.yunjian.dto.UserLoginDTO;
import com.bond.yunjian.dto.UserRegisterDTO;
import com.bond.yunjian.service.AuthService;
import com.bond.yunjian.service.UserService;
import com.bond.yunjian.utils.TokenExtractor;
import com.bond.yunjian.vo.UserLoginVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证入口：处理登录状态。
 * 注册接口保留在 /auth/register 以兼容既有调用，其业务实现归属用户域 UserService。
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    /**
     * 用户注册（兼容路径，业务归属用户域）
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success();
    }

    /**
     * 用户登录，成功时只返回令牌信息
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        return Result.success(authService.login(loginDTO));
    }

    /**
     * 用户登出，使当前令牌对应的会话立即失效
     */
    @PostMapping("/logout")
    public Result<Void> logout(
            @RequestHeader(value = AuthConstants.AUTHORIZATION_HEADER, required = false)
            String authorizationHeader) {
        authService.logout(TokenExtractor.extract(authorizationHeader));
        return Result.success();
    }
}
