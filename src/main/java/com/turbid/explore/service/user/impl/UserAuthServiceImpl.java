package com.turbid.explore.service.user.impl;

import com.turbid.explore.pojo.UserAuth;
import com.turbid.explore.repository.user.UserAuthRepository;
import com.turbid.explore.service.user.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户认证信息服务实现
 */
@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Override
    public void save(UserAuth userAuth) {
        userAuthRepository.save(userAuth);
    }
}
