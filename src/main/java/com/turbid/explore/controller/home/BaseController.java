package com.turbid.explore.controller.home;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.turbid.explore.service.DistrictService;
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

    @ApiOperation(value = "用户类型", notes="用户类型")
    @GetMapping(value = "/usertype")
    public Mono<Info> usertype()  {
        JSONArray ja=new JSONArray();
        JSONObject jo=new JSONObject();
        jo.put("type",0);
        jo.put("text","设计师");
        ja.add(jo);
        jo=new JSONObject();
        jo.put("type",1);
        jo.put("text","经销商");
        ja.add(jo);
        jo=new JSONObject();
        jo.put("type",2);
        jo.put("text","工厂");
        ja.add(jo);
        jo=new JSONObject();
        jo.put("type",3);
        jo.put("text","设计公司");
        ja.add(jo);
        jo=new JSONObject();
        jo.put("type",4);
        jo.put("text","地产商");
        ja.add(jo);
        jo=new JSONObject();
        jo.put("type",5);
        jo.put("text","其他");
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
        map.put("price",9.9);
        map.put("costprice",9.9);
        info.put("SEE_NEEDS",map);
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

    @ApiOperation(value = "生成订单号", notes="生成订单号")
    @GetMapping(value = "/orderno")
    public Mono<Info> orderno()  {
        return Mono.just(Info.SUCCESS(CodeLib.randomCode(18,1)));
    }


    @ApiOperation(value = "获取案例主题", notes="获取案例主题")
    @GetMapping(value = "/casesubject")
    public Mono<Info> casesubject()  {
        return Mono.just(Info.SUCCESS(new String[]{"民宿空间","酒店空间","品牌酒店",
                "餐饮空间","住宅空间","办公空间","商业空间","玩乐健康","文化博物","建筑环境","景观绿化","平面布局","空间概念","摄影交流","手绘交流"}));
    }

    @ApiOperation(value = "获取品牌馆信息", notes="获取品牌馆信息")
    @GetMapping(value = "/brandgroup")
    public Mono<Info> brandgroup()  {
        List<Map<String,String>> list=new ArrayList<>();
        Map<String,String> map =new HashMap<>();
        map.put("name","家居馆");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","高端定制馆");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","全屋定制馆");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","设计馆");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","墙布馆");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","灯具馆");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","床品馆");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","饰品馆");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","地毯馆");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","窗帘");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","其他");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);

        return Mono.just(Info.SUCCESS(list));
    }

    @ApiOperation(value = "获取品牌馆分类信息", notes="获取品牌馆分类信息")
    @GetMapping(value = "/classgroup")
    public Mono<Info> classgroup()  {
        List<Map<String,String>> list=new ArrayList<>();
        Map<String,String> map =new HashMap<>();
        map.put("name","新古典");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","新中式");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","美式");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","轻奢");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","极简");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","北欧");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","现代");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        map=new HashMap<>();
        map.put("name","其他");
        map.put("logo","https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3862065343,1024477825&fm=15&gp=0.jpg");
        list.add(map);
        return Mono.just(Info.SUCCESS(list));
    }





}
