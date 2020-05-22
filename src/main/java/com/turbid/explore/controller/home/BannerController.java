package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Banner;
import com.turbid.explore.pojo.FileInfo;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(description = "banner接口")
@RestController
@RequestMapping("/banner")
@CrossOrigin
public class BannerController {

    @ApiOperation(value = "Banner列表", notes="Banner列表[0首页 1需求 2品牌馆 3特加仓 4达人研习社 5官方严选]")
    @GetMapping(value = "/list")
    public Mono<Info> list(@RequestParam("type")Integer type) {
        List<Banner> bannerList =new ArrayList<>();
        Banner banner = new Banner();
        FileInfo fileInfo=new FileInfo();
        switch (type){
            case 0:
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E7%BC%96%E7%BB%84%2017.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                bannerList.add(banner);
                bannerList.add(banner);
                bannerList.add(banner);
                return Mono.just(Info.SUCCESS(bannerList));
            case 1:
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E7%BC%96%E7%BB%84%2017.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                bannerList.add(banner);
                bannerList.add(banner);
                bannerList.add(banner);
                return Mono.just(Info.SUCCESS(bannerList));
            case 2:
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E7%BC%96%E7%BB%84%2017.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                bannerList.add(banner);
                bannerList.add(banner);
                bannerList.add(banner);
                return Mono.just(Info.SUCCESS(bannerList));
            case 3:
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E7%BC%96%E7%BB%84%2017.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                bannerList.add(banner);
                bannerList.add(banner);
                bannerList.add(banner);
                return Mono.just(Info.SUCCESS(bannerList));
            case 4:
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E7%BC%96%E7%BB%84%2017.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                bannerList.add(banner);
                bannerList.add(banner);
                bannerList.add(banner);
                return Mono.just(Info.SUCCESS(bannerList));
            case 5:
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E7%BC%96%E7%BB%84%2017.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                bannerList.add(banner);
                bannerList.add(banner);
                bannerList.add(banner);
                return Mono.just(Info.SUCCESS(bannerList));
            default:
                return Mono.just(Info.SUCCESS(null));
        }

    }
}
