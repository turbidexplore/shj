package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Case;
import com.turbid.explore.pojo.Study;
import com.turbid.explore.service.StudyService;
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

}
