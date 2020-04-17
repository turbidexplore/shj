package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Case;
import com.turbid.explore.pojo.ProjectNeeds;
import com.turbid.explore.repository.CaseRepositroy;
import com.turbid.explore.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseServiceImpl implements CaseService {

    @Autowired
    private CaseRepositroy caseRepositroy;

    @Override
    public Case save(Case obj) {
        return caseRepositroy.saveAndFlush(obj);
    }

    @Override
    public List<Case> listByPage(Integer page, String subject, String label, String search) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Case> pages=  caseRepositroy.listByPage(pageable,subject,label);
        return pages.getContent();
    }

    @Override
    public List<Case> mycases(Integer page, String name) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Case> pages=  caseRepositroy.mycases(pageable,name);
        return pages.getContent();
    }
}
