package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Collection;
import com.turbid.explore.pojo.Comment;
import com.turbid.explore.service.CollectionService;
import com.turbid.explore.service.CommentService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;


@Api(description = "收藏接口")
@RestController
@RequestMapping("/collection")
@CrossOrigin
public class CollectionController {


    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private CollectionService collectionService;

    @ApiOperation(value = "收藏", notes="收藏")
    @PutMapping("/add")
    public Mono<Info> add(Principal principal, @RequestBody Collection collection) {
        collection.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        return Mono.just(Info.SUCCESS( collectionService.save(collection)));
    }

    @ApiOperation(value = "是否收藏", notes="是否收藏")
    @PutMapping("/iscollection")
    public Mono<Info> iscollection(Principal principal, @RequestParam("relation") String relation) {
      if(0<collectionService.findByCount(principal.getName(),relation)){
            return Mono.just(Info.SUCCESS(true));
        }else{
          return Mono.just(Info.SUCCESS(false));
        }

    }

    @ApiOperation(value = "查看收藏", notes="查看收藏")
    @PostMapping("/lists")
    public Mono<Info> lists(@RequestParam("relation") String relation) {
        return Mono.just(Info.SUCCESS( collectionService.listByPage(relation)));
    }

    @ApiOperation(value = "我的收藏", notes="我的收藏")
    @PostMapping("/my")
    public Mono<Info> my(Principal principal,@RequestParam("page")Integer page) {
        return Mono.just(Info.SUCCESS( collectionService.listByPagePhone(principal.getName(),page)));
    }


}