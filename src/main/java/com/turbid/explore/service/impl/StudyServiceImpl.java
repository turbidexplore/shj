package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Study;
import com.turbid.explore.repository.StudyRepository;
import com.turbid.explore.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudyServiceImpl implements StudyService {

    @Autowired
    private StudyRepository studyRepository;

    @Override
    public Study save(Study study) {
        return studyRepository.saveAndFlush(study);
    }
}
