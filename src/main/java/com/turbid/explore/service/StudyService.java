package com.turbid.explore.service;

import com.turbid.explore.pojo.Study;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudyService {
    Study save(Study study);

    List<Study> listByPage(Integer page, String style);

    Study get(String code);

    List<Study> hatstudyByPage(Integer page);

    List<Study> search(String text, Integer page);
}
