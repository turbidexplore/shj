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




}
