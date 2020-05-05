package com.turbid.explore.service;

import com.turbid.explore.pojo.Collection;
import com.turbid.explore.pojo.bo.CollectionType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CollectionService {
    Collection save(Collection collection);

    List<Collection> listByPage(String relation);

    List<Collection> listByPagePhone(String phone, Integer page, CollectionType collectionType);

    int findByCount(String name, String relation);

    Object cancelcollection(String name, String relation);
}
