package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Study;
import com.turbid.explore.pojo.UserSecurity;
import com.turbid.explore.repository.UserSecurityRepository;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.tools.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @Override
    public List<UserSecurity> shopusers(String code, String text, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<UserSecurity> pages=  userSecurityRepository.listByPage(pageable,text,code);
        return pages.getContent();
    }

    @Override
    public int shopuserscount(String code, String text) {
        return userSecurityRepository.shopuserscount(text,code);
    }

    @Override
    public List<UserSecurity> finduserbyphone(String phone) {
        Pageable pageable = new PageRequest(0,15, Sort.Direction.DESC,"create_time");
        Page<UserSecurity> pages=  userSecurityRepository.findByUserSecurityPhone(pageable,phone);
        return pages.getContent();
    }

    @Override
    public Long countAll() {
        return userSecurityRepository.count();
    }

    @Override
    public Long countByTime(String time) {
        return userSecurityRepository.countByTime(time);
    }

    @Override
    public List<UserSecurity> alluserbypage(Integer page, String text) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<UserSecurity> pages=  userSecurityRepository.alluserbypage(pageable,text);
        return pages.getContent();
    }

    @Override
    public int allusercount(String text) {
        return userSecurityRepository.allusercount(text);
    }

}
