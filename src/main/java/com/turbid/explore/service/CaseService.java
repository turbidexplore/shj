package com.turbid.explore.service;

import com.turbid.explore.pojo.Case;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CaseService {

    Case save(Case obj);

    List<Case> listByPage(Integer page, String subject, String label, String search);

    List<Case> mycases(Integer page, String name);

    Case caseByCode(String code);

    Case update(Case obj);

    List<Case> recommend(Case obj);

    int starcount(String name);

    int remove(String code);

    int casecount(String name);
}
