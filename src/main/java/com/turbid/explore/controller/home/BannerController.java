package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Banner;
import com.turbid.explore.pojo.FileInfo;
import com.turbid.explore.utils.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Api(description = "banner支付接口")
@RestController
@RequestMapping("/banner")
@CrossOrigin
public class BannerController {

    @ApiOperation(value = "Banner列表", notes="Banner列表")
    @GetMapping(value = "/list")
    public Mono<Info> list() {
        List<Banner> bannerList =new ArrayList<>();
        Banner banner = new Banner();
        FileInfo fileInfo=new FileInfo();
        fileInfo.setType("banner图片");
        fileInfo.setUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585200045047&di=d95aad5e2c1441a6159e49e29dd018a3&imgtype=0&src=http%3A%2F%2Fbpic.588ku.com%2Fback_pic%2F17%2F04%2F19%2Fa6dba3676baac65c938bb318a574296e.jpg");
        banner.setFileInfo(fileInfo);
        bannerList.add(banner);
        bannerList.add(banner);
        bannerList.add(banner);
        bannerList.add(banner);
        return Mono.just(Info.SUCCESS(bannerList));
    }
}
