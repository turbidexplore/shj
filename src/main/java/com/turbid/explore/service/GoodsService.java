package com.turbid.explore.service;

import com.turbid.explore.pojo.Goods;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GoodsService {
    Goods save(Goods goods);

    Goods get(String code);

    List<Goods> listByPage(String label, Integer page);

    List<Goods> newGoods(String shopcode);
}
