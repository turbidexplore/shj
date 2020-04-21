package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Comment;
import com.turbid.explore.pojo.QaaInfo;
import com.turbid.explore.service.CommentService;
import com.turbid.explore.service.QaaInfoService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;


@Api(description = "问答接口")
@Controller
@RequestMapping("/qaa")
@CrossOrigin
public class QaaInfoController {


    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private QaaInfoService qaaInfoService;

    @ApiOperation(value = "发布问题", notes="发布问题")
    @PutMapping("/addqaa")
    public Mono<Info> addqaa(Principal principal, @RequestBody QaaInfo qaaInfo) {
        qaaInfo.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        return Mono.just(Info.SUCCESS( qaaInfoService.save(qaaInfo)));
    }

    @ApiOperation(value = "查看问答列表", notes="查看问答列表")
    @GetMapping("/qaas")
    public Mono<Info> qaas(@RequestParam("page")Integer page,@RequestParam(name = "label", required = false)String label) {

        return Mono.just(Info.SUCCESS( qaaInfoService.listByPage(page,label)));
    }

    @ApiOperation(value = "查看问答", notes="查看问答")
    @GetMapping("/qaaByCode")
    public Mono<Info> qaaByCode(@RequestParam("code")String code) {

        return Mono.just(Info.SUCCESS( qaaInfoService.qaaByCode(code)));
    }


}
