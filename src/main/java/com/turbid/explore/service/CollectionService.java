package com.turbid.explore.service;

import com.turbid.explore.pojo.Collection;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CollectionService {
    Collection save(Collection collection);

    List<Collection> listByPage(String relation);

    List<Collection> listByPagePhone(String phone, Integer page);

    int findByCount(String name, String relation);
}
