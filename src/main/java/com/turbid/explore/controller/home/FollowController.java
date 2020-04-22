package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Follow;
import com.turbid.explore.service.FollowService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


@Api(description = "关注接口")
@RestController
@RequestMapping("/follow")
@CrossOrigin
public class FollowController {


    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private FollowService followService;

    @ApiOperation(value = "关注", notes="关注")
    @PutMapping("/add")
    public Mono<Info> add(Principal principal, @RequestParam("phone") String phone) {
        Follow follow=new Follow();
        follow.setUser(userSecurityService.findByPhone(principal.getName()));
        follow.setUserFollow(userSecurityService.findByPhone(phone));
        return Mono.just(Info.SUCCESS( followService.save(follow)));
    }

    @ApiOperation(value = "关注我的", notes="关注我的")
    @PostMapping("/followme")
    public Mono<Info> followme(Principal principal,@RequestParam("page")Integer page) {
        Map<String,Object> jo=new HashMap<>();
        jo.put("data",followService.followme(principal.getName(),page));
        jo.put("count",followService.followmeCount(principal.getName()));
        return Mono.just(Info.SUCCESS(jo));
    }

    @ApiOperation(value = "我的关注", notes="我的关注")
    @PostMapping("/myfollow")
    public Mono<Info> myfollow(Principal principal,@RequestParam("page")Integer page) {
        Map<String,Object> jo=new HashMap<>();
        jo.put("data",followService.myfollow(principal.getName(),page));
        jo.put("count",followService.myfollowCount(principal.getName()));
        return Mono.just(Info.SUCCESS( jo));
    }


}
