package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Answer;
import com.turbid.explore.pojo.Brand;
import com.turbid.explore.pojo.Shop;
import com.turbid.explore.repository.BrandRepositroy;
import com.turbid.explore.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public String getOneByShop(String code) {
        Pageable pageable = new PageRequest(0,1, Sort.Direction.DESC,"create_time");
        Page<String> pages=  brandRepositroy.getOneByShop(pageable,code);
        return pages.getContent().get(0);
    }
}
