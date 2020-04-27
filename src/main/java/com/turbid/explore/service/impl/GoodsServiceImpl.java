package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Goods;
import com.turbid.explore.repository.GoodsRepository;
import com.turbid.explore.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    public Goods save(Goods goods) {
        return goodsRepository.save(goods);
    }

    @Override
    public Goods get(String code) {
        return goodsRepository.getOne(code);
    }
}
