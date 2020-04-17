package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Case;
import com.turbid.explore.pojo.ProjectNeeds;
import com.turbid.explore.pojo.UserSecurity;
import com.turbid.explore.service.CaseService;
import com.turbid.explore.service.user.UserSecurityService;
import com.turbid.explore.tools.CodeLib;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Api(description = "案例相关接口")
@RestController("/case")
@CrossOrigin
public class CaseController {

    @Autowired
    private CaseService caseService;

    @Autowired
    private UserSecurityService userSecurityService;

    @ApiOperation(value = "新增案例", notes="新增案例")
    @PutMapping(value = "/addcase")
    public Mono<Info> addcase(Principal principal, @RequestBody Case obj) {
        obj.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        return Mono.just(Info.SUCCESS(caseService.save(obj)));
    }


    @ApiOperation(value = "获取案例列表", notes="获取案例列表")
    @PostMapping(value = "/caseByPage")
    public Mono<Info> caseByPage(@RequestParam(name = "page")Integer page,
                                  @RequestParam(name = "subject", required = false)String subject,
                                  @RequestParam(name = "label", required = false)String label,
                                  @RequestParam(name = "search", required = false)String search) {
        try {
            return Mono.just(Info.SUCCESS(caseService.listByPage(page,subject,label,search)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @ApiOperation(value = "获取我的案例列表", notes="获取我的案例列表")
    @PostMapping(value = "/mycases")
    public Mono<Info> mycases(Principal principal,@RequestParam(name = "page")Integer page) {
        try {
            return Mono.just(Info.SUCCESS(caseService.mycases(page,principal.getName())));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }
}
