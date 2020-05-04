package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Shop;
import com.turbid.explore.pojo.Visitor;
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

@Api(description = "店铺接口")
@RestController
@RequestMapping("/shop")
@CrossOrigin
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private UserSecurityService userSecurityService;

    @ApiOperation(value = "获取商铺信息", notes="获取商铺信息")
    @GetMapping("/get")
    public Mono<Info> get(Principal principal) {
        return Mono.just(Info.SUCCESS( shopService.getByUser(principal.getName())));
    }

    @ApiOperation(value = "通过商铺code获取商铺信息", notes="通过商铺code获取商铺信息")
    @GetMapping("/getbycode")
    public Mono<Info> getbycode(Principal principal,@RequestParam("code")String code) {
        Shop shop=  shopService.getByCode(code);
        Visitor visitor=new Visitor();
        visitor.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        visitorService.save(visitor);
        shop.getVisitor().add(visitor);
        return Mono.just(Info.SUCCESS( shop));
    }


    @ApiOperation(value = "更新商铺信息", notes="更新商铺信息")
    @PutMapping("/update")
    public Mono<Info> update(@RequestBody Shop shop) {
        return Mono.just(Info.SUCCESS( shopService.save(shop)));
    }

    @ApiOperation(value = "通过商铺label获取商铺信息", notes="通过商铺label获取商铺信息")
    @GetMapping("/getbylabel")
    public Mono<Info> getbylabel(@RequestParam("classgroup")String classgroup,@RequestParam("brandgroup")String brandgroup) {
        List list=new ArrayList();
        shopService.getByLabel(classgroup,brandgroup).forEach(v->{
            Map map=new HashMap();
            map.put("code",v.getCode());
            map.put("name",v.getCompanyname());
            map.put("logo",v.getLogo());
            list.add(map);
        });
        return Mono.just(Info.SUCCESS( list));
    }

    @ApiOperation(value = "官方严选", notes="官方严选")
    @GetMapping("/choose")
    public Mono<Info> choose(@RequestParam(value = "label",required = false)String label,@RequestParam(value = "page")Integer page) {
        return Mono.just(Info.SUCCESS( shopService.getByChoose(label,page)));
    }

}
