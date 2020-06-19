package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Shop;
import com.turbid.explore.repository.ShopRepositroy;
import com.turbid.explore.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.security.Principal;
import java.util.Collections;
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
//    @Cacheable(cacheNames = {"redis_cache"}, key = "'ShopgetByLabel'+#label+#brandgroup")
    public List<Shop> getByLabel(String label,String brandgroup) {
        return shopRepositroy.getByLabel(label,brandgroup);
    }

    @Override
//    @Cacheable(cacheNames = {"redis_cache"}, key = "'getByChoose'+#label+#page")
    public List<Shop> getByChoose(String label, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"ischoose");
        Page<Shop> pages=   shopRepositroy.getByChoose(pageable,label);
        return pages.getContent();
    }


    @PersistenceContext
    private EntityManager entityManager;


//    public List<Shop> recommend() {
//
//        Page<Shop> pages=   shopRepositroy.recommend(pageable,sb.toString());
//        return pages.getContent();
//    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Shop> recommend(Principal principal, Integer page, String likes) {

        StringBuilder dataSql = new StringBuilder("SELECT * FROM SHOP WHERE 1 = 1");
        for (String v : likes.split(",")) {
            dataSql.append(" OR label LIKE '%"+v+"%' ");
        }
        //组装sql语句
        dataSql.append(" order by ischoose desc limit 0,4");

        //创建本地sql查询实例
        Query dataQuery = entityManager.createNativeQuery(dataSql.toString(), Shop.class);
        return dataQuery.getResultList();
    }


    @Override
    public List<Shop> zsjm(Principal principal, Integer page, String type) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"ischoose");
        Page<Shop> pages=   shopRepositroy.zsjm(pageable,type);
        return pages.getContent();
    }

    @Override
    public Shop getByUsercode(String usercode) {
        return shopRepositroy.getByUsercode(usercode);
    }

    @Override
    public List<Shop> search(String text, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"ischoose");
        Page<Shop> pages=  shopRepositroy.search(pageable,text);
        return pages.getContent();
    }

    @Override
    public List<Shop> findByText(String text, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"ischoose");
        Page<Shop> pages=  shopRepositroy.search(pageable,text);
        return pages.getContent();
    }

    @Override
    public int findByTextCount(String text) {
        return shopRepositroy.searchcount(text);
    }

    @Override
    public Long countAll() {
        return shopRepositroy.count();
    }
}
