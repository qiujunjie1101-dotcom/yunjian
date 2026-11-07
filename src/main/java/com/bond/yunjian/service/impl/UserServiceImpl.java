package com.bond.yunjian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bond.yunjian.common.exception.BizException;
import com.bond.yunjian.common.result.ResultCode;
import com.bond.yunjian.dto.UserRegisterDTO;
import com.bond.yunjian.entity.User;
import com.bond.yunjian.mapper.UserMapper;
import com.bond.yunjian.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public void register(UserRegisterDTO registerDTO) {
        //1.业务校验：查库看这个账号是否被别人注册
        if(this.lambdaQuery().eq(User::getAccount,registerDTO.getAccount()).count()>0){
            throw new BizException(ResultCode.USER_REGISTER_EXISTS);
        }
        //2.组装实体
        User user = new User();
        user.setAccount(registerDTO.getAccount());
        //3.密码加密
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        //4.设置昵称
        user.setUsername(registerDTO.getUsername());
        //5.保存数据库
        this.save(user);
    }
}
