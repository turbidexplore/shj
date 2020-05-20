package com.turbid.explore.controller.home;

import com.turbid.explore.pojo.Follow;
import com.turbid.explore.pojo.Notice;
import com.turbid.explore.pojo.UserSecurity;
import com.turbid.explore.repository.NoticeRepository;
import com.turbid.explore.service.FollowService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(description = "关注接口")
@RestController
@RequestMapping("/follow")
@CrossOrigin
public class FollowController {


    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private FollowService followService;

    @Autowired
    private NoticeRepository noticeRepository;

    @ApiOperation(value = "关注", notes="关注")
    @PutMapping("/add")
    public Mono<Info> add(Principal principal, @RequestParam("phone") String phone) {

        Follow follow=new Follow();
        UserSecurity user=userSecurityService.findByPhone(principal.getName());
        follow.setUser(user);
        follow.setUserFollow(userSecurityService.findByPhone(phone));

        noticeRepository.save(new Notice(phone,"用户【"+user.getUserBasic().getNikename()+"】关注了您","关注通知",1,0));

        return Mono.just(Info.SUCCESS( followService.save(follow)));
    }

    @ApiOperation(value = "关注我的", notes="关注我的")
    @PostMapping("/followme")
    public Mono<Info> followme(Principal principal,@RequestParam("page")Integer page) {
        Map<String,Object> jo=new HashMap<>();
        List data=new ArrayList<>();
        followService.followme(principal.getName(),page).forEach(v->{
            v.setIsf(isf(principal.getName(),v.getUser().getPhonenumber()));
            data.add(v);

        });
        jo.put("data",data);
        jo.put("count",followService.followmeCount(principal.getName()));
        return Mono.just(Info.SUCCESS(jo));
    }

    @ApiOperation(value = "我的关注", notes="我的关注")
    @PostMapping("/myfollow")
    public Mono<Info> myfollow(Principal principal,@RequestParam("page")Integer page) {
        Map<String,Object> jo=new HashMap<>();
        List data=new ArrayList<>();
        followService.myfollow(principal.getName(),page).forEach(v->{
            v.setIsf(isf(v.getUserFollow().getPhonenumber(),principal.getName()));
            data.add(v);
        });
        jo.put("data",data);
        jo.put("count",followService.myfollowCount(principal.getName()));
        return Mono.just(Info.SUCCESS( jo));
    }

    public boolean isf(String name,String phone){
        if(0<followService.findByCount(name,phone)){
            return true;
        }else{
            return false;
        }
    }


    @ApiOperation(value = "关注他的", notes="关注他的")
    @PostMapping("/followhe")
    public Mono<Info> followhe(@RequestParam("usercode")String usercode,@RequestParam("page")Integer page) {
        Map<String,Object> jo=new HashMap<>();
        jo.put("data",followService.followhe(usercode,page));
        jo.put("count",followService.followheCount(usercode));
        return Mono.just(Info.SUCCESS(jo));
    }

    @ApiOperation(value = "他的关注", notes="他的关注")
    @PostMapping("/hefollow")
    public Mono<Info> hefollow(@RequestParam("usercode")String usercode,@RequestParam("page")Integer page) {
        Map<String,Object> jo=new HashMap<>();
        jo.put("data",followService.hefollow(usercode,page));
        jo.put("count",followService.hefollowCount(usercode));
        return Mono.just(Info.SUCCESS( jo));
    }

    @ApiOperation(value = "是否关注", notes="是否关注")
    @PutMapping("/isfollow")
    public Mono<Info> isfollow(Principal principal, @RequestParam("phone") String phone) {
        if(0<followService.findByCount(principal.getName(),phone)){
            return Mono.just(Info.SUCCESS(true));
        }else{
            return Mono.just(Info.SUCCESS(false));
        }
    }

    @ApiOperation(value = "取消关注", notes="取消关注")
    @PutMapping("/cancelfollow")
    public Mono<Info> cancelfollow(Principal principal, @RequestParam("phone") String phone) {

        return Mono.just(Info.SUCCESS(followService.cancelfollow(followService.find(principal.getName(),phone))));
    }


}
