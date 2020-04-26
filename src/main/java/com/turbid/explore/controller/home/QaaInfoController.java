package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Answer;
import com.turbid.explore.pojo.Case;
import com.turbid.explore.pojo.QaaInfo;
import com.turbid.explore.pojo.UserSecurity;
import com.turbid.explore.service.AnswerService;
import com.turbid.explore.service.CommentService;
import com.turbid.explore.service.QaaInfoService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(description = "问答接口")
@RestController
@RequestMapping("/qaa")
@CrossOrigin
public class QaaInfoController {


    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private QaaInfoService qaaInfoService;

    @ApiOperation(value = "发布问题", notes="发布问题")
    @PutMapping("/addqaa")
    public Mono<Info> addqaa(Principal principal, @RequestBody QaaInfo qaaInfo) {
        try {
            qaaInfo.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
            return Mono.just(Info.SUCCESS( qaaInfoService.save(qaaInfo)));
        }catch (Exception e){
            return Mono.just(Info.ERROR( e.getMessage()));
        }

    }

    @ApiOperation(value = "查看问答列表", notes="查看问答列表")
    @GetMapping("/qaas")
    public Mono<Info> qaas(@RequestParam("page")Integer page,@RequestParam(name = "label", required = false)String label) {
        return Mono.just(Info.SUCCESS( qaaInfoService.listByPage(page,label)));
    }

    @ApiOperation(value = "查看问答", notes="查看问答")
    @GetMapping("/qaaByCode")
    public Mono<Info> qaaByCode(Principal principal,@RequestParam("code")String code) {
        Map<String,Object> data=new HashMap<>();
        UserSecurity userSecurity= userSecurityService.findByPhone(principal.getName());
        QaaInfo qaaInfo= qaaInfoService.qaaByCode(code);
        data.put("isstar",qaaInfo.getStars().contains(userSecurity));
        data.put("data",qaaInfo);
        return Mono.just(Info.SUCCESS(data ));
    }

    @Autowired
    private AnswerService answerService;

    @ApiOperation(value = "回答答案", notes="回答答案")
    @PutMapping("/answer")
    public Mono<Info> answer(Principal principal,@RequestBody Answer answer) {
        answer.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        QaaInfo qaaInfo= qaaInfoService.qaaByCode(answer.getQaacode());
        qaaInfo.getAnswers().add(answerService.save(answer));
        qaaInfoService.save(qaaInfo);
        return Mono.just(Info.SUCCESS(answer));
    }

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "查看答案", notes="查看答案")
    @GetMapping("/answersByQaacode")
    public Mono<Info> answersByQaacode(Principal principal,@RequestParam("code")String code,@RequestParam("page")Integer page) {
        Map<String,Object> data=new HashMap<>();
        UserSecurity userSecurity= userSecurityService.findByPhone(principal.getName());
        List<Answer> answerList=answerService.answersByQaacode(code,page);
        answerList.forEach(v->{
            v.setCommentcount(commentService.listByCount(v.getCode()));
            v.setIsstar(v.getStars().contains(userSecurity));
        });
        data.put("data",answerList);
        data.put("count",qaaInfoService.qaaByCode(code).getAnswersinfo().size());
        return Mono.just(Info.SUCCESS( data));
    }

    @ApiOperation(value = "点赞", notes="问题点赞:type=0  答案点赞type=1 ")
    @PostMapping(value = "/star")
    public Mono<Info> star(Principal principal,@RequestParam(name = "code")String code,@RequestParam("type")Integer type) {
        UserSecurity userSecurity= userSecurityService.findByPhone(principal.getName());
        try {
            switch (type){
                case 0:
                    QaaInfo qaaInfo=qaaInfoService.qaaByCode(code);
                    qaaInfo.getStars().add(userSecurity);
                    qaaInfoService.save(qaaInfo);
                    break;
                case 1:
                    Answer answer=answerService.get(code);
                    answer.getStars().add(userSecurity);
                    answerService.save(answer);
                    break;
            }
            return Mono.just(Info.SUCCESS(""));
        }catch (Exception e){
            return Mono.just(Info.ERROR(e.getMessage()));
        }
    }

}
