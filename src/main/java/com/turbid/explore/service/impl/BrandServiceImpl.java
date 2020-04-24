package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Brand;
import com.turbid.explore.pojo.Shop;
import com.turbid.explore.repository.BrandRepositroy;
import com.turbid.explore.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepositroy brandRepositroy;

    @Override
    public Brand save(Brand brand) {
        return brandRepositroy.saveAndFlush(brand);
    }

    @Override
    public List<Brand> getByShop(String code) {
        return brandRepositroy.getByShop(code);
    }

    @Override
    public Brand get(String code) {
        return brandRepositroy.getOne(code);
    }
}
