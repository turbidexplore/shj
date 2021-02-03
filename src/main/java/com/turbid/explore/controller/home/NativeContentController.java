package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.NativeContent;
import com.turbid.explore.repository.NativeContentRepositroy;
import com.turbid.explore.repository.ShopRepositroy;
import com.turbid.explore.service.NativeContentService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.tools.CodeLib;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Api(description = "灵感相关接口")
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
        nativeContent.setCreate_time(new Date());
        if(null!=principal) {
            nativeContent.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
            if(null!=nativeContent.getUserSecurity().getShopcode()&&""!=nativeContent.getUserSecurity().getShopcode()&&!"".equals(nativeContent.getUserSecurity().getShopcode())&&!nativeContent.getUserSecurity().getShopcode().equals(null)){
                nativeContent.setCompany(shopRepositroy.getOne(nativeContent.getUserSecurity().getShopcode()));
            }
        }
        List<String> imgs= CodeLib.listImgSrc(nativeContent.getContent());
        if(imgs.size()>2) {
            nativeContent.setFirstimage(imgs.get(0)+","+imgs.get(1)+","+imgs.get(2));
        }else if(imgs.size()>1){
            nativeContent.setFirstimage(imgs.get(0)+","+imgs.get(1));
        }else if(imgs.size()>0){
            nativeContent.setFirstimage(imgs.get(0));
        }else {
            nativeContent.setFirstimage("");
        }
        nativeContent.setContent(nativeContent.getContent().replace("<img ","<img style='width:100%' "));
        nativeContentService.save(nativeContent);
        return Mono.just(Info.SUCCESS(null));
    }

    @ApiOperation(value = "删除", notes="删除")
    @PutMapping(value = "/del")
    public Mono<Info> del(@RequestParam(name = "code")String code) {
        try {
            nativeContentService.del(code);
            return Mono.just(Info.SUCCESS(null));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @ApiOperation(value = "最新发布", notes="最新发布")
    @PostMapping(value = "/news")
    public Mono<Info> news(@RequestParam(name = "page")Integer page,@RequestParam(name = "label",required = false)String label,@RequestParam(name = "fromv",required = false)String fromv) {
        try {
            return Mono.just(Info.SUCCESS(nativeContentService.listByPageLabel(page,label,fromv)));
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

    @ApiOperation(value = "查看软文", notes="查看软文")
    @GetMapping(value = "/newsByCode")
    public Mono<Info> newsByCode(Principal principal, @RequestParam(name = "code")String code) {
        try {
            NativeContent nativeContent=  nativeContentService.newsByCode(code);
            try {
                if( nativeContent.getSees().toString().contains(principal.getName())==false) {
                    nativeContent.getSees().add(userSecurityService.findByPhone(principal.getName()));
                }
            }catch (Exception e){

            }


            return Mono.just(Info.SUCCESS(nativeContentService.save(nativeContent)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @ApiOperation(value = "点赞", notes="点赞")
    @PostMapping(value = "/star")
    public Mono<Info> star(Principal principal,@RequestParam(name = "code")String code) {
        try {
            NativeContent nativeContent=  nativeContentService.newsByCode(code);
            nativeContent.getStars().add(userSecurityService.findByPhone(principal.getName()));
            try {
                nativeContentService.save(nativeContent);
            }catch (Exception e){

            }
            return Mono.just(Info.SUCCESS(""));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @ApiOperation(value = "所有图库筛选", notes="所有图库筛选")
    @PostMapping(value = "/allbypage")
    public Mono<Info> allbypage( @RequestParam(name = "page")Integer page,
                                 @RequestParam(name = "freesubject",required = false)String freesubject,
                                 @RequestParam(name = "subject",required = false)String subject,
                                 @RequestParam(name = "abroad",required = false)Integer abroad,
                                 @RequestParam(name = "isshop",required = false)Integer isshop,
                                 @RequestParam(name = "label",required = false)String label) {
        try {
            return Mono.just(Info.SUCCESS(nativeContentService.allbypage(page,freesubject,subject,abroad,isshop,label)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }


    @PostMapping(value = "/infobyshopcode")
    public Mono<Info> infobyshopcode(@RequestParam(name = "shopcode")String shopcode) {
        try {
            return Mono.just(Info.SUCCESS(nativeContentService.infobyshopcode(shopcode)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @Autowired
    private ShopRepositroy shopRepositroy;

    @ApiOperation(value = "获取推荐商铺信息", notes="获取推荐商铺信息")
    @PostMapping(value = "/getshops")
    public Mono<Info> getshops(@RequestParam("nativecontentCode")String nativecontentCode) {
        try {
            Pageable pageable = new PageRequest(0, 4, Sort.Direction.DESC, "create_time");
            return Mono.just(Info.SUCCESS(shopRepositroy.getshops(pageable, null,null,null).getContent()));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @Autowired
    private NativeContentRepositroy nativeContentRepositroy;

    @ApiOperation(value = "通过品牌code获取产品图库", notes="通过品牌code获取产品图库")
    @PostMapping(value = "/getbybrand")
    public Mono<Info> getbybrand(@RequestParam("brandcode")String brandcode,@RequestParam("page")Integer page,@RequestParam("size")Integer size) {
        try {
            Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, "create_time");
            return Mono.just(Info.SUCCESS(nativeContentRepositroy.getbybrand(pageable,brandcode).getContent()));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

}
