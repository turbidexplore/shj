package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.DayTask;
import com.turbid.explore.pojo.Feedback;
import com.turbid.explore.pojo.UserSecurity;
import com.turbid.explore.repository.*;
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
import java.util.*;

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

    @Autowired
    private CallService callService;

    @Autowired
    private ShopFansRepository shopFansRepository;

    @Autowired
    private CommunityReposity communityReposity;

    @Autowired
    private ProductReposity productReposity;

    @ApiOperation(value = "数据统计", notes="数据统计")
    @PostMapping(value = "/count")
    public Mono<Info> count(Principal principal)  {
        Map<String,Object> data=new HashMap<>();
       UserSecurity userSecurity= userSecurityService.findByPhone(principal.getName());
            data.put("balance",userSecurity.getBalance());
            data.put("shb",userSecurity.getShb());
            data.put("follow",followService.myfollowCount(principal.getName()));
            data.put("fans",followService.followmeCount(principal.getName()));
            data.put("star",caseService.starcount(principal.getName()));
            data.put("needing",0);
            data.put("needed",0);
            data.put("myneed",1);
            data.put("needme",1);
            data.put("casecount",caseService.casecount(principal.getName()));
            data.put("communitycount",productReposity.countbyuser(userSecurity.getCode())+communityReposity.countbyuser(userSecurity.getCode()));
        return Mono.just(Info.SUCCESS(data));
    }

    @ApiOperation(value = "数据统计", notes="数据统计")
    @PostMapping(value = "/countbyuser")
    public Mono<Info> countbyuser(Principal principal,@RequestParam("usercode")String usercode)  {
        Map<String,Object> data=new HashMap<>();
        UserSecurity userSecurity= userSecurityService.findByPhone(principal.getName());
        data.put("balance",userSecurity.getBalance());
        data.put("shb",userSecurity.getShb());
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
        List<Map<String,Object>> data=new ArrayList<>();
        answerService.listByUser(principal.getName(),page).forEach(v->{
            Map<String,Object> item=new HashMap<>();
            item.put("qaa",qaaInfoService.qaaByCode(v.getQaacode()));
            item.put("answer",v);
            data.add(item);
        });
        return Mono.just(Info.SUCCESS(data));
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


    @ApiOperation(value = "签到", notes="签到")
    @PostMapping(value = "/signin")
    public Mono<Info> signin(Principal principal)  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        if(userSecurityService.issignin(principal.getName(),dateStr)==0) {
            UserSecurity userSecurity = userSecurityService.findByPhone(principal.getName());

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(new Date());
            calendar.add(calendar.DATE, -1); //把日期往后增加一天,整数  往后推,负数往前移动
            if (null == userSecurity.getQdcount()) {
                userSecurity.setQdcount(0);
            }
            if (null == userSecurity.getQdtime()) {
                userSecurity.setQdtime("");
            }
            if (userSecurity.getQdcount() < 7 && (userSecurity.getQdtime().equals(sdf.format(calendar.getTime())) || userSecurity.getQdtime() == sdf.format(calendar.getTime()))) {
                userSecurity.setQdcount(userSecurity.getQdcount() + 1);
            } else {
                userSecurity.setQdcount(1);
            }

            userSecurity.setQdtime(dateStr);
            if (null == userSecurity.getShb()) {
                userSecurity.setShb(10);
                userSecurity.setSignintime(dateStr);
            } else if (userSecurity.getQdcount() == 7) {
                userSecurity.setShb(userSecurity.getShb() + 30);
                userSecurity.setSignintime(dateStr);
            } else {
                userSecurity.setShb(userSecurity.getShb() + 10);
                userSecurity.setSignintime(dateStr);
            }
            userSecurity = userSecurityService.save(userSecurity);
            return Mono.just(Info.SUCCESS(userSecurity.getQdcount()));
        }else {
            return Mono.just(Info.ERROR("已经签到"));
        }
    }

    @ApiOperation(value = "是否签到", notes="是否签到")
    @PostMapping(value = "/issignin")
    public Mono<Info> issignin(Principal principal)  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        return Mono.just(Info.SUCCESS(userSecurityService.issignin(principal.getName(),dateStr)));
    }

    @Autowired
    private DayTaskReposity dayTaskReposity;


    @ApiOperation(value = "每日签到信息", notes="每日签到信息")
    @PostMapping(value = "/daytask")
    public Mono<Info> daytask(Principal principal)  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        DayTask dayTask=dayTaskReposity.findByDay(principal.getName(),dateStr);
        if(null==dayTask){
            dayTask=new DayTask();
        }
        dayTask.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        dayTask=dayTaskReposity.saveAndFlush(dayTask);
        Map data=new HashMap();
        data.put("info",dayTask);
        Map base=new HashMap();
            Map a=new HashMap();
            a.put("count",1);
            a.put("shb",10);
            a.put("ok",dayTask.getTaska()>=1?true:false);
            base.put("a",a);
            Map b=new HashMap();
            b.put("count",3);
            b.put("shb",10);
            b.put("ok",dayTask.getTaskb()>=3?true:false);
            base.put("b",b);
            Map c=new HashMap();
            c.put("count",3);
            c.put("shb",5);
            c.put("ok",dayTask.getTaskc()>=3?true:false);
            base.put("c",c);
            Map d=new HashMap();
            d.put("count",10);
            d.put("shb",10);
            d.put("ok",dayTask.getTaskd()>=10?true:false);
            base.put("d",d);
            Map e=new HashMap();
            e.put("count",10);
            e.put("shb",10);
            e.put("ok",dayTask.getTaske()>=10?true:false);
            base.put("e",e);
            Map f=new HashMap();
            f.put("count",10);
            f.put("shb",10);
            f.put("ok",dayTask.getTaskf()>=10?true:false);
            base.put("f",f);
        Map g=new HashMap();
        g.put("count",3);
        g.put("shb",10);
        g.put("ok",dayTask.getTaskg()>=2?true:false);
        base.put("g",g);
        Map h=new HashMap();
        h.put("count",3);
        h.put("shb",10);
        h.put("ok",dayTask.getTaskh()>=2?true:false);
        base.put("h",h);
        Map i=new HashMap();
        i.put("count",3);
        i.put("shb",10);
        i.put("ok",dayTask.getTaski()>=2?true:false);
        base.put("i",i);
        Map j=new HashMap();
        j.put("count",3);
        j.put("shb",10);
        j.put("ok",dayTask.getTaskj()>=2?true:false);
        base.put("j",j);
        Map k=new HashMap();
        k.put("count",3);
        k.put("shb",10);
        k.put("ok",dayTask.getTaskk()>=5?true:false);
        base.put("k",k);

        Map l=new HashMap();
        l.put("count",10);
        l.put("shb",10);
        l.put("ok",dayTask.getTaskl()>=10?true:false);
        base.put("l",l);

        data.put("base",base);
        return Mono.just(Info.SUCCESS(data));
    }


    @ApiOperation(value = "分享统计", notes="分享统计")
    @PostMapping(value = "/sharecount")
    public Mono<Info> sharecount(Principal principal,@RequestParam("type")String type)  {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        DayTask dayTask=dayTaskReposity.findByDay(principal.getName(),dateStr);
        if(null==dayTask){
            dayTask=new DayTask();
        }
        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        dayTask.setUserSecurity(userSecurity);
        String i="分享成功!";


        switch (type){
            case "g":
                dayTask.setTaskg();
                if(dayTask.getTaskg()<=3){
                    userSecurity.setShb(userSecurity.getShb()+10);
                    userSecurityService.save(userSecurity);
                    i=i+"您已成功获得10积分。";
                }
                break;
            case "h":
                dayTask.setTaskh();
                if(dayTask.getTaskh()<=2){
                    userSecurity.setShb(userSecurity.getShb()+10);
                    userSecurityService.save(userSecurity);
                    i=i+"您已成功获得10积分。";
                }
                break;
            case "i":
                dayTask.setTaski();
                if(dayTask.getTaski()<=2){
                    userSecurity.setShb(userSecurity.getShb()+10);
                    userSecurityService.save(userSecurity);
                    i=i+"您已成功获得10积分。";
                }
            case "j":
                dayTask.setTaskj();
                if(dayTask.getTaskj()<=2){
                    userSecurity.setShb(userSecurity.getShb()+10);
                    userSecurityService.save(userSecurity);
                    i=i+"您已成功获得10积分。";
                }
                break;
            case "k":
                dayTask.setTaskk();
                if(dayTask.getTaskk()<=5){
                    userSecurity.setShb(userSecurity.getShb()+10);
                    userSecurityService.save(userSecurity);
                    i=i+"您已成功获得10积分。";
                }
                break;
        }
        dayTask=dayTaskReposity.saveAndFlush(dayTask);

        return Mono.just(Info.SUCCESS(i,dateStr));
    }

    @ApiOperation(value = "用户信息", notes="用户信息")
    @PostMapping(value = "/userinfo")
    public Mono<Info> userinfo(@RequestParam("usercode")String usercode)  {
        Map<String,Object> data=new HashMap<>();
        UserSecurity userSecurity= userSecurityService.findByCode(usercode);
        data.put("follow",followService.myfollowCount(usercode));
        data.put("fans",followService.followmeCount(usercode));
        data.put("userinfo",userSecurity);
        return Mono.just(Info.SUCCESS(data));
    }
}
