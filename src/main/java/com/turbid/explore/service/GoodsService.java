package com.turbid.explore.service;

import com.turbid.explore.pojo.Goods;
import org.springframework.stereotype.Service;

@Service
public interface GoodsService {
    Goods save(Goods goods);

    Goods get(String code);
}
