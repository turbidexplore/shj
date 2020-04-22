package com.turbid.explore.service;

import com.turbid.explore.pojo.UserAuth;
import org.springframework.stereotype.Service;

/**
 * 用户认证信息服务层接口
 */
@Service
public interface UserAuthService {
    UserAuth save(UserAuth userAuth);
}
