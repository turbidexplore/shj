package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.CallCount;
import com.turbid.explore.repository.CallCountRepository;
import com.turbid.explore.repository.ShopFansRepository;
import com.turbid.explore.service.*;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Api(description = "统计接口")
@RestController
@RequestMapping("/count")
@CrossOrigin
public class CountController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private ShopFansRepository shopFansRepository;

    @Autowired
    private CaseService caseService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private CallCountRepository callCountRepository;

    @ApiOperation(value = "店铺数据统计", notes="店铺数据统计")
    @GetMapping("/statistics")
    public Mono<Info> Statistics(Principal principal, @RequestParam("shopcode")String shopcode, @RequestParam(value = "time",required = false)String time) {
        Map<String,Object> data =new HashMap<>();
        data.put("callmecount", callCountRepository.callshopcount(shopcode,time));
        Pageable pageable = new PageRequest(0,4, Sort.Direction.DESC,"create_time");
        Page<String> pages=  callCountRepository.callshop(pageable,shopcode,time);
        data.put("callme", pages.getContent());
        data.put("brandvisitor",visitorService.brandCount(time, shopcode));
        data.put("commentcount",commentService.commentCount(time, shopcode));
        data.put("goodsvisitor",visitorService.goodsCount(time, shopcode));
        data.put("goodslike",collectionService.goodslikes(time, shopcode));
        data.put("businessvisitor",visitorService.count(time, shopcode+"zsjm"));
        data.put("shopvisitor",visitorService.count(time, shopcode));
        data.put("newfans",shopFansRepository.newfollowmeCount(shopcode,time));

        Map callinfo =new HashMap();
        List atime =new ArrayList();
        List adata=new ArrayList();
        if(time.length()==4) {
            for (int i = 1; i <=12; i++) {
                String stime= time;
                if (i<10){
                    stime=stime+"-0"+i;
                }else {
                    stime=stime+"-"+i;
                }
                System.out.println(stime);
                atime.add(stime.replace(time+"-",""));
                adata.add(callCountRepository.callshopcount(shopcode,stime));
            }
        }else if(time.length()==7){
            int count=30;
            if(time.contains("-01")||time.contains("-03")||time.contains("-05")||time.contains("-07")||time.contains("-08")||time.contains("-10")||time.contains("-12")){
                count=31;
            }else if(time.contains("-02")){
                count=28;
            }
            for (int i = 1; i <= count; i++) {
                String stime= time;
                if (i<10){
                    stime=stime+"-0"+i;
                }else {
                    stime=stime+"-"+i;
                }
                System.out.println(stime);
                atime.add(stime.replace(time+"-",""));
                adata.add(callCountRepository.callshopcount(shopcode,stime));
            }
        }else if(time.length()==10){
            for (int i = 1; i <=24; i++) {
                String stime= time;
                if (i<10){
                    stime=stime+" 0"+i;
                }else {
                    stime=stime+" "+i;
                }
                System.out.println(stime);
                adata.add(callCountRepository.callshopcount(shopcode,stime));
                atime.add(stime.replace(time,""));
            }
        }else {
            for (int i = 2; i >=0; i--) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                String dateStr = sdf.format(new Date());
                atime.add(Integer.parseInt(dateStr)-i);
                adata.add( callCountRepository.callshopcount( shopcode,String.valueOf(Integer.parseInt(dateStr)-i)));

            }
        }
        callinfo.put("time",atime);
        callinfo.put("data",adata);
        data.put("callinfo", callinfo);
        return Mono.just(Info.SUCCESS( data));
    }

    @ApiOperation(value = "数据分析", notes="数据分析")
    @GetMapping("/datacount")
    public Mono<Info> datacount(Principal principal, @RequestParam("type") Integer type, @RequestParam("shopcode")String shopcode, @RequestParam(value = "time",required = false)String time) {
        Map<String,Object> data =new HashMap<>();
        data.put("typecount", shopFansRepository.typeCount(shopcode,time));
        Map area = new HashMap();
        List areaname= new ArrayList();
        List areacount= new ArrayList();
        shopFansRepository.areaCountByV(shopcode).forEach(v->{
            areaname.add(v.getName());
            areacount.add(v.getY());
        });

        if (areaname.size()==0){
            areaname.add("苏州");
            areacount.add(0);
            areaname.add("深圳");
            areacount.add(0);
            areaname.add("东莞");
            areacount.add(0);
            areaname.add("广州");
            areacount.add(0);
            areaname.add("温州");
            areacount.add(0);
        }
        area.put("areaname", areaname);
        area.put("areacount", areacount);
        data.put("area",area);
        Map visitor = new HashMap();
        switch (type) {
            case 0:
                data.put("visitorcount",visitorService.count(time, shopcode));
                List atime = new ArrayList();
                List adata = new ArrayList();
                if (time.length() == 4) {
                    for (int i = 1; i <= 12; i++) {
                        String stime = time;
                        if (i < 10) {
                            stime = stime + "-0" + i;
                        } else {
                            stime = stime + "-" + i;
                        }
                        System.out.println(stime);
                        atime.add(stime.replace(time + "-", ""));
                        adata.add(visitorService.count(stime, shopcode));
                    }
                } else if (time.length() == 7) {
                    int count = 30;
                    if (time.contains("-01") || time.contains("-03") || time.contains("-05") || time.contains("-07") || time.contains("-08") || time.contains("-10") || time.contains("-12")) {
                        count = 31;
                    } else if (time.contains("-02")) {
                        count = 28;
                    }
                    for (int i = 1; i <= count; i++) {
                        String stime = time;
                        if (i < 10) {
                            stime = stime + "-0" + i;
                        } else {
                            stime = stime + "-" + i;
                        }
                        System.out.println(stime);
                        atime.add(stime.replace(time + "-", ""));
                        adata.add(visitorService.count(stime, shopcode));
                    }
                } else if (time.length() == 10) {
                    for (int i = 1; i <= 24; i++) {
                        String stime = time;
                        if (i < 10) {
                            stime = stime + " 0" + i;
                        } else {
                            stime = stime + " " + i;
                        }
                        System.out.println(stime);
                        adata.add(visitorService.count(stime, shopcode));
                        atime.add(stime.replace(time, ""));
                    }
                } else {
                    for (int i = 2; i >= 0; i--) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                        String dateStr = sdf.format(new Date());
                        atime.add(Integer.parseInt(dateStr) - i);
                        adata.add(visitorService.count(String.valueOf(Integer.parseInt(dateStr) - i), shopcode));

                    }
                }
                visitor.put("time", atime);
                visitor.put("data", adata);
                data.put("visitor", visitor);
                break;
            case 1:
                List brandtime = new ArrayList();
                List branddata = new ArrayList();
                data.put("visitorcount",visitorService.brandCount(time, shopcode));
                if (time.length() == 4) {
                    for (int i = 1; i <= 12; i++) {
                        String stime = time;
                        if (i < 10) {
                            stime = stime + "-0" + i;
                        } else {
                            stime = stime + "-" + i;
                        }
                        brandtime.add(stime.replace(time + "-", ""));
                        branddata.add(visitorService.brandCount(stime, shopcode));
                    }
                } else if (time.length() == 7) {
                    int count = 30;
                    if (time.contains("-01") || time.contains("-03") || time.contains("-05") || time.contains("-07") || time.contains("-08") || time.contains("-10") || time.contains("-12")) {
                        count = 31;
                    } else if (time.contains("-02")) {
                        count = 28;
                    }
                    for (int i = 1; i <= count; i++) {
                        String stime = time;
                        if (i < 10) {
                            stime = stime + "-0" + i;
                        } else {
                            stime = stime + "-" + i;
                        }
                        brandtime.add(stime.replace(time + "-", ""));
                        branddata.add(visitorService.brandCount(stime, shopcode));
                    }
                } else if (time.length() == 10) {
                    for (int i = 1; i <= 24; i++) {
                        String stime = time;
                        if (i < 10) {
                            stime = stime + " 0" + i;
                        } else {
                            stime = stime + " " + i;
                        }
                        branddata.add(visitorService.brandCount(stime, shopcode));
                        brandtime.add(stime.replace(time, ""));
                    }
                } else {
                    for (int i = 2; i >= 0; i--) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                        String dateStr = sdf.format(new Date());
                        brandtime.add(Integer.parseInt(dateStr) - i);
                        branddata.add(visitorService.brandCount(String.valueOf(Integer.parseInt(dateStr) - i), shopcode));

                    }
                }
                visitor.put("time", brandtime);
                visitor.put("data", branddata);
                break;
            case 2:
                data.put("visitorcount",shopFansRepository.newfollowmeCount(shopcode,time));
                List shopfanstime = new ArrayList();
                List shopfansdata = new ArrayList();
                if (time.length() == 4) {
                    for (int i = 1; i <= 12; i++) {
                        String stime = time;
                        if (i < 10) {
                            stime = stime + "-0" + i;
                        } else {
                            stime = stime + "-" + i;
                        }
                        shopfanstime.add(stime.replace(time + "-", ""));
                        shopfansdata.add(shopFansRepository.newfollowmeCount(shopcode,stime));
                    }
                } else if (time.length() == 7) {
                    int count = 30;
                    if (time.contains("-01") || time.contains("-03") || time.contains("-05") || time.contains("-07") || time.contains("-08") || time.contains("-10") || time.contains("-12")) {
                        count = 31;
                    } else if (time.contains("-02")) {
                        count = 28;
                    }
                    for (int i = 1; i <= count; i++) {
                        String stime = time;
                        if (i < 10) {
                            stime = stime + "-0" + i;
                        } else {
                            stime = stime + "-" + i;
                        }
                        shopfanstime.add(stime.replace(time + "-", ""));
                        shopfansdata.add(shopFansRepository.newfollowmeCount(shopcode,stime));
                    }
                } else if (time.length() == 10) {
                    for (int i = 1; i <= 24; i++) {
                        String stime = time;
                        if (i < 10) {
                            stime = stime + " 0" + i;
                        } else {
                            stime = stime + " " + i;
                        }
                        shopfansdata.add(shopFansRepository.newfollowmeCount(shopcode,stime));
                        shopfanstime.add(stime.replace(time, ""));
                    }
                } else {
                    for (int i = 2; i >= 0; i--) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                        String dateStr = sdf.format(new Date());
                        shopfanstime.add(Integer.parseInt(dateStr) - i);
                        shopfansdata.add(shopFansRepository.newfollowmeCount(shopcode,String.valueOf(Integer.parseInt(dateStr) - i)));

                    }
                }
                visitor.put("time", shopfanstime);
                visitor.put("data", shopfansdata);
                break;
            case 3:
                data.put("visitorcount",commentService.commentCount(time,shopcode));
                List commenttime = new ArrayList();
                List commentdata = new ArrayList();
                if (time.length() == 4) {
                    for (int i = 1; i <= 12; i++) {
                        String stime = time;
                        if (i < 10) {
                            stime = stime + "-0" + i;
                        } else {
                            stime = stime + "-" + i;
                        }
                        commenttime.add(stime.replace(time + "-", ""));
                        commentdata.add(commentService.commentCount(stime,shopcode));
                    }
                } else if (time.length() == 7) {
                    int count = 30;
                    if (time.contains("-01") || time.contains("-03") || time.contains("-05") || time.contains("-07") || time.contains("-08") || time.contains("-10") || time.contains("-12")) {
                        count = 31;
                    } else if (time.contains("-02")) {
                        count = 28;
                    }
                    for (int i = 1; i <= count; i++) {
                        String stime = time;
                        if (i < 10) {
                            stime = stime + "-0" + i;
                        } else {
                            stime = stime + "-" + i;
                        }
                        commenttime.add(stime.replace(time + "-", ""));
                        commentdata.add(commentService.commentCount(stime,shopcode));
                    }
                } else if (time.length() == 10) {
                    for (int i = 1; i <= 24; i++) {
                        String stime = time;
                        if (i < 10) {
                            stime = stime + " 0" + i;
                        } else {
                            stime = stime + " " + i;
                        }
                        commentdata.add(commentService.commentCount(stime,shopcode));
                        commenttime.add(stime.replace(time, ""));
                    }
                } else {
                    for (int i = 2; i >= 0; i--) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                        String dateStr = sdf.format(new Date());
                        commenttime.add(Integer.parseInt(dateStr) - i);
                        commentdata.add(commentService.commentCount(String.valueOf(Integer.parseInt(dateStr) - i),shopcode));

                    }
                }
                visitor.put("time", commenttime);
                visitor.put("data", commentdata);
                break;
        }
        data.put("visitor", visitor);
        return Mono.just(Info.SUCCESS(data ));
    }

    @ApiOperation(value = "联系我的", notes="联系我的")
    @GetMapping("/callmebytime")
    public Mono<Info> callmebytime(Principal principal,@RequestParam("page")Integer page, @RequestParam("shopcode")String shopcode, @RequestParam(value = "time",required = false)String time) {
        Map<String,Object> data =new HashMap<>();
        Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
        Page<CallCount> pages=  callCountRepository.callcountshop(pageable,shopcode,time);
        data.put("count", callCountRepository.callshopcount(shopcode,time));
        data.put("data", pages.getContent());
        return Mono.just(Info.SUCCESS(data ));
    }

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private BrandService brandService;

    @ApiOperation(value = "系统综合统计", notes="系统综合统计")
    @GetMapping("/sysstatistics")
    public Mono<Info> sysstatistics(Principal principal) {
        Map<String,Object> data =new HashMap<>();
        data.put("usercount",userSecurityService.countAll());
        data.put("shopcount",shopService.countAll());
        data.put("brandcount",brandService.countAll());
        data.put("rwcount",shopService.countAll());
        data.put("kccount",brandService.countAll());
        List fwl =new ArrayList();
        List newuser =new ArrayList();
        List date =new ArrayList();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for(int i =30;i>=0;i--){
            date.add(df.format(new Date().getTime()-i*24*60*60*1000));
            fwl.add(visitorService.countByTime(df.format(new Date().getTime()-i*24*60*60*1000)));
            newuser.add(userSecurityService.countByTime(df.format(new Date().getTime()-i*24*60*60*1000)));
        }
        data.put("newuser",newuser);
        data.put("date",date);
        data.put("fwl",fwl);
        return Mono.just(Info.SUCCESS( data));
    }


}
