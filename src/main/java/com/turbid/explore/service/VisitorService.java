package com.turbid.explore.service;

import com.turbid.explore.pojo.Visitor;
import org.springframework.stereotype.Service;

@Service
public interface VisitorService {

     Visitor save(Visitor visitor);

    int count(String dateStr, String code);

    int brandCount(String time, String code);

    int goodsCount(String time, String code);
}
