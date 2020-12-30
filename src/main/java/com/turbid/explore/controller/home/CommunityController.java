package com.turbid.explore.controller.home;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.turbid.explore.configuration.AsyncTaskA;
import com.turbid.explore.pojo.*;
import com.turbid.explore.push.api.client.push.PushV3Client;
import com.turbid.explore.repository.CommunityReposity;
import com.turbid.explore.repository.DayTaskReposity;
import com.turbid.explore.repository.DiscussRepository;
import com.turbid.explore.service.ShopService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.service.VisitorService;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(description = "社区接口")
@RestController
@RequestMapping("/community")
@CrossOrigin
public class CommunityController {

    @GetMapping(value = "/style")
    public Mono<Info> style()  {
        return Mono.just(Info.SUCCESS(new String[]{"课程","设计","人才"}));
    }

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private CommunityReposity communityReposity;

    @Autowired
    private DiscussRepository discussRepository;

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private AsyncTaskA asyncTaskA;

    @Autowired
    private DayTaskReposity dayTaskReposity;

    @PutMapping("/add")
    public Mono<Info> add(Principal principal, @RequestBody Community community) {

        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        DayTask dayTask=dayTaskReposity.findByDay(principal.getName(),dateStr);
        if(null==dayTask){
            dayTask=new DayTask();
        }
        dayTask.setUserSecurity(userSecurity);
        dayTask.setTaska();
        if(dayTask.getTaska()==1){
            userSecurity.setShb(userSecurity.getShb()+10);
            userSecurityService.save(userSecurity);
        }
        dayTask=dayTaskReposity.saveAndFlush(dayTask);
        community.setUserSecurity(userSecurity);
        community.setIsstar(false);
        community.setStar(0);
        community=communityReposity.save(community);
       asyncTaskA.pushcommunity(community);
        return Mono.just(Info.SUCCESS(community ));
    }

    @PutMapping("/delete")
    public Mono<Info> delete(Principal principal, @RequestParam("code") String code) {
        communityReposity.deleteById(code);
        return Mono.just(Info.SUCCESS( null));
    }

    @PutMapping("/star")
    public Mono<Info> star(Principal principal, @RequestParam("code") String code) {
        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        DayTask dayTask=dayTaskReposity.findByDay(principal.getName(),dateStr);
        if(null==dayTask){
            dayTask=new DayTask();
        }
        dayTask.setUserSecurity(userSecurity);
        dayTask.setTaskd();
        if(dayTask.getTaskf()==3){
            userSecurity.setShb(userSecurity.getShb()+10);
            userSecurityService.save(userSecurity);
        }
        dayTask=dayTaskReposity.saveAndFlush(dayTask);
        Community community= communityReposity.getOne(code);
        community.setStar(community.getStar()+1);
        communityReposity.save(community);
        Visitor visitor=new Visitor();
        visitor.setUserSecurity(userSecurity);
        visitor.setShopcode(code);
        visitorService.save(visitor);
        return Mono.just(Info.SUCCESS( null));
    }

    @PostMapping("/removestar")
    public Mono<Info> removestar(Principal principal, @RequestParam("code") String code) {
        Community community= communityReposity.getOne(code);
        community.setStar(community.getStar()-1);
        communityReposity.save(community);
        visitorService.removestar(principal.getName(),code);
        return Mono.just(Info.SUCCESS( null));
    }


    @PostMapping("/communitybyuser")
    public Mono<Info> communitybyuser(Principal principal,@RequestParam(value = "usercode")String usercode,@RequestParam(value = "type",required = false)Integer type,@RequestParam("page")Integer page) {
        Pageable pageable=  new PageRequest(page,10, Sort.Direction.DESC,"create_time");
        List<Community> communities=new ArrayList<>();
        Page<Community> pages=  null;
        switch (type){
            case 0:
                pages= communityReposity.listByPage(pageable,null,usercode);
                break;
            case 1:
                pages= communityReposity.listByPagea(pageable,usercode);
                break;
            case 2:
                pages= communityReposity.listByPageb(pageable,usercode);
                break;
        }

        pages.getContent().forEach(v->{
            if(null!=principal) {
                v.setIsstar(visitorService.countByName(principal.getName(),v.getCode()));
            }
            if(null!=v.getUserSecurity().getShopcode()) {
                v.setShop(shopService.getByCode(v.getUserSecurity().getShopcode()));
            }
            v.setCommentcount(discussRepository.countByCommunityCode(v.getCode()));
            communities.add(v);
        });
        return Mono.just(Info.SUCCESS(communities));
    }

    @PostMapping("/communityByCode")
    public Mono<Info> communityByCode(Principal principal,@RequestParam(value = "code")String code) {
            Community community=communityReposity.getOne(code);
            if(null!=principal) {
                community.setIsstar(visitorService.countByName(principal.getName(),code));
            }
            community.setCommentcount(discussRepository.countByCommunityCode(code));
        return Mono.just(Info.SUCCESS(community));
    }

    @Autowired
    private ShopService shopService;

