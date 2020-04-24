package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Collection;
import com.turbid.explore.pojo.Follow;
import com.turbid.explore.repository.FollowRepositroy;
import com.turbid.explore.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    public List<Follow> myfollow(String name, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Follow> pages=  followRepositroy.myfollow(pageable,name);
        return pages.getContent();
    }

    @Override
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
    public Object cancelfollow(String name, String phone) {
        return followRepositroy.cancelfollow(name,phone);
    }
}
