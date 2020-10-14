package com.turbid.explore.service;

import com.turbid.explore.pojo.Brand;
import com.turbid.explore.pojo.Shop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BrandService {
    Brand save(Brand brand);

    List<Brand> getByShop(String code);

    Brand get(String code);

    List<Shop> getByLabel(String classgroup, String brandgroup);

    Brand getOneByShop(String code);

    List<Brand> search(String text, Integer page);

    void remove(String code);

    Long countAll();
}
