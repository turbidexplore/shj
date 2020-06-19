package com.turbid.explore.service;

import com.turbid.explore.pojo.Visitor;
import com.turbid.explore.pojo.bo.BrandCountInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VisitorService {

     Visitor save(Visitor visitor);

    int count(String dateStr, String code);

    int brandCount(String time, String code);

    int goodsCount(String time, String code);

    int companyCount(String code);

    List<BrandCountInfo> brandinfo(String name);

    int countByTime(String time);
}
