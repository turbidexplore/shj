package com.turbid.explore.service;

import com.turbid.explore.pojo.Shop;
import org.springframework.stereotype.Service;

@Service
public interface ShopService {
    Shop save(Shop shop);

    Shop getByCode(String code);

    Shop getByUser(String name);
}
