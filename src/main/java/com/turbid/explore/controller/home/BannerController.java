package com.turbid.explore.controller.home;

import com.turbid.explore.configuration.AsyncTaskA;
import com.turbid.explore.pojo.Banner;
import com.turbid.explore.pojo.FileInfo;
import com.turbid.explore.repository.AdRepository;
import com.turbid.explore.repository.FileInfoRepositroy;
import com.turbid.explore.service.BannerService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.tools.Info;
import com.turbid.explore.tools.TLSSigAPIv2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.attoparser.util.TextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.*;

@Api(description = "banner接口")
@RestController
@RequestMapping("/banner")
@CrossOrigin
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @Autowired
    private FileInfoRepositroy fileInfoRepositroy;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private RestTemplate restTemplate;

    private String baseUrl="https://console.tim.qq.com/";;
    private String portrait_set="v4/profile/portrait_set";
    private long appid=1400334582;

    public String config(){
        return "?sdkappid="+appid+"&identifier=administrator"+"&usersig="+ TLSSigAPIv2.genSig("administrator",680000000)
                +"&random="+ UUID.randomUUID().toString().replace("-", "").toLowerCase()+"&contenttype=json";
    }

    @Autowired
    private AsyncTaskA asyncTask;

    @ApiOperation(value = "Banner列表", notes="Banner列表[0首页 1需求 2品牌馆 3特加仓 4达人研习社 5官方严选 6]")
    @GetMapping(value = "/list")
    public Mono<Info> list(@RequestParam("type")String type)  {

        return Mono.just(Info.SUCCESS(bannerService.listBytype(type)));
    }

    @Autowired
    private AdRepository adRepository;

    @ApiOperation(value = "广告")
    @GetMapping(value = "/ad")
    public Mono<Info> ad()  {

        return Mono.just(Info.SUCCESS(adRepository.findAll()));
    }

    @PutMapping("/add")
    public Mono<Info> add(Principal principal, @RequestParam("url")String url,@RequestParam("type")String type) {
        Banner banner=new Banner();
        banner.setType(type);
        banner.setUrl(url);
        banner.setImgs(url);
        FileInfo fileInfo=new FileInfo();
        fileInfo.setType("banner图片");
        fileInfo.setUrl(url);
        fileInfoRepositroy.saveAndFlush(fileInfo);
        banner.setFileInfo(fileInfo);
        return Mono.just(Info.SUCCESS( bannerService.save(banner)));
    }

    @PutMapping("/del")
    public Mono<Info> del(Principal principal, @RequestParam("code")String code) {
        bannerService.del(code);
        return Mono.just(Info.SUCCESS( null));
    }
}
