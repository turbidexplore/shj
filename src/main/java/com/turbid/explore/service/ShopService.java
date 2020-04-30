package com.turbid.explore.service;

import com.turbid.explore.pojo.Shop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShopService {
    Shop save(Shop shop);

    Shop getByCode(String code);

    Shop getByUser(String name);

    List<Shop> getByLabel(String label,String brandgroup);

    List<Shop> getByChoose(String label, Integer page);
}
