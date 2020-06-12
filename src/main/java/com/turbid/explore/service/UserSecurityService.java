package com.turbid.explore.service;

import com.turbid.explore.pojo.UserSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户安全信息服务接口
 */
@Service
public interface UserSecurityService {
     UserSecurity save(UserSecurity userSecurity);
     UserSecurity findByPhone(String phone);
     int findByPhoneCount(String phone);

    UserSecurity findByCode(String usercode);

    int issignin(String name, String dateStr);

    List<UserSecurity> shopusers(String code, String text, Integer page);

    int shopuserscount(String code, String text);

    List<UserSecurity> finduserbyphone(String phone);
}
