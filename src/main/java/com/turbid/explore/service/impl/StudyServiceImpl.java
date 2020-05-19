package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.NativeContent;
import com.turbid.explore.pojo.Study;
import com.turbid.explore.repository.StudyRepository;
import com.turbid.explore.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyServiceImpl implements StudyService {

    @Autowired
    private StudyRepository studyRepository;

    @Override
    public Study save(Study study) {
        return studyRepository.saveAndFlush(study);
    }

    @Override
    public List<Study> listByPage(Integer page, String style) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Study> pages=  studyRepository.listByPage(pageable,style);
        return pages.getContent();
    }

    @Override
    public Study get(String code) {
        return studyRepository.getOne(code);
    }

    @Override
    public List<Study> hatstudyByPage(Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"seecount");
        Page<Study> pages=  studyRepository.hatstudyByPage(pageable);
        return pages.getContent();
    }
}
