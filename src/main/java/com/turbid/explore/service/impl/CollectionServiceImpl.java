package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Collection;
import com.turbid.explore.pojo.bo.CollectionType;
import com.turbid.explore.repository.CollectionRepositroy;
import com.turbid.explore.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private CollectionRepositroy collectionRepositroy;

    @Override
    public Collection save(Collection collection) {
        return collectionRepositroy.saveAndFlush(collection);
    }

    @Override
//    @Cacheable(cacheNames = {"redis_cache"}, key = "'CollectionlistByPage'+#relation")
    public List<Collection> listByPage(String relation) {
        return collectionRepositroy.listByPage(relation);
    }

    @Override
//    @Cacheable(cacheNames = {"redis_cache"}, key = "'CollectionlistByPagePhone'+#phone+#page+#collectionType")
    public List<Collection> listByPagePhone(String phone, Integer page, CollectionType collectionType) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Collection> pages=  collectionRepositroy.listByPagePhone(pageable,phone,collectionType);
        return pages.getContent();
    }

    @Override
    public int findByCount(String name, String relation) {
        return collectionRepositroy.findByCount(name,relation);
    }

    @Override
    @Transactional
    public Integer cancelcollection(String name, String relation) {
        return collectionRepositroy.cancelcollection(collectionRepositroy.find(name,relation).getCode());
    }

    @Override
    public int goodslikes(String time, String code) {
        return collectionRepositroy.goodslikes(time,code);
    }

    @Override
    public int count(String code) {
        return collectionRepositroy.countByRelation(code);
    }
}
