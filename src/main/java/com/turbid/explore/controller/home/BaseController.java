package com.turbid.explore.controller.home;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.turbid.explore.pojo.*;
import com.turbid.explore.repository.*;
import com.turbid.explore.service.*;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "基本数据接口")
@RestController
@RequestMapping("/base")
@CrossOrigin
public class BaseController {

    @Autowired
    private TJReposity tjReposity;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private LoginHisRepository loginHisRepository;

    @PutMapping(value = "/datainfo")
    public Mono<Info> datainfo(Principal principal,@RequestParam("type") Integer type,@RequestParam(value = "code",required = false) String code)  {
       TJ tj=new TJ();
       tj.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
       tj.setType(type);
       tj.setRelationcode(code);
        return Mono.just(Info.SUCCESS(tjReposity.saveAndFlush(tj)));
    }

    @PutMapping(value = "/setdevice")
    public Mono<Info> setdevice(Principal principal,@RequestParam("device") String device,@RequestParam(value = "os") String os,@RequestParam("version")String version)  {
        LoginHis loginHis=new LoginHis();
        loginHis.setLogintype("setdevice");
        loginHis.setOs(os);
        loginHis.setPhone(principal.getName());
        loginHis.setDevice(device);
        loginHis.setVersion(version);
        return Mono.just(Info.SUCCESS(loginHisRepository.saveAndFlush(loginHis)));
    }

    @GetMapping(value = "/share")
    public Mono<Info> share()  {
        Map<String,Object> info=new HashMap<>();
        info.put("community","http://m.deslibs.com/communitydetail/");
        info.put("shop","http://m.deslibs.com/companydetail/");
        info.put("goods","http://m.deslibs.com/productdetail/");
        info.put("study","http://m.deslibs.com/courselist/");
        info.put("studyinfo","http://m.deslibs.com/coursedetail/");
        info.put("caseinfo","http://m.deslibs.com/casedetail/");
        info.put("nativecontent","http://m.deslibs.com/inspirationimgs/");
        info.put("brand","http://m.deslibs.com/brandetail/");
        info.put("share","http://m.deslibs.com/home/toapp");
        info.put("product","http://m.deslibs.com/productdetail/");
        return Mono.just(Info.SUCCESS(info));
    }

    @Autowired
    private ConfiginfoRepository configinfoRepository;

