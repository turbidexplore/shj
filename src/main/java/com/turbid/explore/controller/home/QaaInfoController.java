package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.*;
import com.turbid.explore.repository.NoticeRepository;
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

    @Autowired
    private NoticeRepository noticeRepository;

    @ApiOperation(value = "回答答案", notes="回答答案")
    @PutMapping("/answer")
    public Mono<Info> answer(Principal principal,@RequestBody Answer answer) {
        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        answer.setUserSecurity(userSecurity);
        QaaInfo qaaInfo= qaaInfoService.qaaByCode(answer.getQaacode());
        qaaInfo.getAnswersinfo().add(answerService.save(answer));
        qaaInfoService.save(qaaInfo);
        noticeRepository.save(new Notice(qaaInfo.getUserSecurity().getPhonenumber(), "用户【 "+userSecurity.getUserBasic().getNikename()+" 】 回答了您提的问题，请去个人中心查看。", "回答通知", 1, 0));
        if(answerService.countByUser(principal.getName())<=5) {
           if (null == userSecurity.getShb()) {
               userSecurity.setShb(1);
           } else {
               userSecurity.setShb(userSecurity.getShb() + 1);
           }
           userSecurityService.save(userSecurity);
       }
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
        data.put("count",answerService.answersCount(code));
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

    @ApiOperation(value = "删除问题", notes="删除问题")
    @PutMapping("/deleteqaa")
    public Mono<Info> deleteqaa(Principal principal,@RequestParam("qaacode")String qaacode) {
        QaaInfo qaaInfo= qaaInfoService.qaaByCode(qaacode);
        qaaInfo.setIsdel(true);
        qaaInfoService.save(qaaInfo);
        return Mono.just(Info.SUCCESS(null));
    }

    @ApiOperation(value = "删除答案", notes="删除答案")
    @PutMapping("/deleteanswer")
    public Mono<Info> answer(Principal principal,@RequestParam("answercode")String answercode) {
        Answer answer= answerService.get(answercode);
        answer.setIsdel(true);
        answerService.save(answer);
        return Mono.just(Info.SUCCESS(null));
    }

}
