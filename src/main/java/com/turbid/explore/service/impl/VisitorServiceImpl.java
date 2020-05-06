package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Visitor;
import com.turbid.explore.repository.VisitorRepository;
import com.turbid.explore.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitorServiceImpl implements VisitorService {

    @Autowired
    private VisitorRepository visitorRepository;

    @Override
    public Visitor save(Visitor visitor) {
        return visitorRepository.saveAndFlush(visitor);
    }

    @Override
    public int count(String dateStr, String code) {
        return  visitorRepository.countNumber(dateStr,code);
    }
}
