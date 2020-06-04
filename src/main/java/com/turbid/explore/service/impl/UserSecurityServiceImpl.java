package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.UserSecurity;
import com.turbid.explore.repository.UserSecurityRepository;
import com.turbid.explore.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户安全信息服务实现
 */
@Service
public class UserSecurityServiceImpl implements UserSecurityService {

    @Autowired
    private UserSecurityRepository userSecurityRepository;

    public UserSecurity save(UserSecurity userSecurity){
        return userSecurityRepository.saveAndFlush(userSecurity);
    }

    public UserSecurity findByPhone(String phone){
        return userSecurityRepository.findByPhone(phone);
    }

    public int findByPhoneCount(String phone){
        return userSecurityRepository.findByPhoneCount(phone);
    }

    @Override
    public UserSecurity findByCode(String usercode) {
        return userSecurityRepository.getOne(usercode);
    }

    @Override
    public int issignin(String name, String dateStr) {
        return userSecurityRepository.issignin(name,dateStr);
    }


}
