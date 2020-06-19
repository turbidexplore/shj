package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Visitor;
import com.turbid.explore.pojo.bo.BrandCountInfo;
import com.turbid.explore.repository.VisitorRepository;
import com.turbid.explore.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public int brandCount(String time, String code) {
        return  visitorRepository.brandCount(time,code);
    }

    @Override
    public int goodsCount(String time, String code) {
        return  visitorRepository.goodsCount(time,code);
    }

    @Override
    public int companyCount(String code) {
        return visitorRepository.countByShopcode(code);
    }

    @Override
    public List<BrandCountInfo> brandinfo(String name) {
        return  visitorRepository.brandinfo(name);
    }

    @Override
    public int countByTime(String time) {
        return  visitorRepository.countByTime(time);
    }


}
