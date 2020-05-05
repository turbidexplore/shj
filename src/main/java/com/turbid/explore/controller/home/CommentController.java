package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Comment;
import com.turbid.explore.service.CommentService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(description = "评论接口")
@RestController
@RequestMapping("/comment")
@CrossOrigin
public class CommentController {

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "评论", notes="评论")
    @PutMapping("/addcomment")
    public Mono<Info> addcomment(Principal principal, @RequestBody Comment comment) {
        comment.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        return Mono.just(Info.SUCCESS( commentService.save(comment)));
    }

    @ApiOperation(value = "查看相关评论", notes="查看相关评论")
    @PostMapping("/comments")
    public Mono<Info> comments(@RequestParam("relation") String relation,@RequestParam("page")Integer page) {
        Map<String,Object> jo=new HashMap<>();
        List<Comment> commentList= commentService.listByPage(relation,page);
        commentList.forEach(v->{
            v.setCount(commentService.listByCount(v.getCode()));
        });
        jo.put("data",commentList);
        jo.put("count",commentService.listByCount(relation));
        return Mono.just(Info.SUCCESS(jo));
    }


}
