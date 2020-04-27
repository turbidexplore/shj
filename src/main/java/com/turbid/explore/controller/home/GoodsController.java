package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Goods;
import com.turbid.explore.service.GoodsService;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Api(description = "特卖会接口")
@RestController
@RequestMapping("/goods")
@CrossOrigin
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @ApiOperation(value = "添加商铺", notes="添加商铺")
    @PutMapping("/add")
    public Mono<Info> add(@RequestBody Goods goods) {
        goods=goodsService.save(goods);
        return Mono.just(Info.SUCCESS(null));
    }

    @ApiOperation(value = "查询商铺", notes="查询商铺")
    @PutMapping("/get")
    public Mono<Info> get(@RequestParam("code")String code) {
        return Mono.just(Info.SUCCESS( goodsService.get(code)));
    }
}
