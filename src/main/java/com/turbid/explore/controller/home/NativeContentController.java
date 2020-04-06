package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.NativeContent;
import com.turbid.explore.pojo.Needs;
import com.turbid.explore.service.NativeContentService;
import com.turbid.explore.service.user.UserSecurityService;
import com.turbid.explore.utils.CodeLib;
import com.turbid.explore.utils.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

@Api(description = "内容接口")
@RestController
@RequestMapping("/nativecontent")
@CrossOrigin
public class NativeContentController {

    @Autowired
    private NativeContentService nativeContentService;

    @Autowired
    private UserSecurityService userSecurityService;

    @ApiOperation(value = "发布文章", notes="发布文章")
    @PostMapping(value = "/add")
    public Mono<Info> add(Principal principal,@RequestBody NativeContent nativeContent) {
        nativeContent.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        List<String> imgs= CodeLib.listImgSrc(nativeContent.getContent());
        if(imgs.size()>0) {
            nativeContent.setFirstimage(imgs.get(0));
        }else {
            nativeContent.setFirstimage("");
        }
        nativeContentService.save(nativeContent);
        return Mono.just(Info.SUCCESS(null));
    }

    @ApiOperation(value = "最新发布", notes="最新发布")
    @PostMapping(value = "/news")
    public Mono<Info> news(@RequestParam(name = "page")Integer page) {
        try {
            return Mono.just(Info.SUCCESS(nativeContentService.listByPage(page)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @ApiOperation(value = "我的最新发布", notes="我的最新发布")
    @GetMapping(value = "/mynews")
    public Mono<Info> mynews(Principal principal, @RequestParam(name = "page")Integer page) {
        try {
            return Mono.just(Info.SUCCESS(nativeContentService.listByPage(page,principal.getName())));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }
}
