package com.turbid.explore.controller.home;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.turbid.explore.pojo.Shop;
import com.turbid.explore.service.*;
import com.turbid.explore.tools.CodeLib;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "基本数据接口")
@RestController
@RequestMapping("/base")
@CrossOrigin
public class BaseController {

    @GetMapping(value = "/share")
    public Mono<Info> share()  {
        Map<String,String> info=new HashMap<>();
        info.put("shop","http://m.deslibs.com/#/shopHomePage");
        info.put("goods","http://m.deslibs.com/#/salesDetails");
        info.put("study","http://m.deslibs.com/#/study_details");
        info.put("nativecontent","http://m.deslibs.com/#/lingganAlbum");
        info.put("brand","http://m.deslibs.com/#/childBrand");
        return Mono.just(Info.SUCCESS(info));
    }

    @GetMapping(value = "/update")
    public Mono<Info> update(@RequestParam("version")Integer version)  {
        Map<String,Object> info=new HashMap<>();
       if (version==1){
           info.put("isupdate",false);
       }else {
           info.put("isupdate",true);
       }
        info.put("version","1.0.1");
        info.put("desc","添加了达人研习社模块");

        return Mono.just(Info.SUCCESS(info));
    }

    @GetMapping(value = "/basic")
    public Mono<Info> basic()  {
        Map<String,String> info=new HashMap<>();
        info.put("phonenumber","4006081051");
        info.put("privacyprotocol","https://www.deslibs.com/privacyprotocol");
        return Mono.just(Info.SUCCESS(info));
    }

    @ApiOperation(value = "用户类型", notes="用户类型")
    @GetMapping(value = "/usertype")
    public Mono<Info> usertype()  {
        JSONArray ja=new JSONArray();
        Map<String,Object> jo=new HashMap<>();
        jo.put("type",0);
        jo.put("img","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/%E4%BD%8D%E5%9B%BE%403x(3).png");
        jo.put("text","设计师");
        ja.add(jo);
        jo=new JSONObject();
        jo.put("type",1);
        jo.put("img","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/%E4%BD%8D%E5%9B%BE%403x(1).png");
        jo.put("text","经销商");
        ja.add(jo);
        jo=new JSONObject();
        jo.put("type",2);
        jo.put("img","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/%E4%BD%8D%E5%9B%BE%403x(2).png");
        jo.put("text","工厂");
        ja.add(jo);
        jo=new JSONObject();
        jo.put("type",3);
        jo.put("img","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/%E4%BD%8D%E5%9B%BE%403x(3).png");
        jo.put("text","设计公司");
        ja.add(jo);


        return Mono.just(Info.SUCCESS(ja));
    }

    @Autowired
    private DistrictService districtService;

