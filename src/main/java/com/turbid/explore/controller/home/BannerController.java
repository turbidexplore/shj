package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Banner;
import com.turbid.explore.pojo.Brand;
import com.turbid.explore.pojo.FileInfo;
import com.turbid.explore.repository.FileInfoRepositroy;
import com.turbid.explore.service.BannerService;
import com.turbid.explore.service.FileService;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
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
                banner = new Banner();
                banner.setType("0");
                banner.setCode("ff808081725046550172504f83060023");
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E9%A6%96%E9%A1%B5/%E9%A6%96%E9%A1%B5%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                banner = new Banner();
                banner.setType("0");
                banner.setCode("ff808081725046550172504f83060023");
                bannerList.add(banner);
                fileInfo=new FileInfo();
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E9%A6%96%E9%A1%B5/%E9%A6%96%E9%A1%B5%E8%BD%AE%E6%92%AD%E4%BA%8C.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                banner = new Banner();
                banner.setType("0");
                banner.setCode("ff808081725046550172504f83060023");
                fileInfo=new FileInfo();
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E9%A6%96%E9%A1%B5/%E9%A6%96%E9%A1%B5%E8%BD%AE%E6%92%AD%E4%BA%8C.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                banner = new Banner();
                banner.setType("0");
                banner.setCode("ff808081725046550172504f83060023");
                fileInfo=new FileInfo();
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E9%A6%96%E9%A1%B5/%E9%A6%96%E9%A1%B5%E8%BD%AE%E6%92%AD%E4%BA%8C.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                return Mono.just(Info.SUCCESS(bannerList));
            case 1:
                banner = new Banner();
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E9%9C%80%E6%B1%82/%E4%BE%9B%E6%B1%82%E5%95%86%E8%AF%A2%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                banner = new Banner();
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E9%9C%80%E6%B1%82/%E4%BE%9B%E6%B1%82%E5%95%86%E8%AF%A2%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                banner = new Banner();
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E9%9C%80%E6%B1%82/%E4%BE%9B%E6%B1%82%E5%95%86%E8%AF%A2%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                banner = new Banner();
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E9%9C%80%E6%B1%82/%E4%BE%9B%E6%B1%82%E5%95%86%E8%AF%A2%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                return Mono.just(Info.SUCCESS(bannerList));
            case 2:
                banner = new Banner();
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E5%93%81%E7%89%8C%E9%A6%86%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                banner = new Banner();
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E5%93%81%E7%89%8C%E9%A6%86%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                banner = new Banner();
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E5%93%81%E7%89%8C%E9%A6%86%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                banner = new Banner();
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%93%81%E7%89%8C%E9%A6%86/%E5%93%81%E7%89%8C%E9%A6%86%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                return Mono.just(Info.SUCCESS(bannerList));
            case 3:
                banner = new Banner();
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E7%89%B9%E5%8A%A0%E4%BB%93/%E7%89%B9%E4%BB%B7%E4%BB%93%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                banner = new Banner();
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E7%89%B9%E5%8A%A0%E4%BB%93/%E7%89%B9%E4%BB%B7%E4%BB%93%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                banner = new Banner();
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E7%89%B9%E5%8A%A0%E4%BB%93/%E7%89%B9%E4%BB%B7%E4%BB%93%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                banner = new Banner();
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E7%89%B9%E5%8A%A0%E4%BB%93/%E7%89%B9%E4%BB%B7%E4%BB%93%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                return Mono.just(Info.SUCCESS(bannerList));
            case 4:
                banner = new Banner();
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE/%E8%BD%AE%E6%92%AD%E5%9B%BE/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                banner = new Banner();
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE/%E8%BD%AE%E6%92%AD%E5%9B%BE/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                banner = new Banner();
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE/%E8%BD%AE%E6%92%AD%E5%9B%BE/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                banner = new Banner();
                fileInfo=new FileInfo();
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE/%E8%BD%AE%E6%92%AD%E5%9B%BE/%E8%BE%BE%E4%BA%BA%E7%A0%94%E4%B9%A0%E7%A4%BE%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                return Mono.just(Info.SUCCESS(bannerList));
            case 5:
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
                fileInfo.setType("banner图片");
                fileInfo.setUrl("https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/banner/%E5%AE%98%E6%96%B9%E4%B8%A5%E9%80%89/%E5%AE%98%E6%96%B9%E4%B8%A5%E9%80%89%E8%BD%AE%E6%92%AD%E4%B8%80.png");
                banner.setFileInfo(fileInfo);
                bannerList.add(banner);
                bannerList.add(banner);
                bannerList.add(banner);
                bannerList.add(banner);
                return Mono.just(Info.SUCCESS(bannerList));
            case 6:
                banner.setCreate_time(new Date());
                fileInfo.setCreate_time(new Date());
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

    @Autowired
    private BannerService bannerService;

    @Autowired
    private FileInfoRepositroy fileInfoRepositroy;

    @PutMapping("/add")
    public Mono<Info> add(Principal principal, @RequestBody Banner banner) {
        FileInfo fileInfo=new FileInfo();
        fileInfo.setType("banner图片");
        fileInfo.setUrl(banner.getImgs());
        fileInfoRepositroy.saveAndFlush(fileInfo);
        banner.setFileInfo(fileInfo);
        return Mono.just(Info.SUCCESS( bannerService.save(banner)));
    }
}
