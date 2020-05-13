package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Feedback;
import com.turbid.explore.pojo.UserSecurity;
import com.turbid.explore.repository.FeedbackRepository;
import com.turbid.explore.service.*;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Autowired
    private QaaInfoService qaaInfoService;

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
            data.put("casecount",caseService.casecount(principal.getName()));
        return Mono.just(Info.SUCCESS(data));
    }

    @ApiOperation(value = "数据统计", notes="数据统计")
    @PostMapping(value = "/countbyuser")
    public Mono<Info> countbyuser(@RequestParam("usercode")String usercode)  {
        Map<String,Object> data=new HashMap<>();
        data.put("shb",0);
        data.put("follow",followService.myfollowCount(usercode));
        data.put("fans",followService.followmeCount(usercode));
        data.put("star",caseService.starcount(usercode));
        data.put("casecount",caseService.casecount(usercode));
        return Mono.just(Info.SUCCESS(data));
    }

    @ApiOperation(value = "我的问答", notes="我的问答")
    @PostMapping(value = "/myqaa")
    public Mono<Info> myqaa(Principal principal,@RequestParam("page")Integer page)  {
        return Mono.just(Info.SUCCESS(qaaInfoService.listByUser(principal.getName(),page)));
    }

    @Autowired
    private AnswerService answerService;

    @ApiOperation(value = "我的回答", notes="我的回答")
    @PostMapping(value = "/myanswer")
    public Mono<Info> myanswer(Principal principal,@RequestParam("page")Integer page)  {

        return Mono.just(Info.SUCCESS(answerService.listByUser(principal.getName(),page)));
    }

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserSecurityService userSecurityService;

    @ApiOperation(value = "意见反馈", notes="意见反馈")
    @PostMapping(value = "/feedback")
    public Mono<Info> feedback(Principal principal, @RequestBody Feedback feedback)  {
        feedback.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        return Mono.just(Info.SUCCESS(feedbackRepository.saveAndFlush(feedback)));
    }





}
