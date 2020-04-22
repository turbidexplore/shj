package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.UserAuth;
import com.turbid.explore.repository.UserAuthRepository;
import com.turbid.explore.service.UserAuthService;
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
    public UserAuth save(UserAuth userAuth) {
       return  userAuthRepository.saveAndFlush(userAuth);
    }
}
