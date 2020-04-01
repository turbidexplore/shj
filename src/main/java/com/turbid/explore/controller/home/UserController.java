package com.turbid.explore.controller.home;
import com.alibaba.fastjson.JSONObject;
import com.turbid.explore.pojo.UserAuth;
import com.turbid.explore.pojo.UserSecurity;
import com.turbid.explore.service.message.CheckService;
import com.turbid.explore.service.message.SMSService;
import com.turbid.explore.service.message.impl.SMSServiceImpl;
import com.turbid.explore.service.user.UserAuthService;
import com.turbid.explore.service.user.impl.UserSecurityServiceImpl;
import com.turbid.explore.utils.CodeLib;
import com.turbid.explore.utils.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@Api(description = "用户操作接口")
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private SMSServiceImpl smsService;

    @Autowired
    private UserSecurityServiceImpl userSecurityService;

    @Autowired
    private CheckService checkService;

    @ApiOperation(value = "注册", notes="通过手机号注册")
    @PostMapping(value = "/register")
    public Mono<Info> register(@RequestBody JSONObject jo) {
       try {
           if(checkService.findMessagesByMebileAndAuthode(jo.getString("username"),jo.getString("sms"))>0) {
               if(userSecurityService.findByPhoneCount(jo.getString("username"))==0){
                   UserSecurity userSecurity = new UserSecurity();
                   userSecurity.setPassword(jo.getString("password"));
                   userSecurity.setPhonenumber(jo.getString("username"));
                   userSecurityService.save(userSecurity);
                   return Mono.just(Info.SUCCESS(null));
               }else {
                   return Mono.just(Info.ERROR("手机号码已注册"));
               }
           }else{
               return Mono.just(Info.ERROR("验证码错误"));
           }
       }catch (Exception e){
           return Mono.just(Info.ERROR(e.getMessage()));
       }

    }

    @ApiOperation(value = "发送验证码", notes="发送验证码")
    @PostMapping(value = "/sms")
    public Mono<Info> sms(@RequestParam(name = "phone") String phone) throws InterruptedException {
        smsService.sendSMS(phone);
        return Mono.just(Info.SUCCESS(null));
    }


    @ApiOperation(value = "加密", notes="加密")
    @PostMapping(value = "/pe")
    public Mono<Info> pe(@RequestParam(name = "password") String password)  {

        return Mono.just(Info.SUCCESS(CodeLib.encrypt(password)));
    }

    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "登陆", notes="登陆")
    @PostMapping("/login")
    public Mono<Info> login(@RequestBody JSONObject jsonObject){
        try {
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("grant_type","password");
            map.add("scope","web");
            map.add("login_type",jsonObject.getString("login_type"));
            map.add("username",jsonObject.getString("username"));
            map.add("password", jsonObject.getString("password"));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth("turbid","turbid_anoax!@#321");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            JSONObject object=restTemplate.postForObject("http://127.0.0.1:10002/oauth/token",request,JSONObject.class);

            if (null==object){
                return Mono.just(Info.ERROR("登录失败"));
            }
            return Mono.just(Info.SUCCESS(object));
        }catch (Exception e){
            e.getStackTrace();
            return Mono.just(Info.ERROR("帐号或密码错误"));
        }
    }

    @ApiOperation(value = "修改密码", notes="修改密码")
    @PostMapping("/setPassword")
    public Mono<Info> setPassword(@RequestParam(name = "phone")String phone,@RequestParam(name = "sms")String sms,@RequestParam(name = "password")String password){
        try {
            if(checkService.findMessagesByMebileAndAuthode(phone,sms)>0) {
                if (userSecurityService.findByPhoneCount(phone) > 0) {
                      UserSecurity userSecurity= userSecurityService.findByPhone(phone);
                        userSecurity.setPassword(password);
                        userSecurityService.save(userSecurity);
                    return Mono.just(Info.SUCCESS(null));
                }else {
                    return Mono.just(Info.ERROR("帐号或密码错误"));
                }
            }else {
                return Mono.just(Info.ERROR("帐号或密码错误"));
            }


        }catch (Exception e){
            System.out.println(e);
            return Mono.just(Info.ERROR(e.getMessage()));
        }
    }

    @Autowired
    UserAuthService userAuthService;

    @ApiOperation(value = "用户认证", notes="用户认证")
    @PostMapping(value = "/userauth")
    public Mono<Info> userauth(@RequestBody UserAuth userAuth)  {
        try {
           UserSecurity userSecurity= userSecurityService.findByPhone(userAuth.getContactphone());
           if(null==userSecurity.getPhonenumber()||userSecurity.equals(null)){
               userSecurity.setPhonenumber(userAuth.getContactphone());
           }
            userAuth.setStatus(0);
            userSecurity.setUserAuth(userAuth);
            userSecurityService.save(userSecurity);
        }catch (Exception e){
            e.getStackTrace();
        }
        return Mono.just(Info.SUCCESS(null));
    }

    @ApiOperation(value = "检查验证码", notes="检查验证码")
    @PostMapping(value = "/check")
    public Mono<Info> check(@RequestParam(name = "phone")String phone,@RequestParam(name = "sms")String sms)  {
        return Mono.just(Info.SUCCESS(checkService.findMessagesByMebileAndAuthode(phone,sms)));
    }



}