    @PostMapping("/listByPage")
    public Mono<Info> listByPage(Principal principal,@RequestParam(value = "label",required = false)String label,@RequestParam("page")Integer page) {
        Pageable pageable = null;
        if(null==label){
             pageable = new PageRequest(page,5, Sort.Direction.DESC,"create_time");
        }else {
            pageable=  new PageRequest(page,5, Sort.Direction.DESC,"create_time");
        }
        List<Community> communities=new ArrayList<>();
        Page<Community> pages=  communityReposity.listByPage(pageable, label,null);
        pages.getContent().forEach(v->{
            if(null!=principal) {
                v.setIsstar(visitorService.countByName(principal.getName(),v.getCode()));
            }
            if(null!=v.getUserSecurity().getShopcode()) {
                v.setShop(shopService.getByCode(v.getUserSecurity().getShopcode()));
            }
            v.setCommentcount(discussRepository.countByCommunityCode(v.getCode()));
            v.setRecommends(shopService.recommenda(v.getStyle()+","+v.getLocal(),3));
            communities.add(v);
        });
        return Mono.just(Info.SUCCESS(communities));
    }
    @PutMapping("/discuss")
    public Mono<Info> discuss(Principal principal, @RequestBody Discuss discuss) {
        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        DayTask dayTask=dayTaskReposity.findByDay(principal.getName(),dateStr);
        if(null==dayTask){
            dayTask=new DayTask();
        }
        dayTask.setUserSecurity(userSecurity);
        dayTask.setTaske();
        if(dayTask.getTaske()==1){
            userSecurity.setShb(userSecurity.getShb()+5);
            userSecurityService.save(userSecurity);
        }
        dayTask=dayTaskReposity.saveAndFlush(dayTask);
        discuss.setDiscussUserSecurity(userSecurity);
        asyncTaskA.pushdiscuss(discuss);
        return Mono.just(Info.SUCCESS(discussRepository.save(discuss)));
    }

    @PostMapping("/hotdiscuss")
    public Mono<Info> hotdiscuss(Principal principal,@RequestParam(value = "code")String code) {
        Pageable pageable = new PageRequest(0,2, Sort.Direction.DESC,"star");
        Page<Discuss> pages=  discussRepository.hotlistByPage(pageable,code, 5);
        JSONArray discusses=new JSONArray();
        pages.getContent().forEach(v->{
            JSONObject discuss=new JSONObject();
            if(null!=principal) {
                v.setIsstar(visitorService.countByName(principal.getName(),v.getCode()));
            }
            discuss.put("discuss",v);
            Page<Discuss>  a=  discussRepository.listByPage(new PageRequest(0,3, Sort.Direction.DESC,"create_time"),v.getCode());
            discuss.put("discusses",a.getContent());
            discusses.add(discuss);
        });
        return Mono.just(Info.SUCCESS(discusses));
    }

    @PostMapping("/discussListByPage")
    public Mono<Info> discussListByPage(Principal principal,@RequestParam(value = "code")String code,@RequestParam("page")Integer page) {
        Pageable pageable = new PageRequest(page,10, Sort.Direction.DESC,"create_time");
        Page<Discuss> pages=  discussRepository.listByPage(pageable,code);
        JSONArray discusses=new JSONArray();
        pages.getContent().forEach(v->{
            JSONObject discuss=new JSONObject();
            if(null!=principal) {
                v.setIsstar(visitorService.countByName(principal.getName(),v.getCode()));
            }
            discuss.put("discuss",v);
            Page<Discuss>  a=  discussRepository.listByPage(new PageRequest(0,3, Sort.Direction.DESC,"create_time"),v.getCode());
            discuss.put("discusses",a.getContent());
            discusses.add(discuss);
        });
        JSONObject data=new JSONObject();
        data.put("data",discusses);
        data.put("size",discussRepository.countByCommunityCode(code));
        return Mono.just(Info.SUCCESS(data));
    }

    @PutMapping("/discussstar")
    public Mono<Info> discussstar(Principal principal, @RequestParam("code") String code) {
        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        Discuss discuss= discussRepository.getOne(code);
        discuss.setStar(discuss.getStar()+1);
        discussRepository.save(discuss);
        Visitor visitor=new Visitor();
        visitor.setUserSecurity(userSecurity);
        visitor.setShopcode(code);
        visitorService.save(visitor);
        return Mono.just(Info.SUCCESS( null));
    }

    @PostMapping("/removediscuss")
    public Mono<Info> removediscuss(Principal principal, @RequestParam("code") String code) {
        discussRepository.deleteById(code);
        return Mono.just(Info.SUCCESS( null));
    }

    @PostMapping("/discussremovestar")
    public Mono<Info> discussremovestar(Principal principal, @RequestParam("code") String code) {
        Discuss discuss= discussRepository.getOne(code);
        discuss.setStar(discuss.getStar()-1);
        discussRepository.save(discuss);
        visitorService.removestar(principal.getName(),code);
        return Mono.just(Info.SUCCESS(null));
    }


}
