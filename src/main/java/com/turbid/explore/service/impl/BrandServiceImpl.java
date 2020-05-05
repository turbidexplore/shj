package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Brand;
import com.turbid.explore.pojo.Shop;
import com.turbid.explore.repository.BrandRepositroy;
import com.turbid.explore.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
//    @Cacheable(cacheNames = {"redis_cache"}, key = "'getByShop'+#code")
    public List<Brand> getByShop(String code) {
        return brandRepositroy.getByShop(code);
    }

    @Override
    public Brand get(String code) {
        return brandRepositroy.getOne(code);
    }

    @Override
//    @Cacheable(cacheNames = {"redis_cache"}, key = "'getByLabel'+#classgroup+'.'+#brandgroup")
    public List<Brand> getByLabel(String classgroup, String brandgroup) {
        return brandRepositroy.getByLabel(classgroup,brandgroup);
    }
}
