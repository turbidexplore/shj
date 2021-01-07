package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Brand;
import com.turbid.explore.pojo.Shop;
import com.turbid.explore.pojo.Visitor;
import com.turbid.explore.repository.BrandRepositroy;
import com.turbid.explore.repository.NativeContentRepositroy;
import com.turbid.explore.service.BrandService;
import com.turbid.explore.service.ShopService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.service.VisitorService;
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
import java.util.*;

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
        String content=brand.getContent().replace(String.format("height: ^\\d{n}$px;"),"height: auto;");
        brand.setContent(content);
        brand.setCreate_time(new Date());
        brand.setCompany(shopService.getByUser(principal.getName()));
        return Mono.just(Info.SUCCESS( brandService.save(brand)));
    }

    @ApiOperation(value = "删除品牌信息", notes="删除品牌信息")
    @DeleteMapping("/delete")
    public Mono<Info> delete(Principal principal, @RequestParam("code") String code) {
        brandService.remove(code);
        return Mono.just(Info.SUCCESS(null ));
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
        try {
            Shop shop=  shopService.getByCode(code);
            Visitor visitor = new Visitor();
            visitor.setShopcode(shop.getCode());
            visitor.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
            visitorService.save(visitor);
        }catch (Exception e){

        }
        return Mono.just(Info.SUCCESS( brandService.get(code)));
    }

    @Autowired
    private NativeContentRepositroy nativeContentRepositroy;

    @ApiOperation(value = "通过商铺code获取品牌", notes="通过商铺code获取品牌")
    @GetMapping("/getByShop")
    public Mono<Info> getByShop(@RequestParam("code") String code) {
        List data=new ArrayList();
        brandService.getByShop(code).forEach(a->{
            Map i=new HashMap();
            i.put("data",a);
            Pageable pageable=  new PageRequest(0,5, Sort.Direction.DESC,"create_time");
            i.put("cp",nativeContentRepositroy.findByBrandcode(pageable,a.getCode()).getContent());
            data.add(i);
        });
        return Mono.just(Info.SUCCESS(data ));
    }

    @ApiOperation(value = "通过label获取品牌信息", notes="通过label获取品牌信息")
    @GetMapping("/getbylabel")
    public Mono<Info> getbylabel(@RequestParam(value = "classgroup",required = false)String classgroup,@RequestParam(value = "brandgroup",required = false)String brandgroup) {
        return Mono.just(Info.SUCCESS(brandService.getByLabel(classgroup,brandgroup) ));
    }


    @ApiOperation(value = "通过label获取品牌信息", notes="通过label获取品牌信息")
    @GetMapping("/getbylabel1")
    public Mono<Info> getbylabel1(@RequestParam(value = "classgroup",required = false)String classgroup,@RequestParam(value = "brandgroup",required = false)String brandgroup) {
        List data=new ArrayList();
        brandService.getByLabel(classgroup,brandgroup).forEach(d->{
            Map i=new HashMap();
            i.put("data",d);
            Pageable pageable=  new PageRequest(0,4, Sort.Direction.DESC,"idx");
            List img=new ArrayList();
            nativeContentRepositroy.wuyu(pageable,d.getCode()).getContent().forEach(a->{
                img.add(a.split(",")[0]);
            });
            i.put("img",img);
            data.add(i);
        });
        return Mono.just(Info.SUCCESS(data ));
    }

    @ApiOperation(value = "通过label获取品牌信息", notes="通过label获取品牌信息")
    @GetMapping("/infoByLabel")
    public Mono<Info> infoByLabel(@RequestParam(value = "type")Integer type) {
        Map it=new HashMap();
        if(type==0) {
            String a="极简,轻奢,中式,美式,欧式,定制";
            String b="jj,qs,zs,ms,os,dz";
            for (int i=0;i<a.split(",").length;i++) {
                List data=new ArrayList();
                brandService.getByLabel(a.split(",")[i], null).forEach(d -> {
                    Map item = new HashMap();
                    item.put("data", d);
                    Pageable pageable = new PageRequest(0, 4, Sort.Direction.DESC, "idx");
                    List img = new ArrayList();
                    nativeContentRepositroy.wuyu(pageable, d.getCode()).getContent().forEach(e -> {
                        img.add(e.split(",")[0]);
                    });
                    item.put("img", img);
                    data.add(item);
                });
                it.put(b.split(",")[i],data);
            }
        }else {
            String a="家纺,饰品,灯具";
            String b="jf,sp,dj";
            for (int i=0;i<a.split(",").length;i++) {

                List data=new ArrayList();
                brandService.getByLabel(null, a.split(",")[i]).forEach(d -> {
                    Map item = new HashMap();
                    item.put("data", d);
                    Pageable pageable = new PageRequest(0, 4, Sort.Direction.DESC, "idx");
                    List img = new ArrayList();
                    nativeContentRepositroy.wuyu(pageable, d.getCode()).getContent().forEach(c -> {
                        img.add(c.split(",")[0]);
                    });
                    item.put("img", img);
                    data.add(item);
                });
                it.put(b.split(",")[i],data);
            }
        }
        return Mono.just(Info.SUCCESS(it ));
    }


}
