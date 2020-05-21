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

    @ApiOperation(value = "Banner列表", notes="Banner列表")
    @GetMapping(value = "/list")
    public Mono<Info> list(@RequestParam("type")Integer type) {
        switch (type){
            case 0:
                List<Banner> bannerList =new ArrayList<>();
                Banner banner = new Banner();
                banner.setCreate_time(new Date());
                FileInfo fileInfo=new FileInfo();
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
