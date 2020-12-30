package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.*;
import com.turbid.explore.push.api.client.push.PushV3Client;
import com.turbid.explore.repository.NeedsRelationRepositroy;
import com.turbid.explore.repository.NoticeRepository;
import com.turbid.explore.service.CallService;
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
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Api(description = "产品需求操作接口")
@RestController
@RequestMapping("/projectneeds")
@CrossOrigin
public class ProjectNeedsController {

    @Autowired
    private ProjectNeedsService projectNeedsService;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private NoticeRepository noticeRepository;


    @ApiOperation(value = "新需求添加", notes="新需求添加")
    @PutMapping(value = "/addneeds")
    public Mono<Info> addneeds(Principal principal, @RequestBody ProjectNeeds projectNeeds) {
        projectNeeds.setOrderno(CodeLib.randomCode(18,1));
        projectNeeds.setStatus(0);
        projectNeeds.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, projectNeeds.getTimeout());
        projectNeeds.setOvertime(sdf.format(c.getTime()));
        noticeRepository.save(new Notice(principal.getName(),"您的需求添加成功","系统通知",0,0));
        projectNeeds=projectNeedsService.save(projectNeeds);
        PushV3Client.pushByTags(projectNeeds.getCode(),projectNeeds.getTitle(),projectNeeds.getContext(),"code",projectNeeds.getCode(),"企业");
        return Mono.just(Info.SUCCESS(projectNeeds));
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
        noticeRepository.save(new Notice(principal.getName(),"您的需求已关闭","系统通知",0,0));

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
            ProjectNeeds projectNeeds=projectNeedsService.getNeedsByCode(code);
            if(null==projectNeeds.getBrowsecount()) {
                projectNeeds.setBrowsecount( 1);
            }else {
                projectNeeds.setBrowsecount(projectNeeds.getBrowsecount() + 1);
            }
            return Mono.just(Info.SUCCESS(projectNeedsService.save(projectNeeds)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @ApiOperation(value = "获取最新需求", notes="获取最新需求")
    @PostMapping(value = "/newneeds")
    public Mono<Info> newneeds() {
        try {
            return Mono.just(Info.SUCCESS(projectNeedsService.newneeds()));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }


    @ApiOperation(value = "获取我的产品需求信息", notes="获取我的产品需求信息")
    @PostMapping(value = "/getMyNeeds")
    public Mono<Info> getMyNeeds(Principal principal,@RequestParam(name = "page")Integer page,@RequestParam(name = "status")Integer status) {
        try {
            if(status==3) {
                return Mono.just(Info.SUCCESS(callService.callme(userSecurityService.findByPhone(principal.getName()).getCode(), page)));
            }else if(status==2){
                return Mono.just(Info.SUCCESS(callService.listByUserMy(userSecurityService.findByPhone(principal.getName()).getCode(), page)));
            }else {
                return Mono.just(Info.SUCCESS(projectNeedsService.getMyNeeds(principal.getName(), page, status)));
            }
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @Autowired
    private NeedsRelationRepositroy needsRelationRepositroy;

    @ApiOperation(value = "单个需求查看订单生成", notes="单个需求查看订单生成")
    @PutMapping(value = "/seeneedorder")
    public Mono<Info> seeneedorder(Principal principal,@RequestParam(name = "needsorderno")String needsorderno) {
        try {
            NeedsRelation needsRelation=new NeedsRelation();
            needsRelation.setOrderno(CodeLib.randomCode(12,1));
            needsRelation.setPhone(principal.getName());
            needsRelation.setStatus(0);
            needsRelation.setNeedsorderno(needsorderno);
            return Mono.just(Info.SUCCESS(needsRelationRepositroy.saveAndFlush(needsRelation).getOrderno()));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @ApiOperation(value = "查看是否能看该条需求", notes="查看是否能看该条需求")
    @PostMapping(value = "/isseeneeds")
    public Mono<Info> isseeneeds(Principal principal,@RequestParam(name = "needsorderno")String needsorderno) {
        try {
            boolean issee=false;
            if(needsRelationRepositroy.findneedsR(principal.getName(),needsorderno)>0){
                issee=true;
            }
            return Mono.just(Info.SUCCESS(issee));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }




    @Autowired
    private CallService callService;

    @ApiOperation(value = "联系", notes="联系")
    @PostMapping(value = "/call")
    public Mono<Info> call(Principal principal,@RequestParam(name = "usercode")String usercode,@RequestParam("needscode")String needscode) {
        try {
            Call call=new Call();
            UserSecurity userinfo= userSecurityService.findByPhone(principal.getName());
            call.setUsercode(userinfo.getCode());
            call.setUsername(userinfo.getUserBasic().getNikename());
            call.setUserhredimg(userinfo.getUserBasic().getHeadportrait());
            call.setUsertype(userinfo.getType().toString());
            UserSecurity calluserinfo= userSecurityService.findByCode(usercode);
            call.setCallusercode(calluserinfo.getCode());
            call.setCallusername(calluserinfo.getUserBasic().getNikename());
            call.setCalluserhredimg(calluserinfo.getUserBasic().getHeadportrait());
            call.setCallusertype(calluserinfo.getType().toString());

            ProjectNeeds projectNeeds=projectNeedsService.getNeedsByCode(needscode);
            if(null==projectNeeds.getChatcount()||0==projectNeeds.getChatcount()){
                projectNeeds.setChatcount(1);
            }else if(!projectNeeds.getUserBasics().contains(userinfo.getUserBasic())){
                projectNeeds.setChatcount(projectNeeds.getChatcount()+1);
            }
            if (projectNeeds.getUserBasics().size()<4&&!projectNeeds.getUserBasics().contains(userinfo.getUserBasic())){
                projectNeeds.getUserBasics().add(userinfo.getUserBasic());
            }
            call.setProjectinfo(projectNeedsService.save(projectNeeds));
            return Mono.just(Info.SUCCESS(callService.save(call)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

}
