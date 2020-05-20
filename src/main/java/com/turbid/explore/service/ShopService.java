package com.turbid.explore.service;

import com.turbid.explore.pojo.Shop;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface ShopService {
    Shop save(Shop shop);

    Shop getByCode(String code);

    Shop getByUser(String name);

    List<Shop> getByLabel(String label,String brandgroup);

    List<Shop> getByChoose(String label, Integer page);

    List<Shop> recommend(Principal principal, Integer page);

    List<Shop> zsjm(Principal principal, Integer page, String type);

    Shop getByUsercode(String usercode);

    List<Shop> search(String text, Integer page);
}
