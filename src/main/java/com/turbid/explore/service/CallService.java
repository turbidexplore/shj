package com.turbid.explore.service;

import com.turbid.explore.pojo.Call;
import org.springframework.stereotype.Service;

@Service
public interface CallService {
    Call save(Call call);
}
