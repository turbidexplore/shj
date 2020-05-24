package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Collection;
import com.turbid.explore.pojo.Follow;
import com.turbid.explore.pojo.bo.AreaCount;
import com.turbid.explore.repository.FollowRepositroy;
import com.turbid.explore.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.security.Principal;
import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowRepositroy followRepositroy;

    @Override
    public Follow save(Follow follow) {
        return followRepositroy.saveAndFlush(follow);
    }

    @Override
//    @Cacheable(cacheNames = {"redis_cache"}, key = "'myfollow'+#name+#page")
    public List<Follow> myfollow(String name, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Follow> pages=  followRepositroy.myfollow(pageable,name);
        return pages.getContent();
    }

    @Override
    @Cacheable(cacheNames = {"redis_cache"}, key = "'followme'+#name+#page")
    public List<Follow> followme(String name, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Follow> pages=  followRepositroy.followme(pageable,name);
        return pages.getContent();
    }

    @Override
    public int followmeCount(String name) {
        return followRepositroy.followmeCount(name);
    }

    @Override
    public int myfollowCount(String name) {
        return followRepositroy.myfollowCount(name);
    }

    @Override
    public int findByCount(String name, String phone) {
        return followRepositroy.findByCount(name,phone);
    }

    @Override
    @Transactional
    public int cancelfollow( String code) {
        return followRepositroy.cancelfollow(code);
    }

    @Override
    public String find(String name, String phone) {
        return followRepositroy.find(name,phone);
    }

    @Override
    public List<Follow> hefollow(String name, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Follow> pages=  followRepositroy.hefollow(pageable,name);
        return pages.getContent();
    }

    @Override
    public List<Follow> followhe(String name, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Follow> pages=  followRepositroy.followhe(pageable,name);
        return pages.getContent();
    }

    @Override
    public int followheCount(String name) {
            return followRepositroy.followheCount(name);
    }

    @Override
    public int hefollowCount(String name) {
                return followRepositroy.hefollowCount(name);
    }

    @Override
    public int newfollowmeCount(String name, String time) {
        return followRepositroy.newfollowmeCount(name,time);
    }

    @Override
    public List<AreaCount> areaCount(Principal principal) {
        return followRepositroy.areaCount(principal.getName());
    }
}
