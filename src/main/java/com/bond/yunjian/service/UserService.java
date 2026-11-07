package com.bond.yunjian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bond.yunjian.dto.UserRegisterDTO;
import com.bond.yunjian.entity.User;
import jakarta.validation.Valid;

public interface UserService extends IService<User> {
    void register(@Valid UserRegisterDTO registerDTO);
}
