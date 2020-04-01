package com.turbid.explore.service.user.impl;

import com.turbid.explore.pojo.UserSecurity;
import com.turbid.explore.repository.user.UserSecurityRepository;
import com.turbid.explore.service.user.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户安全信息服务实现
 */
@Service
public class UserSecurityServiceImpl implements UserSecurityService {

    @Autowired
    private UserSecurityRepository userSecurityRepository;

    public void save(UserSecurity userSecurity){
        userSecurityRepository.save(userSecurity);
    }

    public UserSecurity findByPhone(String phone){
        return userSecurityRepository.findByPhone(phone);
    }

    public int findByPhoneCount(String phone){
        return userSecurityRepository.findByPhoneCount(phone);
    }
}
