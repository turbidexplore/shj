package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Case;
import com.turbid.explore.pojo.Study;
import com.turbid.explore.pojo.StudyRelation;
import com.turbid.explore.repository.StudyRelationRepository;
import com.turbid.explore.service.StudyService;
import com.turbid.explore.tools.CodeLib;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Api(description = "达人研习社")
@RestController
@RequestMapping("/study")
@CrossOrigin
public class StudyController {

    @Autowired
    private StudyService studyService;

    @ApiOperation(value = "添加课程", notes="添加课程")
    @PutMapping("/add")
    public Mono<Info> add(@RequestBody Study study) {

        return Mono.just(Info.SUCCESS(studyService.save(study)));
    }


    @ApiOperation(value = "获取课程详情", notes="获取课程详情")
    @GetMapping("/get")
    public Mono<Info> get(@RequestParam(name = "code")String code) {

        return Mono.just(Info.SUCCESS(studyService.get(code)));
    }

    @ApiOperation(value = "获取课程分页列表", notes="获取课程分页列表")
    @PostMapping(value = "/studyByPage")
    public Mono<Info> studyByPage(@RequestParam(name = "page")Integer page,
                                  @RequestParam(name = "style", required = false)String style) {
        try {
            return Mono.just(Info.SUCCESS(studyService.listByPage(page,style)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @ApiOperation(value = "热门课程", notes="热门课程")
    @PostMapping(value = "/hatstudyByPage")
    public Mono<Info> hatstudyByPage(@RequestParam(name = "page")Integer page) {
        try {
            return Mono.just(Info.SUCCESS(studyService.hatstudyByPage(page)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @Autowired
    private StudyRelationRepository studyRelationRepository;

    @ApiOperation(value = "购买课程", notes="购买课程")
    @PostMapping(value = "/buyStudy")
    public Mono<Info> buyStudy(Principal principal,@RequestParam("studycode")String studycode) {
        try {
            StudyRelation studyRelation=new StudyRelation();
            studyRelation.setOrderno(CodeLib.randomCode(12,1));
            studyRelation.setPhone(principal.getName());
            studyRelation.setStudycode(studycode);
            studyRelation.setStatus(0);
            return Mono.just(Info.SUCCESS(studyRelationRepository.saveAndFlush(studyRelation)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }


    @ApiOperation(value = "是否可看", notes="是否可看")
    @PostMapping(value = "/issee")
    public Mono<Info> issee(Principal principal,@RequestParam("studycode")String studycode) {
        try {
            if(0<studyRelationRepository.issee(principal.getName(),studycode)){
                return Mono.just(Info.SUCCESS(true));
            }else {
                return Mono.just(Info.SUCCESS(false));
            }
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

}