    @ApiOperation(value = "获取区域信息", notes="获取区域信息")
    @GetMapping(value = "/area")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name="id", dataType="Integer", required=true, value="上级id"),
    })
    public Mono<Info> area(@RequestParam("id")Integer id)  {

        return Mono.just(Info.SUCCESS(districtService.getAreaByPid(id)));
    }

    @ApiOperation(value = "获取产品风格", notes="获取产品风格")
    @GetMapping(value = "/styles")
    public Mono<Info> styles()  {
        return Mono.just(Info.SUCCESS(new String[]{"新古典","美式","轻奢","极简","北欧","现代","新中式"}));
    }


    @ApiOperation(value = "获取产品品类", notes="获取产品品类")
    @GetMapping(value = "/categorys")
    public Mono<Info> categorys()  {
        return Mono.just(Info.SUCCESS(new String[]{"成品家具","全屋定制","成品定制","窗帘","墙布","画品","灯具","生活用品","饰品","地毯","其他"}));
    }

    @ApiOperation(value = "获取设计类型", notes="获取设计类型")
    @GetMapping(value = "/destype")
    public Mono<Info> destype()  {
        return Mono.just(Info.SUCCESS(new String[]{"产品配色","产品研发","空间设计","其他"}));
    }


    @ApiOperation(value = "获取支付编码", notes="获取支付编码")
    @GetMapping(value = "/paycode")
    public Mono<Info> paycode()  {
        Map<String,Map<String,Object>> info=new HashMap<>();
        Map<String,Object> map=new HashMap<>();
        map.put("key","需求加急");
        map.put("value","NEEDS_URGENT");
        map.put("price",0.01);
        map.put("costprice",0.01);
        info.put("NEEDS_URGENT",map);
        map=new HashMap<>();
        map.put("key","逐条查看信息");
        map.put("value","SEE_NEEDS");
        map.put("price",0.01);
        map.put("costprice",0.01);
        info.put("SEE_NEEDS",map);
        map=new HashMap<>();
        map.put("key","课程购买");
        map.put("value","SEE_STUDY");
        map.put("price",0.01);
        map.put("costprice",0.01);
        info.put("SEE_STUDY",map);
        map=new HashMap<>();
        map.put("key","工厂VIP月卡");
        map.put("value","MMVIP");
        map.put("price",1280);
        map.put("costprice",1980);
        info.put("MMVIP",map);
        map=new HashMap<>();
        map.put("key","工厂VIP三月卡");
        map.put("value","MSVIP");
        map.put("price",2980);
        map.put("costprice",5980);
        info.put("MSVIP",map);
        map=new HashMap<>();
        map.put("key","工厂VIP年卡");
        map.put("value","MYVIP");
        map.put("price",6800);
        map.put("costprice",12800);
        info.put("MYVIP",map);
        return Mono.just(Info.SUCCESS(info));
    }


    @ApiOperation(value = "获取案例主题", notes="获取案例主题")
    @GetMapping(value = "/casesubject")
    public Mono<Info> casesubject()  {
        return Mono.just(Info.SUCCESS(new String[]{"民宿空间","酒店空间",
                "餐饮空间","住宅空间","办公空间","商业空间"}));
    }

    @ApiOperation(value = "获取品牌馆信息", notes="获取品牌馆信息")
    @GetMapping(value = "/brandgroup")
    public Mono<Info> brandgroup()  {
        List<Map<String,String>> list=new ArrayList<>();
        Map<String,String> map =new HashMap<>();
        map.put("name","家具馆");
        map.put("name_en","furniture");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%BA%8C%E7%BA%A7/%E5%AE%B6%E5%85%B7%E9%A6%86.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/icon/%E5%93%81%E7%89%8C%E9%A6%86/%E5%AE%B6%E5%85%B7%E9%A6%86.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","高端定制馆");
        map.put("name_en","high end customization");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%BA%8C%E7%BA%A7/%E9%AB%98%E7%AB%AF%E5%AE%9A%E5%88%B6%E9%A6%86.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/icon/%E5%93%81%E7%89%8C%E9%A6%86/%E9%AB%98%E7%AB%AF%E5%AE%9A%E5%88%B6%E9%A6%86.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","全屋定制馆");
        map.put("name_en","all room customization");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%BA%8C%E7%BA%A7/%E5%85%A8%E5%B1%8B%E5%AE%9A%E5%88%B6%E9%A6%86.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/icon/%E5%93%81%E7%89%8C%E9%A6%86/%E5%85%A8%E5%B1%8B%E5%AE%9A%E5%88%B6%E9%A6%86.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","设计馆");
        map.put("name_en","design");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%BA%8C%E7%BA%A7/%E8%AE%BE%E8%AE%A1%E9%A6%86.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/icon/%E5%93%81%E7%89%8C%E9%A6%86/%E8%AE%BE%E8%AE%A1%E9%A6%86.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","墙布馆");
        map.put("name_en","wall cloth");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%BA%8C%E7%BA%A7/%E5%A2%99%E5%B8%83%E9%A6%86.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/icon/%E5%93%81%E7%89%8C%E9%A6%86/%E5%A2%99%E5%B8%83%E9%A6%86.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","灯具馆");
        map.put("name_en","lamps and lanterns");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%BA%8C%E7%BA%A7/%E7%81%AF%E5%85%B7%E9%A6%86.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/icon/%E5%93%81%E7%89%8C%E9%A6%86/%E7%81%AF%E5%85%B7%E9%A6%86.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","床品馆");
        map.put("name_en","bedding");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%BA%8C%E7%BA%A7/%E5%BA%8A%E5%93%81%E9%A6%86.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/icon/%E5%93%81%E7%89%8C%E9%A6%86/%E5%BA%8A%E5%93%81%E9%A6%86.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","饰品馆");
        map.put("name_en","ornaments");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%BA%8C%E7%BA%A7/%E9%A5%B0%E5%93%81%E9%A6%86.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/icon/%E5%93%81%E7%89%8C%E9%A6%86/%E9%A5%B0%E5%93%81%E9%A6%86.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","地毯馆");
        map.put("name_en","carpet");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%BA%8C%E7%BA%A7/%E5%9C%B0%E6%AF%AF%E9%A6%86.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/icon/%E5%93%81%E7%89%8C%E9%A6%86/%E5%9C%B0%E6%AF%AF%E9%A6%86.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","窗帘馆");
        map.put("name_en","window curtains");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%BA%8C%E7%BA%A7/%E7%AA%97%E5%B8%98%E9%A6%86.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/icon/%E5%93%81%E7%89%8C%E9%A6%86/%E7%AA%97%E5%B8%98%E9%A6%86.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","画品馆");
        map.put("name_en","quality of paintings");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%BA%8C%E7%BA%A7/%E7%94%BB%E5%93%81%E9%A6%86.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/icon/%E5%93%81%E7%89%8C%E9%A6%86/%E7%94%BB%E5%93%81%E9%A6%86.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","综合馆");
        map.put("name_en","comprehensive");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%BA%8C%E7%BA%A7/%E7%BB%BC%E5%90%88%E9%A6%86.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/icon/%E5%93%81%E7%89%8C%E9%A6%86/%E7%BB%BC%E5%90%88%E9%A6%86.png");
        list.add(map);

        return Mono.just(Info.SUCCESS(list));
    }



    @ApiOperation(value = "获取品牌馆分类信息", notes="获取品牌馆分类信息")
    @GetMapping(value = "/classgroup")
    public Mono<Info> classgroup()  {
        List<Map<String,String>> list=new ArrayList<>();
        Map<String,String> map =new HashMap<>();
        map.put("name","新古典");
        map.put("name_en","Neoclassical");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%B8%89%E7%BA%A7/%E6%96%B0%E5%8F%A4%E5%85%B8.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E9%A3%8E%E6%A0%BC/%E6%96%B0%E5%8F%A4%E5%85%B8.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","新中式");
        map.put("name_en","New Chinese style");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%B8%89%E7%BA%A7/%E6%96%B0%E4%B8%AD%E5%BC%8F.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E9%A3%8E%E6%A0%BC/%E6%96%B0%E4%B8%AD%E5%BC%8F.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","美式");
        map.put("name_en","American style");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%B8%89%E7%BA%A7/%E7%BE%8E%E5%BC%8F.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E9%A3%8E%E6%A0%BC/%E7%BE%8E%E5%BC%8F.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","轻奢");
        map.put("name_en","Light luxury");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%B8%89%E7%BA%A7/%E8%BD%BB%E5%A5%A2.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E9%A3%8E%E6%A0%BC/%E8%BD%BB%E5%A5%A2.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","极简");
        map.put("name_en","Minimalism");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%B8%89%E7%BA%A7/%E6%9E%81%E7%AE%80.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E9%A3%8E%E6%A0%BC/%E6%9E%81%E7%AE%80.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","北欧");
        map.put("name_en","Northern Europe");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%B8%89%E7%BA%A7/%E5%8C%97%E6%AC%A7.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E9%A3%8E%E6%A0%BC/%E5%8C%97%E6%AC%A7.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","现代");
        map.put("name_en","Modern");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%B8%89%E7%BA%A7/%E7%8E%B0%E4%BB%A3.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E9%A3%8E%E6%A0%BC/%E7%8E%B0%E4%BB%A3.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","其他");
        map.put("name_en","Other");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%B8%89%E7%BA%A7/%E5%85%B6%E4%BB%96.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E9%A3%8E%E6%A0%BC/%E5%85%B6%E4%BB%96.png");
        list.add(map);
        return Mono.just(Info.SUCCESS(list));
    }


    @ApiOperation(value = "达人研习社分类", notes="达人研习社分类")
    @GetMapping(value = "/studysubject")
    public Mono<Info> studysubject()  {
        List data=new ArrayList();
        Map<String,String> map =new HashMap<>();
        map.put("name","材料工艺");
        map.put("img","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE/%E6%9D%90%E6%96%99%E5%B7%A5%E8%89%BA.png");
        data.add(map);
        map =new HashMap<>();
        map.put("name","风格解析");
        map.put("img","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE/%E9%A3%8E%E6%A0%BC%E8%A7%A3%E6%9E%90.png");
        data.add(map);
        map =new HashMap<>();
        map.put("name","色彩搭配");
        map.put("img","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE/%E8%89%B2%E5%BD%A9%E6%90%AD%E9%85%8D.png");
        data.add(map);
        map =new HashMap<>();
        map.put("name","布艺软装");
        map.put("img","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE/%E5%B8%83%E8%89%BA%E8%BD%AF%E8%A3%85.png");
        data.add(map);
        map =new HashMap<>();
        map.put("name","运营思维");
        map.put("img","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE/%E8%BF%90%E8%90%A5%E6%80%9D%E7%BB%B4.png");
        data.add(map);
        map =new HashMap<>();
        map.put("name","空间方案");
        map.put("img","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE/%E7%A9%BA%E9%97%B4%E6%96%B9%E6%A1%88.png");
        data.add(map);
        map =new HashMap<>();
        map.put("name","销售技巧");
        map.put("img","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE/%E9%94%80%E5%94%AE%E6%8A%80%E5%B7%A7.png");
        data.add(map);
        map =new HashMap<>();
        map.put("name","风水设计");
        map.put("img","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE/%E9%A3%8E%E6%B0%B4%E8%AE%BE%E8%AE%A1.png");
        data.add(map);

        return Mono.just(Info.SUCCESS(data));
    }


    @Autowired
    private ProjectNeedsService projectNeedsService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private QaaInfoService qaaInfoService;

    @Autowired
    private StudyService studyService;

    @Autowired
    private CaseService caseService;

    @Autowired
    private NativeContentService nativeContentService;

    @ApiOperation(value = "热门搜索", notes="热门搜索")
    @GetMapping(value = "/searchhat")
    public Mono<Info> searchhat()  {
        return Mono.just(Info.SUCCESS(new String[]{"民宿空间","酒店空间","品牌酒店",
                "餐饮空间","住宅空间","美式"}));
    }

    @ApiOperation(value = "搜索", notes="搜索 0需求 2品牌 3特加仓 4问答 5课程 6案例 7灵感")
    @PostMapping(value = "/search")
    public Mono<Info> search(@RequestParam("text")String text,@RequestParam("type")Integer type,@RequestParam("page")Integer page)  {
        Map data =new HashMap();

        switch (type){
            case 0:
                data.put("needs",projectNeedsService.search(text,page));
                break;
            case 1:
                data.put("zsjm",null);
                break;
            case 2:
                data.put("company",shopService.search(text,page));
                break;
            case 3:
                data.put("goods",goodsService.search(text,page));
                break;
            case 4:
                data.put("qaaInfo",qaaInfoService.search(text,page));
                break;
            case 5:
                data.put("study",studyService.search(text,page));
                break;
            case 6:
                data.put("case",caseService.search(text,page));
                break;
            case 7:
                data.put("lg",nativeContentService.search(text,page));
                break;
            default:
                data.put("needs",projectNeedsService.search(text,page));
                data.put("company",shopService.search(text,page));
                data.put("goods",goodsService.search(text,page));
                data.put("qaaInfo",qaaInfoService.search(text,page));
                data.put("study",studyService.search(text,page));
                data.put("case",caseService.search(text,page));
                break;
        }
        return Mono.just(Info.SUCCESS(data));
    }


}