    @GetMapping(value = "/update")
    public Mono<Info> update(@RequestParam("version")String version)  {
        Configinfo configinfo=configinfoRepository.getOne(0);
        Map<String,Object> data=new HashMap<>();

        Map<String,Object> info=new HashMap<>();
        if (version!=configinfo.getAndroidversion()&&!version.equals(configinfo.getAndroidversion())){
           info.put("isupdate",true);
        }else {
           info.put("isupdate",false);
        }
        info.put("version",configinfo.getAndroidversion());
        info.put("desc",configinfo.getAndroidcontent());
        info.put("forceupdate",configinfo.isAndroidupdate());
        info.put("url","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/shehuijia.apk");
        data.put("android",info);
        info=new HashMap<>();
        if (version!=configinfo.getIosversion()&&!version.equals(configinfo.getIosversion())){
            info.put("isupdate",true);
        }else {
            info.put("isupdate",false);
        }
        info.put("desc",configinfo.getIoscontent());
        info.put("version",configinfo.getIosversion());
        info.put("forceupdate",configinfo.isIosupdate());
        data.put("ios",info);
        return Mono.just(Info.SUCCESS(data));
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

    @Autowired
    City_CNReposity city_cnReposity;

    @ApiOperation(value = "获取城市信息", notes="获取城市信息")
    @GetMapping(value = "/citys")
    public Mono<Info> citys(@RequestParam("type")Integer type,@RequestParam(value = "text",required = false)String text)  {
        switch (type){
            case 0:
                return Mono.just(Info.SUCCESS(city_cnReposity.countrys()));
            case 1:
                return Mono.just(Info.SUCCESS(city_cnReposity.states(text)));
            case 2:
                return Mono.just(Info.SUCCESS(city_cnReposity.citys(text)));
            case 3:
                return Mono.just(Info.SUCCESS(city_cnReposity.citys1(text)));
            default:
                return Mono.just(Info.SUCCESS(null));
        }
    }

    @ApiOperation(value = "获取产品风格", notes="获取产品风格")
    @GetMapping(value = "/styles")
    public Mono<Info> styles()  {
        return Mono.just(Info.SUCCESS(new String[]{"极简","轻奢","中式","美式","欧式","原创","其他"}));
    }


    @ApiOperation(value = "获取产品品类", notes="获取产品品类")
    @GetMapping(value = "/categorys")
    public Mono<Info> categorys()  {
        return Mono.just(Info.SUCCESS(new String[]{"窗帘","墙布","画品","灯具","床品","饰品","定制"}));
    }

    @ApiOperation(value = "获取特价仓产品品类", notes="获取特价仓产品品类")
    @GetMapping(value = "/tjccategorys")
    public Mono<Info> tjccategorys()  {
        return Mono.just(Info.SUCCESS(new String[]{"成品家具","全屋定制","成品定制","窗帘","墙布","画品","灯具","饰品","地毯"}));
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
        return Mono.just(Info.SUCCESS(new String[]{"住宅","餐厅","展厅","酒店","办公","其他"}));
    }

    @ApiOperation(value = "获取品牌馆信息", notes="获取品牌馆信息")
    @GetMapping(value = "/brandgroup")
    public Mono<Info> brandgroup()  {
        List<Map<String,String>> list=new ArrayList<>();
        Map<String,String> map =new HashMap<>();
        map.put("name","家具");
        map.put("name_en","furniture");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%BA%8C%E7%BA%A7/%E5%AE%B6%E5%85%B7%E9%A6%86.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/icon/%E5%93%81%E7%89%8C%E9%A6%86/%E5%AE%B6%E5%85%B7%E9%A6%86.png");
        list.add(map);
         map =new HashMap<>();
        map.put("name","家纺");
        map.put("name_en","furniture");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%BA%8C%E7%BA%A7/%E5%AE%B6%E5%85%B7%E9%A6%86.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/icon/%E5%93%81%E7%89%8C%E9%A6%86/%E5%AE%B6%E5%85%B7%E9%A6%86.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","饰品");
        map.put("name_en","high end customization");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%BA%8C%E7%BA%A7/%E9%AB%98%E7%AB%AF%E5%AE%9A%E5%88%B6%E9%A6%86.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/icon/%E5%93%81%E7%89%8C%E9%A6%86/%E9%AB%98%E7%AB%AF%E5%AE%9A%E5%88%B6%E9%A6%86.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","灯具");
        map.put("name_en","all room customization");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%BA%8C%E7%BA%A7/%E5%85%A8%E5%B1%8B%E5%AE%9A%E5%88%B6%E9%A6%86.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/icon/%E5%93%81%E7%89%8C%E9%A6%86/%E5%85%A8%E5%B1%8B%E5%AE%9A%E5%88%B6%E9%A6%86.png");
        list.add(map);
        return Mono.just(Info.SUCCESS(list));
    }



    @ApiOperation(value = "获取品牌馆分类信息", notes="获取品牌馆分类信息")
    @GetMapping(value = "/classgroup")
    public Mono<Info> classgroup()  {
        List<Map<String,String>> list=new ArrayList<>();
        Map<String,String> map =new HashMap<>();
        map.put("name","现代");
        map.put("name_en","Neoclassical");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%B8%89%E7%BA%A7/%E6%96%B0%E5%8F%A4%E5%85%B8.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E9%A3%8E%E6%A0%BC/%E6%96%B0%E5%8F%A4%E5%85%B8.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","中式");
        map.put("name_en","American style");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%B8%89%E7%BA%A7/%E7%BE%8E%E5%BC%8F.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E9%A3%8E%E6%A0%BC/%E7%BE%8E%E5%BC%8F.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","美式");
        map.put("name_en","Light luxury");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%B8%89%E7%BA%A7/%E8%BD%BB%E5%A5%A2.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E9%A3%8E%E6%A0%BC/%E8%BD%BB%E5%A5%A2.png");
        list.add(map);
        map =new HashMap<>();
        map.put("name","古典");
        map.put("name_en","Neoclassical");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%B8%89%E7%BA%A7/%E6%96%B0%E5%8F%A4%E5%85%B8.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E9%A3%8E%E6%A0%BC/%E6%96%B0%E5%8F%A4%E5%85%B8.png");
        list.add(map);
        map =new HashMap<>();
        map.put("name","工业");
        map.put("name_en","Neoclassical");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%B8%89%E7%BA%A7/%E6%96%B0%E5%8F%A4%E5%85%B8.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E9%A3%8E%E6%A0%BC/%E6%96%B0%E5%8F%A4%E5%85%B8.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","北欧");
        map.put("name_en","Minimalism");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%B8%89%E7%BA%A7/%E6%9E%81%E7%AE%80.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E9%A3%8E%E6%A0%BC/%E6%9E%81%E7%AE%80.png");
        list.add(map);
        map=new HashMap<>();
        map.put("name","其他");
        map.put("name_en","Northern Europe");
        map.put("banner","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E4%B8%89%E7%BA%A7/%E5%8C%97%E6%AC%A7.png");
        map.put("logo","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E9%A3%8E%E6%A0%BC/%E5%8C%97%E6%AC%A7.png");
        list.add(map);

        return Mono.just(Info.SUCCESS(list));
    }


    @ApiOperation(value = "达人研习社分类", notes="达人研习社分类")
    @GetMapping(value = "/studysubject")
    public Mono<Info> studysubject()  {
        List data=new ArrayList();
        Map<String,String> map =new HashMap<>();
        map.put("name","色彩风格课");
        map.put("img","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE/%E6%9D%90%E6%96%99%E5%B7%A5%E8%89%BA.png");
        data.add(map);
        map =new HashMap<>();
        map.put("name","意式品牌课");
        map.put("img","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE/%E9%A3%8E%E6%A0%BC%E8%A7%A3%E6%9E%90.png");
        data.add(map);
        map =new HashMap<>();
        map.put("name","软装搭配课");
        map.put("img","https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE/%E8%89%B2%E5%BD%A9%E6%90%AD%E9%85%8D.png");
        data.add(map);
        return Mono.just(Info.SUCCESS(data));
    }


    @Autowired
    private ProjectNeedsService projectNeedsService;

    @Autowired
    private ShopService shopService;

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
        return Mono.just(Info.SUCCESS(new String[]{"轻奢","极简","中式",
                "古典","美式"}));
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
                data.put("qaaInfo",qaaInfoService.search(text,page ));
                data.put("study",studyService.search(text,page));
                data.put("case",caseService.search(text,page));
                break;
        }
        return Mono.just(Info.SUCCESS(data));
    }


    @ApiOperation(value = "审核", notes="审核")
    @GetMapping(value = "/sh")
    public Mono<Info> sh() {
        return Mono.just(Info.SUCCESS(true));
    }


    @ApiOperation(value = "imgbanner", notes="imgbanner")
    @GetMapping(value = "/imgbanner")
    public Mono<Info> imgbanner() {
        Map<String,Object> data=new HashMap<>();
        data.put("case",caseService.listByPage(0,null,null,null).get(0).getIndexurl());
        data.put("nativecontent",nativeContentService.listByPageLabel(0,null,null).get(0).getFirstimage().split(",")[0]);
        return Mono.just(Info.SUCCESS(data));
    }

    @ApiOperation(value = "图库类别", notes="图库类别")
    @GetMapping(value = "/imgclass")
    public Mono<Info> imgclass()  {
        List<Map> data=new ArrayList<>();
        Map item=new HashMap();
        item.put("name","家具");
        item.put("items",new String[]{"床","沙发","茶几","桌子","椅子","柜子","置物架","其他"});
        data.add(item);
         item=new HashMap();
        item.put("name","家纺");
        item.put("items",new String[]{"窗帘","床品","靠垫","抱枕","其他"});
        data.add(item);
         item=new HashMap();
        item.put("name","配饰");
        item.put("items",new String[]{"摆件","挂画","墙纸","地毯","其他"});
        data.add(item);
         item=new HashMap();
        item.put("name","灯具");
        item.put("items",new String[]{"吊灯","壁灯","落地灯","台灯"});
        data.add(item);
        return Mono.just(Info.SUCCESS(data));
    }

    @Autowired
    private ProductReposity productReposity;

    @Autowired
    private CommunityReposity communityReposity;

    @Autowired
    private CaseRepositroy caseRepositroy;

    @Autowired
    private NativeContentRepositroy nativeContentRepositroy;

    @Autowired
    private ShopRepositroy shopRepositroy;

    @Autowired
    private StudyGroupRepository studyGroupRepository;


    @ApiOperation(value = "搜索2", notes="搜索 0案例 1图库 2找产品 3社区 4企业 5学习 6all")
    @PostMapping(value = "/search2")
    public Mono<Info> search2(@RequestParam("text")String text,@RequestParam("type")Integer type,@RequestParam("page")Integer page)  {
        Map data =new HashMap();
        Pageable pageable = new PageRequest(page,10, Sort.Direction.DESC,"create_time");
        switch (type){
            case 0:
                data.put("case",caseService.search(text,page));
                break;
            case 1:
                data.put("tk",nativeContentService.search(text,page));
                break;
            case 2:
                Page<Product> zcp=  productReposity.search(pageable,text);
                data.put("zcp",zcp.getContent());
                break;
            case 3:
                Page<Community> sq=  communityReposity.search(pageable,text);
                data.put("sq",sq.getContent());
                break;
            case 4:
                data.put("company",shopService.search(text,page));
                break;
            case 5:
                data.put("studygroup",studyGroupRepository.search(pageable,text).getContent());
                break;
            case 6:
                Pageable pageablea =  PageRequest.of(page,2, Sort.Direction.DESC,"create_time");
                data.put("case",caseRepositroy.search(pageablea,text).getContent());
                data.put("tk",nativeContentRepositroy.search(pageablea,text).getContent());
                data.put("zcp",productReposity.search(pageable,text).getContent());
                data.put("sq", communityReposity.search(pageable,text).getContent());
                data.put("company",shopRepositroy.search(pageablea,text).getContent());
                data.put("studygroup",studyGroupRepository.search(pageable,text).getContent());
                break;
        }
        return Mono.just(Info.SUCCESS(data));
    }
}



