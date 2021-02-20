package com.turbid.explore.controller.home;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.turbid.explore.pojo.*;
import com.turbid.explore.push.api.client.push.PushV3Client;
import com.turbid.explore.repository.DayTaskReposity;
import com.turbid.explore.repository.DiscussRepository;
import com.turbid.explore.repository.ProductReposity;
import com.turbid.explore.repository.VisitorRepository;
import com.turbid.explore.service.CommentService;
import com.turbid.explore.service.ShopService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.service.VisitorService;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;


@Api(description = "评论接口")
@RestController
@RequestMapping("/comment")
@CrossOrigin
public class CommentController {

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ProductReposity productReposity;

    @Autowired
    private DayTaskReposity dayTaskReposity;

    @ApiOperation(value = "评论", notes="评论")
    @PutMapping("/addcomment")
    public Mono<Info> addcomment(Principal principal, @RequestBody Comment comment) {
        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        comment.setUserSecurity(userSecurity);
//        try {
//        Product product=  productReposity.getOne(comment.getRelation());
//        if(null!=product){
//            PushV3Client.pushByAlias(UUID.randomUUID().toString().replace("-",""),  "(｡･∀･)ﾉﾞ嗨  有人回复了您的需求，快去看看吧", product.getWord()+" 详情>>", "code", product.getCode(),"shehuijia://com.shehuijia.explore/product",product.getUserSecurity().getPhonenumber());
//        }
//        }catch (Exception e){
//        }
        String i="评论成功!";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        DayTask dayTask=dayTaskReposity.findByDay(principal.getName(),dateStr);
        if(dayTask.getTaske()<=10){
            userSecurity.setShb(userSecurity.getShb()+10);
            userSecurityService.save(userSecurity);
            i=i+"您已成功获得10积分。";
        }
        return Mono.just(Info.SUCCESS( i,commentService.save(comment)));
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

    @ApiOperation(value = "查看相关评论", notes="查看相关评论")
    @PostMapping("/mycomments")
    public Mono<Info> mycomments(Principal principal,@RequestParam("relation") String relation) {
        return Mono.just(Info.SUCCESS(commentService.mycomments(principal.getName(),relation)));
    }

    @Autowired
    private ShopService shopService;

    @ApiOperation(value = "查看店铺相关评论", notes="查看店铺相关评论")
    @PostMapping("/shopcomments")
    public Mono<Info> shopcomments(Principal principal,@RequestParam("page")Integer page) {
        Shop shop=shopService.getByUser(principal.getName());
        Map<String,Object> jo=new HashMap<>();
        jo.put("data",commentService.listByShopPage(shop.getCode(),page));
        jo.put("count",commentService.listByShopCount(shop.getCode()));
        return Mono.just(Info.SUCCESS(jo));
    }

    @ApiOperation(value = "查看店铺相关评论数量", notes="查看店铺相关评论数量")
    @PostMapping("/shopcommentscount")
    public Mono<Info> shopcommentscount(Principal principal) {
        Shop shop=shopService.getByUser(principal.getName());

        return Mono.just(Info.SUCCESS(commentService.listByShopCount(shop.getCode())));
    }

    @Autowired
    private DiscussRepository discussRepository;

    @Autowired
    private VisitorService visitorService;

    @PutMapping("/discuss")
    public Mono<Info> discuss(Principal principal, @RequestBody Discuss discuss) {
        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        discuss.setDiscussUserSecurity(userSecurity);
        String i="评论成功!";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        DayTask dayTask=dayTaskReposity.findByDay(principal.getName(),dateStr);
        if(dayTask.getTaske()<=10){
            userSecurity.setShb(userSecurity.getShb()+10);
            userSecurityService.save(userSecurity);
            i=i+"您已成功获得10积分。";
        }
//        PushV3Client.pushByAlias(UUID.randomUUID().toString().replace("-",""),  "(｡･∀･)ﾉﾞ嗨  有人回复了您的评论，快去看看吧","", "code", "","",discuss.getReplyUserSecurity().getPhonenumber());
        return Mono.just(Info.SUCCESS(i,discussRepository.save(discuss)));
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
        return Mono.just(Info.SUCCESS( null));
    }


}
