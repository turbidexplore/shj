package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.NativeContent;
import com.turbid.explore.pojo.Study;
import com.turbid.explore.pojo.StudyGroup;
import com.turbid.explore.repository.StudyRelationRepository;
import com.turbid.explore.repository.StudyRepository;
import com.turbid.explore.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Study> listByPage(Integer page, String style,String code) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Study> pages=  studyRepository.listByPage(pageable,style,code);
        return pages.getContent();
    }

    @Override
    public Study get(String code) {
        return studyRepository.getOne(code);
    }

    @Override
    public List<StudyGroup> hatstudyByPage(Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"seecount");
        Page<StudyGroup> pages=  studyRepository.hatstudyByPage(pageable);
        return pages.getContent();
    }

    @Override
    public List<Study> search(String text, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"seecount");
        Page<Study> pages=  studyRepository.search(pageable,text);
        return pages.getContent();
    }

    @Autowired
    private StudyRelationRepository studyRelationRepository;

    @Override
    @Transactional
    public void updateSTUDY(String out_trade_no) {
        studyRelationRepository.updateSTUDY(out_trade_no);
    }

    @Override
    public Study getByOrder(String orderno) {
        return studyRepository.getByOrder(orderno);
    }

    @Override
    public List<Study> list(Integer page, String style,String code) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Study> pages=  studyRepository.list(pageable,style,code);
        return pages.getContent();
    }

    @Override
    @Transactional
    public int del(String code) {
        return studyRepository.del(code);
    }
}
