package com.bond.yunjian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bond.yunjian.common.exception.BizException;
import com.bond.yunjian.common.result.ResultCode;
import com.bond.yunjian.dto.UserRegisterDTO;
import com.bond.yunjian.entity.User;
import com.bond.yunjian.mapper.UserMapper;
import com.bond.yunjian.service.UserService;
import com.bond.yunjian.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户业务实现。登录状态由 {@code AuthService} 负责，本类不签发令牌、不访问 Redis。
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void register(UserRegisterDTO registerDTO) {
        if (this.lambdaQuery().eq(User::getAccount, registerDTO.getAccount()).count() > 0) {
            throw new BizException(ResultCode.USER_REGISTER_EXISTS);
        }
        User user = new User();
        user.setAccount(registerDTO.getAccount());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setUsername(registerDTO.getUsername());
        this.save(user);
    }

    @Override
    public User findByAccount(String account) {
        // @TableLogic 会自动追加 deleted = 0 条件
        return this.lambdaQuery().eq(User::getAccount, account).one();
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND);
        }
        return UserInfoVO.builder()
                .id(user.getId())
                .account(user.getAccount())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .signature(user.getSignature())
                .gender(user.getGender())
                .lastLoginTime(user.getLastLoginTime())
                .build();
    }

    @Override
    public void updateLastLoginTime(Long userId) {
        // 只更新非空字段，updateTime 由 MyMetaObjectHandler 自动填充
        User user = new User();
        user.setId(userId);
        user.setLastLoginTime(LocalDateTime.now());
        this.updateById(user);
    }
}
