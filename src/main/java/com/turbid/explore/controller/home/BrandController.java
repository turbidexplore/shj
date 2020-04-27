package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Brand;
import com.turbid.explore.pojo.Shop;
import com.turbid.explore.service.BrandService;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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

    @ApiOperation(value = "添加品牌信息", notes="添加品牌信息")
    @PutMapping("/add")
    public Mono<Info> add(@RequestBody Brand brand) {
        return Mono.just(Info.SUCCESS( brandService.save(brand)));
    }

    @ApiOperation(value = "通过code获取品牌", notes="通过code获取品牌")
    @GetMapping("/get")
    public Mono<Info> get(@RequestParam("code") String code) {
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
