package com.turbid.explore.controller.home;

import com.turbid.explore.service.CaseService;
import com.turbid.explore.service.FollowService;
import com.turbid.explore.service.ProjectNeedsService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Api(description = "个人中心")
@RestController
@RequestMapping("/usercenter")
@CrossOrigin
public class UserCenterController {

    @Autowired
    private FollowService followService;

    @Autowired
    private CaseService caseService;

    @Autowired
    private ProjectNeedsService projectNeedsService;

    @ApiOperation(value = "数据统计", notes="数据统计")
    @PostMapping(value = "/count")
    public Mono<Info> count(Principal principal)  {
        Map<String,Object> data=new HashMap<>();
            data.put("shb",0);
            data.put("follow",followService.myfollowCount(principal.getName()));
            data.put("fans",followService.followmeCount(principal.getName()));
            data.put("star",caseService.starcount(principal.getName()));
            data.put("needing",projectNeedsService.countByStatus(principal.getName(),0));
            data.put("needed",projectNeedsService.countByStatus(principal.getName(),1));
            data.put("myneed",caseService.starcount(principal.getName()));
            data.put("needme",caseService.starcount(principal.getName()));
        return Mono.just(Info.SUCCESS(data));
    }

}
