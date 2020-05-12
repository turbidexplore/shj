package com.turbid.explore.service;

import com.turbid.explore.pojo.Study;
import org.springframework.stereotype.Service;

@Service
public interface StudyService {
    Study save(Study study);
}
