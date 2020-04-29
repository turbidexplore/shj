package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Goods;
import com.turbid.explore.pojo.ProjectNeeds;
import com.turbid.explore.repository.GoodsRepository;
import com.turbid.explore.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Goods> listByPage(String label, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Goods> pages=  goodsRepository.listByPage(pageable,label);
        return pages.getContent();
    }

    @Override
    public List<Goods> newGoods(String shopcode) {
        Pageable pageable = new PageRequest(0,4, Sort.Direction.DESC,"create_time");
        Page<Goods> pages=  goodsRepository.newGoods(pageable,shopcode);
        return pages.getContent();
    }
}
