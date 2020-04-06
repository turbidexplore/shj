package com.turbid.explore.service.user.impl;

import com.turbid.explore.pojo.UserBasic;
import com.turbid.explore.repository.UserBasicRepository;
import com.turbid.explore.service.user.UserBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户基本信息服务实现
 */
@Service
public class UserBasicServiceImpl implements UserBasicService {

    @Autowired
    private UserBasicRepository userBasicRepository;

    @Override
    public void save(UserBasic userBasic) {
        userBasicRepository.saveAndFlush(userBasic);
    }
}
