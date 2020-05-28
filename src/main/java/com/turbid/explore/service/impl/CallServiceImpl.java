package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Call;
import com.turbid.explore.pojo.Case;
import com.turbid.explore.pojo.ProjectNeeds;
import com.turbid.explore.repository.CallRepository;
import com.turbid.explore.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CallServiceImpl implements CallService {

    @Autowired
    private CallRepository callRepository;

    @Override
    public Call save(Call call) {
        return callRepository.saveAndFlush(call);
    }

    @Override
    public List<ProjectNeeds> listByUserMe(String name, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<ProjectNeeds> pages=  callRepository.listByUserMe(pageable,name);
        return pages.getContent();
    }

    @Override
    public List<ProjectNeeds> listByUserMy(String name, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<ProjectNeeds> pages=  callRepository.listByUserMy(pageable,name);
        return pages.getContent();
    }

    @Override
    public int mycallcount(String name) {
        return callRepository.mycallcount(name).size();
    }

    @Override
    public int callmecount(String name) {
        return callRepository.callmecount(name).size();
    }

    @Override
    public List<Call> callme(String name, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Call> pages=  callRepository.callme(pageable,name);
        return pages.getContent();
    }


}
