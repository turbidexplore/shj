package com.turbid.explore.service;

import com.turbid.explore.pojo.UserSecurity;
import org.springframework.stereotype.Service;

/**
 * 用户安全信息服务接口
 */
@Service
public interface UserSecurityService {
     void save(UserSecurity userSecurity);
     UserSecurity findByPhone(String phone);
     int findByPhoneCount(String phone);
}
