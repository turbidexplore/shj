package com.turbid.explore.service;

import com.turbid.explore.pojo.District;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DistrictService {
    List<District> getAreaByPid(Integer id);
}
