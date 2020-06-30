package com.turbid.explore.service;

import com.turbid.explore.pojo.Answer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnswerService {
    Answer save(Answer answer);

    List<Answer> answersByQaacode(String code, Integer page);

    int answerCount(String code);

    Answer get(String code);

    List<Answer> listByUser(String name, Integer page);

    int answersCount(String code);

    int countByUser(String name);
}
