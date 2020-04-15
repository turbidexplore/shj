package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.District;
import com.turbid.explore.repository.DistrictRepositroy;
import com.turbid.explore.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {

    @Autowired
    private DistrictRepositroy districtRepositroy;

    @Override
    public List<District> getAreaByPid(Integer id) {
        return districtRepositroy.getAreaByPid(id);
    }
}
