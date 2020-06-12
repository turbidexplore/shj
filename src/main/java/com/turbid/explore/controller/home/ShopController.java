package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.*;
import com.turbid.explore.repository.ShopFansRepository;
import com.turbid.explore.service.*;
import com.turbid.explore.tools.CodeLib;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private BrandService brandService;

    @ApiOperation(value = "获取商铺信息", notes="获取商铺信息")
    @GetMapping("/get")
    public Mono<Info> get(Principal principal) {
        try {
            Shop shop=shopService.getByUser(principal.getName());
            shop.setSeecount(visitorService.companyCount(shop.getCode()));
            shop.setFanscount(followService.followmeCount(principal.getName()));
            shop.setHat(85);
            return Mono.just(Info.SUCCESS(shop));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(null));
        }

    }

    @Autowired
    private ShopFansRepository shopFansRepository;

    @ApiOperation(value = "关注店铺", notes="关注店铺")
    @PutMapping("/follow")
    public Mono<Info> follow(Principal principal,@RequestParam("shopcode")String shopcode) {
        try {
            ShopFans shopFans=new ShopFans();
            shopFans.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
            shopFans.setShop(shopService.getByCode(shopcode));
            return Mono.just(Info.SUCCESS(shopFansRepository.saveAndFlush(shopFans)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(null));
        }
    }

    @ApiOperation(value = "个人获取关注店铺", notes="个人获取关注店铺")
    @GetMapping("/myfollow")
    public Mono<Info> myfollow(Principal principal,@RequestParam("page")Integer page) {
        try {
            Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
            Page<ShopFans> pages=  shopFansRepository.findByUserSecurityPage(pageable,principal.getName());
            return Mono.just(Info.SUCCESS(pages.getContent()));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(null));
        }
    }

    @ApiOperation(value = "获取店铺粉丝", notes="获取店铺粉丝")
    @GetMapping("/getfollow")
    public Mono<Info> getfollow(Principal principal,@RequestParam("shopcode")String shopcode,@RequestParam("page")Integer page) {
        try {
            Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
            Page<ShopFans> pages=  shopFansRepository.findByShopPage(pageable,shopcode);
            return Mono.just(Info.SUCCESS(pages.getContent()));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(null));
        }
    }

    @ApiOperation(value = "获取店铺粉丝总数", notes="获取店铺粉丝总数")
    @GetMapping("/getfollowcount")
    public Mono<Info> getfollowcount(Principal principal,@RequestParam("shopcode")String shopcode) {
        try {

            return Mono.just(Info.SUCCESS(shopFansRepository.countByShopCode(shopcode)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(null));
        }
    }

    @ApiOperation(value = "通过商铺code获取商铺信息", notes="通过商铺code获取商铺信息")
    @GetMapping("/getbycode")
    public Mono<Info> getbycode(Principal principal,@RequestParam("code")String code) {
        Shop shop=  shopService.getByCode(code);
        if(null!=principal) {
            Visitor visitor = new Visitor();
            visitor.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
            visitor.setShopcode(code);
            visitorService.save(visitor);
        }
        return Mono.just(Info.SUCCESS( shop));
    }

    @ApiOperation(value = "通过商铺usercode获取商铺信息", notes="通过商铺usercode获取商铺信息")
    @GetMapping("/getbyusercode")
    public Mono<Info> getbyusercode(Principal principal,@RequestParam("usercode")String usercode) {
        try {
            Shop shop=  shopService.getByUsercode(usercode);
            Visitor visitor=new Visitor();
            visitor.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
            visitor.setShopcode(shop.getCode());
            visitorService.save(visitor);

            return Mono.just(Info.SUCCESS( shop));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS( null));
        }

    }


    @ApiOperation(value = "更新商铺信息", notes="更新商铺信息")
    @PutMapping("/update")
    public Mono<Info> update(Principal principal,@RequestBody Shop shop) {
        Shop old=shopService.getByCode(shop.getCode());
        shop.setUserSecurity(old.getUserSecurity());
        shop.setCreate_time(new Date());
        shop.setIschoose(old.getIschoose());
        shop.setStatus(old.getStatus());
        shop.setHat(old.getHat());
        shop.setFanscount(old.getFanscount());
        shop.setSeecount(old.getSeecount());
        return Mono.just(Info.SUCCESS( shopService.save(shop)));
    }

    @ApiOperation(value = "通过商铺label获取商铺信息", notes="通过商铺label获取商铺信息")
    @GetMapping("/getbylabel")
    public Mono<Info> getbylabel(@RequestParam(value = "classgroup",required = false)String classgroup,@RequestParam(value = "brandgroup",required = false)String brandgroup) {
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
       List<Shop> shops= shopService.getByChoose(label,page);
        System.out.println(shops.size());
        return Mono.just(Info.SUCCESS(shops ));
    }

    @ApiOperation(value = "官方推荐", notes="官方推荐")
    @GetMapping("/recommend")
    public Mono<Info> recommend(Principal principal,@RequestParam(value = "page")Integer page) {
        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        return Mono.just(Info.SUCCESS( shopService.recommend(principal,page,userSecurity.getLikes())));
    }

    @ApiOperation(value = "查看招商加盟", notes="查看招商加盟")
    @GetMapping("/seezsjm")
    public Mono<Info> seezsjm(Principal principal,@RequestParam(value = "code")String code) {
        Visitor visitor=new Visitor();
        visitor.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        visitor.setShopcode(code+"zsjm");
        visitorService.save(visitor);
        return Mono.just(Info.SUCCESS( null));
    }

    @ApiOperation(value = "招商加盟", notes="招商加盟")
    @GetMapping("/zsjm")
    public Mono<Info> zsjm(Principal principal,@RequestParam(value = "page")Integer page,@RequestParam(value = "type",required = false)String type) {
        List<Map<String,Object>> data=new ArrayList<>();
        shopService.zsjm(principal,page,type).forEach(v->{
            Map<String,Object> item=new HashMap<>();
            item.put("companyname",v.getCompanyname());
            item.put("name",v.getName());
            item.put("logo",v.getLogo());
            item.put("area","全国");
            item.put("content",v.getBusinessscope());
            item.put("address",v.getCompanyaddress());
            item.put("label",v.getLabel());
            item.put("user",v.getUserSecurity());
            item.put("ischoose",v.getIschoose());
            item.put("banner",v.getBanner());
            try {
                item.put("brand",brandService.getOneByShop(v.getCode()));
            }catch (Exception e){
                item.put("brand",null);
            }

            item.put("investmentamount",v.getBzj());
            item.put("showimg",v.getCompany_show());
            item.put("shopcode",v.getCode());
            item.put("shopcount",50);
            item.put("dateofestablishment","2018-08-01");
            try {
                if(null!=v.getMargin()&&!"".equals(v.getMargin())&&!v.getMargin().equals(null)) {
                    item.put("bzj", v.getMargin());
                }else {
                    item.put("bzj","无");
                }
            }catch (Exception e){
                item.put("bzj","无");
            }
            data.add(item);
        });
        return Mono.just(Info.SUCCESS(data ));
    }

    @Autowired
    private FollowService followService;

    @Autowired
    private CaseService caseService;

    @ApiOperation(value = "店铺统计", notes="店铺统计")
    @GetMapping("/count")
    public Mono<Info> count(Principal principal,@RequestParam("code")String code) {
        Map<String,Object> data =new HashMap<>();
        data.put("followcount",shopFansRepository.findByShopCode(code));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        data.put("today",visitorService.count(dateStr,code));
        sdf = new SimpleDateFormat("yyyy-MM");
         dateStr = sdf.format(new Date());
        data.put("tomom",visitorService.count(dateStr, code));
        data.put("casecount",caseService.casecount(principal.getName()));
        return Mono.just(Info.SUCCESS( data));
    }


    @ApiOperation(value = "需求列表推荐店铺", notes="需求列表推荐店铺")
    @GetMapping("/shopByNeeds")
    public Mono<Info> shopByNeeds(@RequestParam(value = "needscode")String needscode,@RequestParam(value = "page")Integer page) {
        return Mono.just(Info.SUCCESS( shopService.getByChoose(null,page)));
    }

    @Autowired
    private CommentService commentService;

    @Autowired
    private CollectionService collectionService;

    @ApiOperation(value = "店铺数据统计", notes="店铺数据统计")
    @GetMapping("/statistics")
    public Mono<Info> Statistics(Principal principal,@RequestParam("code")String code,@RequestParam(value = "time",required = false)String time) {
        Map<String,Object> data =new HashMap<>();
        data.put("hyd",85);
        data.put("brandvisitor",visitorService.brandCount(time, code));
        data.put("commentcount",commentService.commentCount(time, code));
        data.put("goodsvisitor",visitorService.goodsCount(time, code));
        data.put("goodslike",collectionService.goodslikes(time, code));
        data.put("businessvisitor",visitorService.count(time, code+"zsjm"));
        data.put("shopvisitor",visitorService.count(time, code));
        data.put("newfans",shopFansRepository.newfollowmeCount(principal.getName(),time));
        data.put("casecount",caseService.casecount(principal.getName()));
        return Mono.just(Info.SUCCESS( data));
    }


    @ApiOperation(value = "店铺数据统计", notes="店铺数据统计")
    @GetMapping("/StatisticsCount")
    public Mono<Info> StatisticsCount(Principal principal) {
        Shop shop=shopService.getByUser(principal.getName());
        Map<String,Object> data =new HashMap<>();
        List area= new ArrayList();
        followService.areaCount(principal).forEach(v->{
            Map item=new HashMap();
            item.put("name",v.getName());
            item.put("y",v.getY());
            area.add(item);
        });
        DateFormat df = new SimpleDateFormat("yyyy-");
        List fansadd= new ArrayList();
        List fanssee= new ArrayList();
        for(int i =1;i<=12;i++){
            String toyear=df.format(new Date());
            if(i>=10){
                toyear=toyear+i;
            }else {
                toyear=toyear+"0"+i;
            }
            System.out.println(toyear);
            fansadd.add(followService.newfollowmeCount(principal.getName(),toyear));
            try {
                fanssee.add(visitorService.count(toyear,shop.getCode()));
            }catch (Exception e){
                fanssee.add("0");
            }

        }
        data.put("area",area);
        data.put("fansadd",fansadd);
        data.put("fanssee",fanssee);
        List brand=new ArrayList();
        visitorService.brandinfo(principal.getName()).forEach(v->{
            brand.add(new Object[]{v.getName(),v.getCount()});
        });
        data.put("brand",brand);
        return Mono.just(Info.SUCCESS( data));
    }

    @ApiOperation(value = "店铺访问数据统计", notes="店铺访问数据统计")
    @GetMapping("/fwl")
    public Mono<Info> fwl(Principal principal,@RequestParam("code")String code) {
        List data =new ArrayList();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for(int i =14;i>=0;i--){
            String time = df.format(new Date().getTime()-i*24*60*60*1000);
            data.add(new Object[]{time,visitorService.count(time,code)});
        }

        return Mono.just(Info.SUCCESS( data));
    }


    @ApiOperation(value = "所有店铺信息", notes="所有店铺信息")
    @PostMapping("/shops")
    public Mono<Info> shops(Principal principal,@RequestParam("text")String text,@RequestParam("page")Integer page) {
        return Mono.just(Info.SUCCESS( shopService.findByText(text,page)));
    }

    @ApiOperation(value = "所有店铺信息总数", notes="所有店铺信息总数")
    @GetMapping("/shopscount")
    public Mono<Info> shopscount(Principal principal,@RequestParam("text")String text) {
        return Mono.just(Info.SUCCESS( shopService.findByTextCount(text)));
    }


    @ApiOperation(value = "设置严选", notes="设置严选")
    @PostMapping("/yanxuan")
    public Mono<Info> yanxuan(Principal principal,@RequestParam("code")String code,@RequestParam("status")Integer status) {
        Shop shop=shopService.getByCode(code);
        shop.setIschoose(status);
        return Mono.just(Info.SUCCESS(shopService.save(shop)));
    }


    @ApiOperation(value = "设置保证金和vip", notes="设置保证金和vip")
    @PostMapping("/bzjandvip")
    public Mono<Info> bzjandvip(Principal principal,@RequestParam("code")String code,@RequestParam(value = "m",required = false)Integer m,@RequestParam(value = "bzj",required = false)Integer bzj) throws ParseException {
        Shop shop=shopService.getByCode(code);

        if(m!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (shop.getVipday()==null||new Date().compareTo(sdf.parse(shop.getVipday()))>0) {
                shop.setVipday(sdf.format(CodeLib.addMonth(new Date(),m)));
            }else {
                shop.setVipday(sdf.format(CodeLib.addMonth(sdf.parse(shop.getVipday()),m)));
            }
        }else if(bzj!=null){
            shop.setMargin(bzj);
        }
        return Mono.just(Info.SUCCESS(shopService.save(shop)));
    }


}


