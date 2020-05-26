package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Comment;
import com.turbid.explore.pojo.QaaInfo;
import com.turbid.explore.repository.QaaInfoRepositroy;
import com.turbid.explore.service.QaaInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QaaInfoServiceImpl implements QaaInfoService {

    @Autowired
    private QaaInfoRepositroy qaaInfoRepositroy;

    @Override
    public QaaInfo save(QaaInfo qaaInfo) {
        return qaaInfoRepositroy.saveAndFlush(qaaInfo);
    }

    @Override
//    @Cacheable(cacheNames = {"redis_cache"}, key = "'QaaInfolistByPage'+#page+#label")
    public List<QaaInfo> listByPage(Integer page, String label) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<QaaInfo> pages=  qaaInfoRepositroy.listByPage(pageable,label);
        return pages.getContent();
    }

    @Override
//    @Cacheable(cacheNames = {"redis_cache"}, key = "'qaaByCode'+#code")
    public QaaInfo qaaByCode(String code) {
        return qaaInfoRepositroy.getOne(code);
    }

    @Override
    public List<QaaInfo> listByUser(String name, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<QaaInfo> pages=  qaaInfoRepositroy.listByUser(pageable,name);
        return pages.getContent();
    }

    @Override
    public List<QaaInfo> search(String text, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<QaaInfo> pages=  qaaInfoRepositroy.search(pageable,text);
        return pages.getContent();
    }
}
