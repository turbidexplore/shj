package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Shop;
import com.turbid.explore.repository.ShopFansRepository;
import com.turbid.explore.service.*;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    private CallService callService;

    @ApiOperation(value = "店铺数据统计", notes="店铺数据统计")
    @GetMapping("/statistics")
    public Mono<Info> Statistics(Principal principal, @RequestParam("shopcode")String shopcode, @RequestParam(value = "time",required = false)String time) {
        Map<String,Object> data =new HashMap<>();
        data.put("callmecount", callService.callshopcount(shopcode,time));
        data.put("callme", callService.callshop(shopcode,time));
        data.put("brandvisitor",visitorService.brandCount(time, shopcode));
        data.put("commentcount",commentService.commentCount(time, shopcode));
        data.put("goodsvisitor",visitorService.goodsCount(time, shopcode));
        data.put("goodslike",collectionService.goodslikes(time, shopcode));
        data.put("businessvisitor",visitorService.count(time, shopcode+"zsjm"));
        data.put("shopvisitor",visitorService.count(time, shopcode));
        data.put("newfans",shopFansRepository.newfollowmeCount(shopcode,time));

        List fwl =new ArrayList();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for(int i =6;i>=0;i--){
            fwl.add(new Object[]{df.format(new Date().getTime()-i*24*60*60*1000),visitorService.count(df.format(new Date().getTime()-i*24*60*60*1000),shopcode)});
        }
        data.put("fwl",fwl);

        return Mono.just(Info.SUCCESS( data));
    }


}
