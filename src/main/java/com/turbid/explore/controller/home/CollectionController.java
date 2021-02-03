package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.*;
import com.turbid.explore.pojo.bo.CollectionType;
import com.turbid.explore.repository.NativeContentRepositroy;
import com.turbid.explore.repository.ProductReposity;
import com.turbid.explore.repository.StudyGroupRepository;
import com.turbid.explore.service.*;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Api(description = "收藏接口")
@RestController
@RequestMapping("/collection")
@CrossOrigin
public class CollectionController {

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private CollectionService collectionService;

    @ApiOperation(value = "收藏", notes="收藏")
    @PutMapping("/add")
    public Mono<Info> add(Principal principal, @RequestBody Collection collection) {
        collection.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        return Mono.just(Info.SUCCESS( collectionService.save(collection)));
    }

    @ApiOperation(value = "是否收藏", notes="是否收藏")
    @PutMapping("/iscollection")
    public Mono<Info> iscollection(Principal principal, @RequestParam("relation") String relation) {
      if(0<collectionService.findByCount(principal.getName(),relation)){
            return Mono.just(Info.SUCCESS(true));
        }else{
          return Mono.just(Info.SUCCESS(false));
        }
    }

    @ApiOperation(value = "取消收藏", notes="取消收藏")
    @PutMapping("/cancelcollection")
    public Mono<Info> cancelcollection(Principal principal, @RequestParam("relation") String relation) {

            return Mono.just(Info.SUCCESS(collectionService.cancelcollection(principal.getName(),relation)));
    }

    @ApiOperation(value = "查看收藏", notes="查看收藏")
    @PostMapping("/lists")
    public Mono<Info> lists(@RequestParam("relation") String relation) {
        return Mono.just(Info.SUCCESS( collectionService.listByPage(relation)));
    }

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private NativeContentRepositroy nativeContentRepositroy;


    @Autowired
    private CaseService caseService;

    @Autowired
    private StudyService studyService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ProductReposity productReposity;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @ApiOperation(value = "我的收藏", notes="我的收藏")
    @PostMapping("/my")
    public Mono<Info> my(Principal principal, @RequestParam("page")Integer page, @RequestParam("type")CollectionType collectionType) {
        List<Object> data=new ArrayList<>();
        collectionService.listByPagePhone(principal.getName(),page,collectionType).forEach(v->{
                switch (collectionType){
                    case product:
                        try {
                        Product product=productReposity.getOne(v.getRelation());
                        if(null!=product.getCode()){
                            data.add(product);
                        }
                        }catch (Exception e){

                        }
                        break;
                    case shop:
                        try {
                      Shop shop= shopService.getByCode(v.getRelation());
                        if(null!=shop.getCode()){
                            data.add(shop);
                        }
                        }catch (Exception e){

                        }
                        break;
                    case nativecontent:
                        try {
                       NativeContent nativeContent= nativeContentRepositroy.getOne(v.getRelation());
                        if(null!=nativeContent.getCode()){
                            data.add(nativeContent);
                        }
                        }catch (Exception e){

                        }
                        break;
                    case books:
                        try {
                        Study study=studyService.get(v.getRelation());
                        if(null!=study.getCode()){
                            data.add(study);
                        }
                        }catch (Exception e){

                        }
                        break;
                    case caseinfo:
                        try {
                        Case caseinfo=caseService.caseByCode(v.getRelation());
                        if(null!=caseinfo.getCode()){
                            data.add(caseinfo);
                        }
                        }catch (Exception e){

                        }
                        break;
                    case studygroup:
                        try {
                            StudyGroup study=studyGroupRepository.getOne(v.getRelation());
                            if(null!=study.getCode()){
                                data.add(study);
                            }
                        }catch (Exception e){

                        }
                        break;
                }
        });
        return Mono.just(Info.SUCCESS(data ));
    }

}
