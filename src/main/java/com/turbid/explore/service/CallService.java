package com.turbid.explore.service;

import com.turbid.explore.pojo.Call;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CallService {
    Call save(Call call);

    List<Call> listByUserMe(String name, Integer page);

    List<Call> listByUserMy(String name, Integer page);
}
