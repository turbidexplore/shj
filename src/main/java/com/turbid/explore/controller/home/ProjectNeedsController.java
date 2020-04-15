package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.ProjectNeeds;
import com.turbid.explore.service.ProjectNeedsService;
import com.turbid.explore.service.user.UserSecurityService;
import com.turbid.explore.tools.CodeLib;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Api(description = "产品需求操作接口")
@RestController
@RequestMapping("/projectneeds")
@CrossOrigin
public class ProjectNeedsController {

    @Autowired
    private ProjectNeedsService projectNeedsService;

    @Autowired
    private UserSecurityService userSecurityService;

    @ApiOperation(value = "新需求添加", notes="新需求添加")
    @PutMapping(value = "/addneeds")
    public Mono<Info> addneeds(Principal principal, @RequestBody ProjectNeeds projectNeeds) {
        projectNeeds.setOrderno(CodeLib.randomCode(18,1));
        projectNeeds.setStatus(0);
        projectNeeds.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        return Mono.just(Info.SUCCESS(projectNeedsService.save(projectNeeds)));
    }




    @ApiOperation(value = "获取需求分页列表", notes="获取需求分页列表")
    @PostMapping(value = "/needsByPage")
    public Mono<Info> needsByPage(@RequestParam(name = "page")Integer page,
                                  @RequestParam(name = "style", required = false)String style,
                                  @RequestParam(name = "category", required = false)String category,
                                  @RequestParam(name = "search", required = false)String search,

                                  @RequestParam(name = "type")String type) {
        try {
            return Mono.just(Info.SUCCESS(projectNeedsService.listByPage(page,style,category,type,search)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }


    @ApiOperation(value = "获取产品需求信息", notes="获取产品需求信息")
    @PostMapping(value = "/getneeds")
    public Mono<Info> getNeeds(@RequestParam(name = "code")String code) {
        try {
            return Mono.just(Info.SUCCESS(projectNeedsService.getNeedsByCode(code)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @ApiOperation(value = "获取我的产品需求信息", notes="获取我的产品需求信息")
    @PostMapping(value = "/getMyNeeds")
    public Mono<Info> getMyNeeds(Principal principal,@RequestParam(name = "page")Integer page,@RequestParam(name = "status")Integer status,@RequestParam(name = "type")String type) {
        try {
            return Mono.just(Info.SUCCESS(projectNeedsService.getMyNeeds(principal.getName(),page,status,type)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }


}
