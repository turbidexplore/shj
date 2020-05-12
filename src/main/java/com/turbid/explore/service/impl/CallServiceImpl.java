package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Call;
import com.turbid.explore.repository.CallRepository;
import com.turbid.explore.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallServiceImpl implements CallService {

    @Autowired
    private CallRepository callRepository;

    @Override
    public Call save(Call call) {
        return callRepository.saveAndFlush(call);
    }
}
