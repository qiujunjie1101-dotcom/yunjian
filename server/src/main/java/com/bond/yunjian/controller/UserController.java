package com.bond.yunjian.controller;

import com.bond.yunjian.common.context.UserContext;
import com.bond.yunjian.common.result.Result;
import com.bond.yunjian.service.UserService;
import com.bond.yunjian.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户资料入口：与认证域分离，只提供用户自身信息，不处理令牌。
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 查询当前登录用户资料，用户 ID 取自登录上下文，不接受外部传参
     */
    @GetMapping("/info")
    public Result<UserInfoVO> info() {
        return Result.success(userService.getUserInfo(UserContext.requireUserId()));
    }
}
