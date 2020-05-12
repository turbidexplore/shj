package com.turbid.explore.service;

import com.turbid.explore.pojo.Follow;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FollowService {
    Follow save(Follow follow);

    List<Follow> myfollow(String name, Integer page);

    List<Follow> followme(String name, Integer page);

    int followmeCount(String name);

    int myfollowCount(String name);

    int findByCount(String name, String phone);

    int cancelfollow(String code);

    String find(String name, String phone);

    List<Follow> hefollow(String name, Integer page);

    List<Follow> followhe(String name, Integer page);

    int followheCount(String name);

    int hefollowCount(String name);
}
