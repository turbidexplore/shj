package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Comment;
import com.turbid.explore.pojo.ProjectNeeds;
import com.turbid.explore.repository.CommentRepositroy;
import com.turbid.explore.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepositroy commentRepositroy;

    @Override
    public Comment save(Comment comment) {
        return commentRepositroy.saveAndFlush(comment);
    }

    @Override
    public List<Comment> listByPage(String relation, Integer page) {
        Pageable pageable = new PageRequest(page,10, Sort.Direction.DESC,"create_time");
        Page<Comment> pages=  commentRepositroy.listByPage(pageable,relation);
        return pages.getContent();
    }

    @Override
    public int listByCount(String relation) {
        return commentRepositroy.listByCount(relation);
    }
}
