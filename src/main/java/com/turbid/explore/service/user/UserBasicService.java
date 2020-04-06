package com.turbid.explore.service.user;

import com.turbid.explore.pojo.UserBasic;
import com.turbid.explore.pojo.UserSecurity;
import org.springframework.stereotype.Service;

/**
 * 用户基本信息服务层接口
 */
@Service
public interface UserBasicService {

    void save(UserBasic userBasic);
}
