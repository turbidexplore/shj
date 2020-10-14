package com.turbid.explore.service;

import com.turbid.explore.pojo.Goods;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GoodsService {
    Goods save(Goods goods);

    Goods get(String code);

    List<Goods> listByPage(String label, Integer page);

    List<Goods> newGoods( String shopcode,String label);

    List<Goods> mylistByPage(String name, Integer page);

    int updatastatus(String code, Integer status);

    List<Goods> search(String text, Integer page);

    List<String> goodsclassByShopcode(String shopcode);

    List<Goods> newlistByPage();
}
