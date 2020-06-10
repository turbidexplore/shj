package com.turbid.explore.controller.home;

import com.alibaba.fastjson.JSONObject;
import com.turbid.explore.pojo.*;
import com.turbid.explore.repository.*;
import com.turbid.explore.service.BannerService;
import com.turbid.explore.service.OrderService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.tools.CodeLib;
import com.turbid.explore.tools.Expressage;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Api(description = "积分商城模块")
@RestController
@RequestMapping("/shoping")
@CrossOrigin
public class ShopingController {

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private AddressRepository addressRepository;

    @ApiOperation(value = "收货地址列表")
    @GetMapping(value = "/addresslist")
    public Mono<Info> addresslist(Principal principal) {
        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        return Mono.just(Info.SUCCESS(addressRepository.findByUsercodeAndStatus(userSecurity.getCode(),0)));
    }

    @ApiOperation(value = "添加或更新收货地址")
    @PutMapping(value = "/addorupdateaddress")
    public Mono<Info> addorupdateaddress(Principal principal, @RequestBody Address address) {
        address.setStatus(0);
        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        address.setUsercode(userSecurity.getCode());
        address= addressRepository.saveAndFlush(address);
        if(null!=address.getIsdef()&&address.getIsdef()==true){
            defaddress(userSecurity.getCode(),address.getCode());
        }
        return Mono.just(Info.SUCCESS(address));
    }

    @ApiOperation(value = "设置默认收货地址")
    @PutMapping(value = "/setdefaddress")
    public Mono<Info> setdefaddress(Principal principal, @RequestParam("code")String code) {
        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        defaddress(userSecurity.getCode(),code);
        return Mono.just(Info.SUCCESS(null));
    }

    @ApiOperation(value = "获取默认地址")
    @GetMapping(value = "/getdefaddress")
    public Mono<Info> getdefaddress(Principal principal) {
        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        return Mono.just(Info.SUCCESS(addressRepository.findByUsercodeAndIsdefAndStatus(userSecurity.getCode(),true,0)));
    }

    @ApiOperation(value = "删除收货地址")
    @DeleteMapping(value = "/deleteaddress")
    public Mono<Info> deleteaddress(Principal principal, @RequestParam("code")String code) {
        addressRepository.updateStatus(code);
        return Mono.just(Info.SUCCESS(null));
    }


    public void defaddress(String usercode,String code){
        addressRepository.updateDef(usercode,code);
    }

    @Autowired
    private IntegralGoodsRepository integralGoodsRepository;

    @Autowired
    private IntegralGoodsOrderRepository integralGoodsOrderRepository;

    @Autowired
    private NoticeRepository noticeRepository;


    @ApiOperation(value = "添加积分商品")
    @PutMapping(value = "/addintegralgoods")
    public Mono<Info> addintegralgoods(Principal principal,@RequestBody IntegralGoods integralGoods) {
        integralGoods.setStatus(0);
        return Mono.just(Info.SUCCESS( integralGoodsRepository.saveAndFlush(integralGoods)));
    }

    @ApiOperation(value = "积分商品列表")
    @PostMapping(value = "/integralgoodslist")
    public Mono<Info> integralgoodslist(@RequestParam("page")Integer page) {
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<String> pages=  integralGoodsRepository.listbypage(pageable);
        return Mono.just(Info.SUCCESS( pages.getContent()));
    }

    @ApiOperation(value = "获取积分商品详情")
    @GetMapping(value = "/integralgoodsbycode")
    public Mono<Info> integralgoodsbycode(@RequestParam("code")String code) {
        return Mono.just(Info.SUCCESS(integralGoodsRepository.getOne(code)));
    }

    @ApiOperation(value = "查询快递轨迹")
    @GetMapping(value = "/getkdinfo")
    public Mono<Info> getkdinfo(@RequestParam("kdd")String kdd) {
        return Mono.just(Info.SUCCESS(JSONObject.parse(Expressage.info(kdd))));
    }

    @ApiOperation(value = "兑换积分商品")
    @PostMapping(value = "/buyintegralgoods")
    public Mono<Info> buyintegralgoods(Principal principal,@RequestParam("addresscode")String addresscode,@RequestParam("integralgoodscode")String integralgoodscode) {
        UserSecurity userSecurity= userSecurityService.findByPhone(principal.getName());
        IntegralGoods integralGoods=integralGoodsRepository.getOne(integralgoodscode);
        if(null!=userSecurity.getShb()&&userSecurity.getShb()>integralGoods.getPrice()){
            userSecurity.setShb(userSecurity.getShb()-integralGoods.getPrice());
            userSecurityService.save(userSecurity);
            IntegralGoodsOrder integralGoodsOrder=new IntegralGoodsOrder();
            integralGoodsOrder.setUserSecurity(userSecurity);
            integralGoodsOrder.setAddress(new Address(addresscode));
            integralGoodsOrder.setIntegralGoods(new IntegralGoods(integralgoodscode));
            integralGoodsOrder.setOrderno("JF:"+CodeLib.randomCode(12,1));
            integralGoodsOrder.setStatus(1);
            integralGoodsOrder=integralGoodsOrderRepository.saveAndFlush(integralGoodsOrder);
            integralGoods.setSkucount(integralGoods.getSkucount()-1);
            if(integralGoods.getSkucount()==0){
                integralGoods.setStatus(2);
            }
            integralGoodsRepository.saveAndFlush(integralGoods);
            orderinfo(integralGoodsOrder.getOrderno(),"JF_GOODS",integralGoods.getPrice().toString(),"shb",integralGoods.getTitle(),principal.getName(),1);
            noticeRepository.save(new Notice(integralGoodsOrder.getOrderno(),principal.getName(), "恭喜您！您已经成功兑换积分商品，请耐心等待收货。", "支付通知", 1, 0));
            return Mono.just(Info.SUCCESS("支付成功"));
        }else {
            return Mono.just(Info.ERROR("设汇币余额不足"));
        }

    }
    @Autowired
    private OrderService orderService;

    public void orderinfo(String orderno,String code,String price,String type,String body,String userphone,Integer status){
        Order order=new Order();
        order.setUserphone(userphone);
        order.setBody(body);
        order.setOrderno(orderno);
        order.setPaytype(type);
        order.setGoodscode(code);
        order.setPrice(price);
        order.setStatus(status);
        orderService.save(order);
    }



}
