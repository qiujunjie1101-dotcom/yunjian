package com.bond.yunjian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bond.yunjian.dto.UserRegisterDTO;
import com.bond.yunjian.entity.User;
import com.bond.yunjian.vo.UserInfoVO;

/**
 * 用户域：只负责账号与资料，不负责登录状态、令牌与会话。
 */
public interface UserService extends IService<User> {

    /** 注册账号，密码在此处加密落库 */
    void register(UserRegisterDTO registerDTO);

    /**
     * 按账号查询用户实体，仅供认证域校验凭据使用。
     * 返回值含密码字段，禁止直接作为接口响应返回。
     */
    User findByAccount(String account);

    /** 查询用户资料，返回值已剔除敏感字段 */
    UserInfoVO getUserInfo(Long userId);

    /** 记录最后登录时间 */
    void updateLastLoginTime(Long userId);
}
