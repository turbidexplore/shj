package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Case;
import com.turbid.explore.pojo.ProjectNeeds;
import com.turbid.explore.repository.CaseRepositroy;
import com.turbid.explore.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
    //@Cacheable(cacheNames = {"redis_cache"}, key = "'caseListByPage'+#page+#subject+#label+#search")
    public List<Case> listByPage(Integer page, String subject, String label, String search) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Case> pages=  caseRepositroy.listByPage(pageable,subject,label);
        return pages.getContent();
    }

    @Override
    //@Cacheable(cacheNames = {"redis_cache"}, key = "'mycases'+#page+#name")
    public List<Case> mycases(Integer page, String name) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Case> pages=  caseRepositroy.mycases(pageable,name);
        return pages.getContent();
    }

    @Override
    //@Cacheable(cacheNames = {"redis_cache"}, key = "'caseByCode'+#code")
    public Case caseByCode(String code) {
        return caseRepositroy.caseByCode(code);
    }

    @Override
    public Case update(Case obj) {
        return caseRepositroy.save(obj);
    }

    @Override
    //@Cacheable(cacheNames = {"redis_cache"}, key = "'recommend'+#obj.getLabel()+#obj.getSubject()")
    public List<Case> recommend(Case obj) {
        return caseRepositroy.recommend(obj.getLabel(),obj.getSubject());
    }

    @Override
    public int starcount(String name) {
      Integer count=  caseRepositroy.starcount(name);
        if(null==count){
        return 0;}else {
            return count;
        }
    }

    @Override
    public int remove(String code) {
         caseRepositroy.delete(caseRepositroy.caseByCode(code));
         return 1;
    }

    @Override
    public int casecount(String name) {
        return caseRepositroy.casecount(name);
    }

    @Override
    public List<Case> casesByUsercode(String usercode, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Case> pages=  caseRepositroy.casesByUsercode(pageable,usercode);
        return pages.getContent();
    }

    @Override
    public int commentcount(String usercode) {
         return caseRepositroy.commentcount(usercode);
    }

    @Override
    public List<Case> search(String text, Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<Case> pages=  caseRepositroy.search(pageable,text);
        return pages.getContent();
    }
}
