package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.*;
import com.turbid.explore.repository.DiscussRepository;
import com.turbid.explore.repository.ProductReposity;
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

@Api(description = "找产品模块")
@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductReposity productReposity;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private ShopService shopService;


    @ApiOperation(value = "发布", notes="发布 type 【带shopcode 1:代理 2:出货 3:生产】4:开店 5:拿货 6:定制")
    @PutMapping("/add")
    public Mono<Info> add(Principal principal, @RequestBody Product product) {
        product.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        if(null!=product.getCompany()&&null!=product.getCompany().getCode()){
            product.setCompany(shopService.getByCode(product.getCompany().getCode()));
        }
        product= productReposity.save(product);
//      PushV3Client.pushAll(product.getCode(),  "(｡･∀･)ﾉﾞ嗨  有新的"+community.getLabel()+"需求，快去看看吧", community.getContent()+" 详情>>", "code", community.getCode(),"shehuijia://com.shehuijia.explore/communitydemand");
        return Mono.just(Info.SUCCESS(product));
    }


    @Autowired
    private VisitorService visitorService;

    @Autowired
    private DiscussRepository discussRepository;

    @ApiOperation(value = "获取发布信息", notes=" gettype 0按type类型获取 1经销商 2企业方 3设计师")
    @PostMapping("/productsByPage")
    public Mono<Info> productsByPage(Principal principal,@RequestParam("page")Integer page,@RequestParam("gettype")Integer gettype,@RequestParam(value = "type",required = false)Integer type) {
        Pageable pageable = new PageRequest(page,10, Sort.Direction.DESC,"create_time");
        Page<Product> productList=null;
        switch (gettype){
            case 1:
                productList=productReposity.productsByPagea(pageable);
                break;
            case 2:
                productList=productReposity.productsByPageb(pageable);
                break;
            case 3:
                productList=productReposity.productsByPagec(pageable);
                break;
            default:
                productList=productReposity.productsByPage(pageable,type);
                break;
        }

        productList.getContent().forEach(v->{
            if(null!=principal) {
                v.setIsstar(visitorService.countByName(principal.getName(),v.getCode()));
            }
            v.setCommentcount(discussRepository.countByCommunityCode(v.getCode()));
        });
        return Mono.just(Info.SUCCESS(productList.getContent()));
    }

    @PutMapping("/star")
    public Mono<Info> star(Principal principal, @RequestParam("code") String code) {
        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        Product product= productReposity.getOne(code);
        product.setStarcount(product.getStarcount()+1);
        productReposity.save(product);
        Visitor visitor=new Visitor();
        visitor.setUserSecurity(userSecurity);
        visitor.setShopcode(code);
        visitorService.save(visitor);
        return Mono.just(Info.SUCCESS( null));
    }

    @PostMapping("/removestar")
    public Mono<Info> removestar(Principal principal, @RequestParam("code") String code) {
        Product product= productReposity.getOne(code);
        product.setStarcount(product.getStarcount()-1);
        productReposity.save(product);
        visitorService.removestar(principal.getName(),code);
        return Mono.just(Info.SUCCESS(null));
    }

    @ApiOperation(value = "官方推荐", notes="官方推荐")
    @GetMapping("/recommend")
    public Mono<Info> recommend(@RequestParam(value = "text")String text) {
        return Mono.just(Info.SUCCESS( shopService.recommend(null,0,text)));
    }

    @ApiOperation(value = "获取详情", notes="获取详情")
    @GetMapping("/get")
    public Mono<Info> get(Principal principal,@RequestParam(value = "code")String code) {
        Product product=productReposity.getOne(code);
        if(null!=principal) {
            product.setIsstar(visitorService.countByName(principal.getName(),product.getCode()));
        }
        product.setCommentcount(discussRepository.countByCommunityCode(product.getCode()));
        return Mono.just(Info.SUCCESS( product));
    }

    @ApiOperation(value = "usercode获取详情", notes="usercode获取详情")
    @GetMapping("/getbyusercode")
    public Mono<Info> getbyusercode(Principal principal,@RequestParam(value = "usercode")String usercode,@RequestParam("page")Integer page) {
        Pageable pageable = new PageRequest(page, 10, Sort.Direction.DESC, "create_time");
        Page<Product> productList = productReposity.allByPage(pageable,usercode);
        if (null != principal) {
            productList.getContent().forEach(v -> {
                if (null != principal) {
                    v.setIsstar(visitorService.countByName(principal.getName(), v.getCode()));
                }
                v.setCommentcount(discussRepository.countByCommunityCode(v.getCode()));
            });
        }
        return Mono.just(Info.SUCCESS(productList.getContent()));
    }

}
