package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.NeedsRelation;
import com.turbid.explore.pojo.ProjectNeeds;
import com.turbid.explore.repository.NeedsRelationRepositroy;
import com.turbid.explore.service.ProjectNeedsService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.tools.CodeLib;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @ApiOperation(value = "修改需求", notes="修改需求")
    @PutMapping(value = "/updateneeds")
    public Mono<Info> updateneeds(Principal principal, @RequestBody ProjectNeeds projectNeeds) {
        if(!principal.getName().equals(projectNeeds.getUserSecurity().getPhonenumber())){
            return Mono.just(Info.ERROR("您没有修改该需求的权限！"));
        }
        return Mono.just(Info.SUCCESS(projectNeedsService.save(projectNeeds)));
    }

    @ApiOperation(value = "关闭需求", notes="关闭需求")
    @DeleteMapping(value = "/colseneeds")
    public Mono<Info> colseneeds(Principal principal, @RequestParam("code")String code) {
        ProjectNeeds projectNeeds=  projectNeedsService.getNeedsByCode(code);
        if(!principal.getName().equals(projectNeeds.getUserSecurity().getPhonenumber())){
            return Mono.just(Info.ERROR("您没有修改该需求的权限！"));
        }
        projectNeeds.setStatus(1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(new Date());
        projectNeeds.setOvertime(dateStr);
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
    public Mono<Info> getMyNeeds(Principal principal,@RequestParam(name = "page")Integer page,@RequestParam(name = "status")Integer status) {
        try {
            return Mono.just(Info.SUCCESS(projectNeedsService.getMyNeeds(principal.getName(),page,status)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @Autowired
    private NeedsRelationRepositroy needsRelationRepositroy;

    @ApiOperation(value = "单个需求查看订单生成", notes="单个需求查看订单生成")
    @PutMapping(value = "/seeneedorder")
    public Mono<Info> seeneedorder(Principal principal,@RequestParam(name = "code")String code,@RequestParam(name="paytype")String paytype) {
        try {
            NeedsRelation needsRelation=new NeedsRelation();
            needsRelation.setOrderno(CodeLib.randomCode(12,1));
            needsRelation.setPhone(principal.getName());
            needsRelation.setStatus(0);
            needsRelation.setNeedscode(code);
            needsRelationRepositroy.saveAndFlush(needsRelation);
            Object obj=null;
            if(paytype=="wechatpay"){

            }else if(paytype=="alipay"){

            }
            return Mono.just(Info.SUCCESS(obj));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @ApiOperation(value = "查看是否能看该条需求", notes="查看是否能看该条需求")
    @PostMapping(value = "/isseeneeds")
    public Mono<Info> isseeneeds(Principal principal,@RequestParam(name = "code")String code) {
        try {
            boolean issee=false;
            if(needsRelationRepositroy.findneedsR(principal.getName(),code)>0){
                issee=true;
            }
            return Mono.just(Info.SUCCESS(issee));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @ApiOperation(value = "需求加急", notes="需求加急")
    @PostMapping(value = "/urgent")
    public Mono<Info> urgent(Principal principal,@RequestParam(name = "orderno")String orderno) {
        try {



            return Mono.just(Info.SUCCESS(null));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }


}
