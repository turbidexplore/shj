package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.*;
import com.turbid.explore.repository.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "首页接口")
@RestController
@RequestMapping("/home")
@CrossOrigin
public class HomeController {

    @Autowired
    private ProductReposity productReposity;

    @Autowired
    private CaseRepositroy caseRepositroy;

    @Autowired
    private NativeContentRepositroy nativeContentRepositroy;

    @Autowired
    private ShopRepositroy shopRepositroy;

    @Autowired
    private StudyGroupRepository studyGroupRepository;


    @ApiOperation(value = "获取首页信息", notes="获取首页信息")
    @GetMapping("/info")
    public Mono<Info> info(Principal principal,@RequestParam("type")Integer type) {
        Map data=new HashMap<>();
        Pageable pageable = new PageRequest(0,3, Sort.Direction.DESC,"create_time");

        switch (type){
            case 1:
                data.put("products",productReposity.productsByPagea(pageable).getContent());
                break;
            case 2:
                data.put("products",productReposity.productsByPageb(pageable).getContent());
                break;
            case 3:
                data.put("products",productReposity.productsByPagec(pageable).getContent());
                break;
            default:
                data.put("products",productReposity.productsByPage(pageable,type).getContent());
                break;
        }

        Page<Shop> shops=  shopRepositroy.getshops(pageable,"观韵","美萃","博领");
        List shop =new ArrayList<>();
        shops.getContent().forEach(a->{
            Map i=new HashMap();
            i.put("data",a);
            Pageable aaa = new PageRequest(0,4, Sort.Direction.DESC,"create_time");
            i.put("img",nativeContentRepositroy.wuyu(aaa,a.getCode()).getContent());
            shop.add(i);
        });
        data.put("shops",shop);

        Pageable pageablea= new PageRequest(0,2, Sort.Direction.DESC,"_cj");
        Page<Case> cases=  caseRepositroy.casebylabel(pageablea,null);
        data.put("cases",cases.getContent());

        Page<NativeContent> nativeContents=  nativeContentRepositroy.listByPageLabel(pageable,null,null);
        data.put("nativeContents",nativeContents.getContent());

        Page<StudyGroup> studyGroups=  studyGroupRepository.grouplist(pageable,null);
        data.put("studyGroups",studyGroups.getContent());

        return Mono.just(Info.SUCCESS(data));
    }

}
