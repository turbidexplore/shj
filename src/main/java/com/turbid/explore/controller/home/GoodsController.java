package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Goods;
import com.turbid.explore.service.GoodsService;
import com.turbid.explore.service.ShopService;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Api(description = "特卖会接口")
@RestController
@RequestMapping("/goods")
@CrossOrigin
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ShopService shopService;

    @ApiOperation(value = "添加特卖品", notes="添加特卖品")
    @PutMapping("/add")
    public Mono<Info> add(Principal principal,@RequestBody Goods goods) {
        goods.setStatus(0);
        goods.setBrowses(0);
        goods.setCompany(shopService.getByUser(principal.getName()));
        goods=goodsService.save(goods);
        return Mono.just(Info.SUCCESS(goods));
    }

    @ApiOperation(value = "上架下架", notes="上架下架")
    @PutMapping("/updatastatus")
    public Mono<Info> updatastatus(Principal principal,@RequestParam("code")String code,@RequestParam("status")Integer status) {
        goodsService.updatastatus(code,status);
        return Mono.just(Info.SUCCESS(null));
    }

    @ApiOperation(value = "查询特卖品", notes="查询特卖品")
    @GetMapping("/get")
    public Mono<Info> get(@RequestParam("code")String code) {
        Goods goods=  goodsService.get(code);
        goods.setBrowses(goods.getBrowses()+1);
        return Mono.just(Info.SUCCESS(goodsService.save(goods)));
    }

    @ApiOperation(value = "特卖品列表", notes="特卖品列表")
    @PostMapping("/listByPage")
    public Mono<Info> listByPage(@RequestParam(value = "label",required = false)String label,@RequestParam("page")Integer page) {
        return Mono.just(Info.SUCCESS( goodsService.listByPage(label,page)));
    }

    @ApiOperation(value = "新品推荐", notes="新品推荐")
    @GetMapping("/newGoods")
    public Mono<Info> newGoods(@RequestParam(value = "shopcode")String shopcode) {
        return Mono.just(Info.SUCCESS( goodsService.newGoods(shopcode)));
    }

    @ApiOperation(value = "感兴趣", notes="感兴趣")
    @GetMapping("/likes")
    public Mono<Info> likes(@RequestParam("code")String code) {
        Goods goods= goodsService.get(code);
        goods.setLikes(goods.getLikes()+1);
        goodsService.save(goods);
        return Mono.just(Info.SUCCESS(goods));
    }

    @ApiOperation(value = "获取我的商品信息",notes = "获取我的商品信息")
    @PostMapping("/mylistByPage")
    public Mono<Info> mylistByPage(Principal principal,  @RequestParam("page")Integer page) {

        return Mono.just(Info.SUCCESS( goodsService.mylistByPage(principal.getName(),page)));
    }
}
