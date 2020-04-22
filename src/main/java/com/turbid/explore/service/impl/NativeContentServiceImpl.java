package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.NativeContent;
import com.turbid.explore.repository.NativeContentRepositroy;
import com.turbid.explore.service.NativeContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NativeContentServiceImpl implements NativeContentService {

    @Autowired
    private NativeContentRepositroy nativeContentRepositroy;

    private Integer size=15;

    @Override
    public NativeContent save(NativeContent nativeContent) {
       return nativeContentRepositroy.save(nativeContent);
    }

    @Override
    public List<NativeContent> listByPage(Integer page) {
        Pageable pageable = new PageRequest(page,size, Sort.Direction.DESC,"create_time");
        Page<NativeContent> pages=  nativeContentRepositroy.listByPage(pageable);
        return pages.getContent();
    }

    @Override
    public List<NativeContent> listByPage(Integer page, String username) {
        Pageable pageable = new PageRequest(page,size, Sort.Direction.DESC,"create_time");
        Page<NativeContent> pages=  nativeContentRepositroy.listByPage(pageable,username);
        return pages.getContent();
    }

    @Override
    public NativeContent newsByCode(String code) {
        return nativeContentRepositroy.getOne(code);
    }
}
