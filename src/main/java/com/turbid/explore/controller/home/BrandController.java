package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Brand;
import com.turbid.explore.pojo.Shop;
import com.turbid.explore.pojo.Visitor;
import com.turbid.explore.service.BrandService;
import com.turbid.explore.service.ShopService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.service.VisitorService;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "品牌接口")
@RestController
@RequestMapping("/brand")
@CrossOrigin
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private ShopService shopService;

    @ApiOperation(value = "添加品牌信息", notes="添加品牌信息")
    @PutMapping("/add")
    public Mono<Info> add(Principal principal, @RequestBody Brand brand) {
        brand.setCompany(shopService.getByUser(principal.getName()));
        return Mono.just(Info.SUCCESS( brandService.save(brand)));
    }

    @ApiOperation(value = "通过code获取品牌", notes="通过code获取品牌")
    @GetMapping("/mybrands")
    public Mono<Info> mybrands(Principal principal) {
        return Mono.just(Info.SUCCESS(brandService.getByShop(shopService.getByUser(principal.getName()).getCode())));
    }

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private VisitorService visitorService;

    @ApiOperation(value = "通过code获取品牌", notes="通过code获取品牌")
    @GetMapping("/get")
    public Mono<Info> get(Principal principal,@RequestParam("code") String code) {

        Shop shop=  shopService.getByCode(code);
        Visitor visitor=new Visitor();
        visitor.setShopcode(shop.getCode());
        visitor.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        visitorService.save(visitor);
        return Mono.just(Info.SUCCESS( brandService.get(code)));
    }

    @ApiOperation(value = "通过商铺code获取品牌", notes="通过商铺code获取品牌")
    @GetMapping("/getByShop")
    public Mono<Info> getByShop(@RequestParam("code") String code) {

        return Mono.just(Info.SUCCESS( brandService.getByShop(code)));
    }


    @ApiOperation(value = "通过label获取品牌信息", notes="通过label获取品牌信息")
    @GetMapping("/getbylabel")
    public Mono<Info> getbylabel(@RequestParam("classgroup")String classgroup,@RequestParam("brandgroup")String brandgroup) {
        List list=new ArrayList();
        brandService.getByLabel(classgroup,brandgroup).forEach(v->{
            Map map=new HashMap();
            map.put("code",v.getCode());
            map.put("name",v.getName());
            map.put("logo",v.getLogo());
            list.add(map);
        });
        return Mono.just(Info.SUCCESS(list ));
    }




}
