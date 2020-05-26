package com.turbid.explore.service;

import com.turbid.explore.pojo.Call;
import com.turbid.explore.pojo.ProjectNeeds;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CallService {
    Call save(Call call);

    List<ProjectNeeds> listByUserMe(String name, Integer page);

    List<ProjectNeeds> listByUserMy(String name, Integer page);

    int mycallcount(String name);

    int callmecount(String name);
}
