package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Needs;
import com.turbid.explore.repository.NeedsRepositroy;
import com.turbid.explore.service.NeedsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NeedsServiceImpl implements NeedsService {

    @Autowired
    private NeedsRepositroy needsRepositroy;

    private Integer size=15;

    @Override
    public void save(Needs needs) {
        needsRepositroy.save(needs);
    }

    @Override
    public List<Needs> listByPage(Integer page) {
        Pageable pageable = new PageRequest(page,size, Sort.Direction.DESC,"create_time");
        Page<Needs> pages=  needsRepositroy.listByPage(pageable);
        return pages.getContent();
    }
}
