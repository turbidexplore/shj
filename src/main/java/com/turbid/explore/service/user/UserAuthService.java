package com.turbid.explore.service.user;

import com.turbid.explore.pojo.UserAuth;
import org.springframework.stereotype.Service;

/**
 * 用户认证信息服务层接口
 */
@Service
public interface UserAuthService {
    void save(UserAuth userAuth);
}
