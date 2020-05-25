package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Answer;
import com.turbid.explore.repository.AnswerRepository;
import com.turbid.explore.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public Answer save(Answer answer) {
        return answerRepository.saveAndFlush(answer);
    }

    @Override
//    @Cacheable(cacheNames = {"redis_cache"}, key = "'answersByQaacode'+#code+'.'+#page")
    public List<Answer> answersByQaacode(String code, Integer page) {
        Pageable pageable = new PageRequest(page,10, Sort.Direction.DESC,"create_time");
        Page<Answer> pages=  answerRepository.answersByQaacode(pageable,code);
        return pages.getContent();
    }

    @Override
    public int answerCount(String code) {
        return answerRepository.answerCount(code);
    }

    @Override
    public Answer get(String code) {
        return answerRepository.getOne(code);
    }

    @Override
    public List<Answer> listByUser(String name, Integer page) {
        Pageable pageable = new PageRequest(page,10, Sort.Direction.DESC,"create_time");
        Page<Answer> pages=  answerRepository.listByUser(pageable,name);
        return pages.getContent();
    }

    @Override
    public int answersCount(String code) {
        return answerRepository.countByQaacode(code);
    }
}
