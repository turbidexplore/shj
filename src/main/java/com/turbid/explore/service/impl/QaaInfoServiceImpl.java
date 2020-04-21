package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Comment;
import com.turbid.explore.pojo.QaaInfo;
import com.turbid.explore.repository.QaaInfoRepositroy;
import com.turbid.explore.service.QaaInfoService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<QaaInfo> listByPage(Integer page, String label) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<QaaInfo> pages=  qaaInfoRepositroy.listByPage(pageable,label);
        return pages.getContent();
    }

    @Override
    public QaaInfo qaaByCode(String code) {
        return qaaInfoRepositroy.qaaByCode(code);
    }
}
