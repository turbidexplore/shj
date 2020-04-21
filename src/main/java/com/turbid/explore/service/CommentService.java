package com.turbid.explore.service;

import com.turbid.explore.pojo.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    Comment save(Comment comment);

    List<Comment> listByPage(String relation, Integer page);
}
