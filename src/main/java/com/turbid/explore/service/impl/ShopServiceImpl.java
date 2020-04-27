package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Shop;
import com.turbid.explore.repository.ShopRepositroy;
import com.turbid.explore.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopRepositroy shopRepositroy;

    @Override
    public Shop save(Shop shop) {
        return shopRepositroy.saveAndFlush(shop);
    }

    @Override
    public Shop getByCode(String code) {
        return shopRepositroy.getOne(code);
    }

    @Override
    public Shop getByUser(String name) {
        return shopRepositroy.getByUser(name);
    }

    @Override
    public List<Shop> getByLabel(String label,String brandgroup) {
        return shopRepositroy.getByLabel(label,brandgroup);
    }

    @Override
    public List<Shop> getByChoose(String label) {
        return shopRepositroy.getByChoose(label);
    }
}
