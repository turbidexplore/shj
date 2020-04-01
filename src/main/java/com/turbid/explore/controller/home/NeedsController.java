package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Needs;
import com.turbid.explore.service.NeedsService;
import com.turbid.explore.utils.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Api(description = "需求操作接口")
@RestController
@RequestMapping("/needs")
@CrossOrigin
public class NeedsController {

    @Autowired
    private NeedsService needsService;

    @ApiOperation(value = "新需求添加", notes="新需求添加")
    @PostMapping(value = "/add")
    public Mono<Info> add(@RequestBody Needs needs) {
        needsService.save(needs);
        return Mono.just(Info.SUCCESS(null));
    }

    @ApiOperation(value = "获取需求分页列表", notes="获取需求分页列表")
    @PostMapping(value = "/listByPage")
    public Mono<Info> listByPage(@RequestParam(name = "page")Integer page) {
        try {
            return Mono.just(Info.SUCCESS(needsService.listByPage(page)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

}
