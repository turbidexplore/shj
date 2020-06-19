package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.CallCount;
import com.turbid.explore.pojo.Shop;
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
                adata.add(callCountRepository.callshopcount(stime, shopcode));
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
                adata.add(callCountRepository.callshopcount(stime, shopcode));
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
                adata.add(callCountRepository.callshopcount(stime, shopcode));
                atime.add(stime.replace(time,""));
            }
        }else {
            for (int i = 2; i >=0; i--) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                String dateStr = sdf.format(new Date());
                atime.add(Integer.parseInt(dateStr)-i);
                adata.add( callCountRepository.callshopcount(String.valueOf(Integer.parseInt(dateStr)-i), shopcode));

            }
        }
        callinfo.put("time",atime);
        callinfo.put("data",adata);
        data.put("callinfo", callinfo);
        return Mono.just(Info.SUCCESS( data));
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
