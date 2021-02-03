package com.turbid.explore.controller.home;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.turbid.explore.configuration.AsyncTaskA;
import com.turbid.explore.pojo.*;
import com.turbid.explore.push.api.client.push.PushV3Client;
import com.turbid.explore.repository.*;
import com.turbid.explore.service.*;
import com.turbid.explore.service.impl.SMSServiceImpl;
import com.turbid.explore.tools.CodeLib;
import com.turbid.explore.tools.Info;
import com.turbid.explore.tools.TLSSigAPIv2;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Api(description = "用户操作接口")
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private SMSServiceImpl smsService;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private UserBasicService userBasicService;

    @Autowired
    private CheckService checkService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    private String baseUrl="https://console.tim.qq.com/";;
    private String portrait_set="v4/profile/portrait_set";
    private long appid=1400334582;

    public String config(){
        return "?sdkappid="+appid+"&identifier=administrator"+"&usersig="+ TLSSigAPIv2.genSig("administrator",680000000)
                +"&random="+ UUID.randomUUID().toString().replace("-", "").toLowerCase()+"&contenttype=json";
    }

    @ApiOperation(value = "注册", notes="通过手机号注册")
    @PostMapping(value = "/register")
    public Mono<Info> register(@RequestBody JSONObject jo) {
        try {
            if(checkService.findMessagesByMebileAndAuthode(jo.getString("username"),jo.getString("sms"))>0) {
                if(userSecurityService.findByPhoneCount(jo.getString("username"))==0){
                    UserSecurity userSecurity = new UserSecurity();
                    userSecurity.setPassword(jo.getString("password"));
                    userSecurity.setLikes(jo.getString("likes"));
                    userSecurity.setPhonenumber(jo.getString("username"));
                    userSecurity.setType(jo.getInteger("type"));
                    userSecurity.setShb(100);

                    String typename="";
                    switch (jo.getInteger("type")){
                        case 0:
                            typename="设计师";
                            break;
                        case 1:
                            typename="经销商";
                            break;
                        case 2:
                            typename="工厂";
                            break;
                        case 3:
                            typename="设计公司";
                            break;
                    }
                    UserBasic userBasic=new UserBasic();
                    userBasic.setNikename(CodeLib.getNikeName(stringRedisTemplate));
                    userBasic.setHeadportrait(CodeLib.getHeadimg());
                    if(null!=jo.getString("city")&&""!=jo.getString("city")&&!"".equals(jo.getString("city"))) {
                        userBasic.setCity(jo.getString("city").replace("市",""));
                    }else {
                        if(null!=jo.getString("ip")&&""!=jo.getString("ip")&&!"".equals(jo.getString("ip"))) {
                            userBasic.setCity(CodeLib.getAddressByIp(jo.getString("ip")).replace("市", ""));
                        }else {
                            userBasic.setCity("");
                        }
                    }

                    userBasicService.save(userBasic);
                    userSecurity.setUserBasic(userBasic);
                    userSecurity=  userSecurityService.save(userSecurity);
                    Map<String, Object> requestBody = ImmutableMap.of(
                            "Identifier", userSecurity.getCode(),
                            "Nick", userBasic.getNikename(),
                            "FaceUrl","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588763946885&di=11fc71844ac400e62d61e5abbff4e4fb&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fdesign%2F00%2F67%2F59%2F63%2F58e89bee922a2.png");
                    JSONObject jsonObject= restTemplate.postForObject(baseUrl+"v4/im_open_login_svc/account_import"+config()
                            ,requestBody, JSONObject.class);

                    JSONArray data =new JSONArray();
                    JSONObject item=new JSONObject();
                    item.put("Tag","Tag_Profile_Custom_usertype");
                    item.put("Value",typename);
                    data.add(item);

                    requestBody = ImmutableMap.of(
                            "From_Account", userSecurity.getCode(),
                            "ProfileItem",data
                    );
                    jsonObject= restTemplate.postForObject(baseUrl+portrait_set+config()
                            ,requestBody, JSONObject.class);
                    return Mono.just(Info.SUCCESS(userSecurity ));
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
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name="phone", dataType="String", required=true, value="手机号码"),
    })
    public Mono<Info> sms(@RequestParam(name = "phone") String phone) throws InterruptedException {
        smsService.sendSMS(phone);
        return Mono.just(Info.SUCCESS(null));
    }

    @ApiOperation(value = "判断是否注册", notes="判断是否注册")
    @PostMapping(value = "/isregister")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name="phone", dataType="String", required=true, value="手机号码"),
    })
    public Mono<Info> isregister(@RequestParam(name = "phone") String phone) {
        if(userSecurityService.findByPhoneCount(phone)==0){
            return Mono.just(Info.SUCCESS(false));
        }else {
            return Mono.just(Info.SUCCESS(true));
        }

    }


    @ApiOperation(value = "加密", notes="密码加密")
    @PostMapping(value = "/pe")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name="password", dataType="String", required=true, value="密码"),
    })
    public Mono<Info> pe(@RequestParam(name = "password") String password)  {

        return Mono.just(Info.SUCCESS(CodeLib.encrypt(password)));
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoginHisRepository loginHisRepository;

    @Autowired
    private AsyncTaskA asyncTask;

    @ApiOperation(value = "登陆", notes="login_type为sms或password")
    @PostMapping("/login")
    public Mono<Info> login(@RequestBody JSONObject jsonObject){
        LoginHis loginHis=new LoginHis();
        loginHis.setPhone(jsonObject.getString("username"));
        loginHis.setOs(jsonObject.getString("os"));
        loginHis.setLogintype(jsonObject.getString("login_type"));
        try {
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("grant_type","password");
            map.add("scope","web");
            map.add("login_type",jsonObject.getString("login_type"));
            map.add("username",jsonObject.getString("username"));
            map.add("password", jsonObject.getString("password"));
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth("turbid","turbid_anoax!@#321");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            JSONObject object=restTemplate.postForObject("http://127.0.0.1:10002/oauth/token",request,JSONObject.class);
            if (null==object){
                loginHis.setStatus(0);
                return Mono.just(Info.ERROR("登录失败"));
            }
            loginHis.setStatus(1);
            UserSecurity userSecurity=userSecurityService.findByPhone(jsonObject.getString("username"));
            object.put("userinfo",userSecurity);
            asyncTask.checkOrderStatus(userSecurity.getPhonenumber());
            loginHisRepository.saveAndFlush(loginHis);
            return Mono.just(Info.SUCCESS(object));
        }catch (Exception e){
            e.getStackTrace();
            loginHis.setStatus(0);
            loginHisRepository.saveAndFlush(loginHis);
            return Mono.just(Info.ERROR("帐号或密码错误"));
        }
    }

    @Autowired
    private OpenUserRepository openUserRepository;

    @ApiOperation(value = "第三方注册", notes="第三方注册")
    @PostMapping(value = "/openregister")
    public Mono<Info> openregister(@RequestBody JSONObject jo) {
        try {
            if(checkService.findMessagesByMebileAndAuthode(jo.getString("username"),jo.getString("sms"))>0) {
                if(userSecurityService.findByPhoneCount(jo.getString("username"))==0){
                    UserSecurity userSecurity = new UserSecurity();
                    userSecurity.setPassword(jo.getString("password"));
                    userSecurity.setLikes(jo.getString("likes"));
                    userSecurity.setPhonenumber(jo.getString("username"));
                    userSecurity.setType(jo.getInteger("type"));

                    String typename="";
                    switch (jo.getInteger("type")){
                        case 0:
                            typename="设计师";
                            break;
                        case 1:
                            typename="经销商";
                            break;
                        case 2:
                            typename="工厂";
                            break;
                        case 3:
                            typename="设计公司";
                            break;
                    }

                    UserBasic userBasic=new UserBasic();
                    userBasic.setNikename(CodeLib.getNikeName(stringRedisTemplate));
                    userBasic.setHeadportrait(CodeLib.getHeadimg());
                    if(null!=jo.getString("city")&&""!=jo.getString("city")&&!"".equals(jo.getString("city"))) {
                        userBasic.setCity(jo.getString("city").replace("市",""));
                    }else {
                        if(null!=jo.getString("ip")&&""!=jo.getString("ip")&&!"".equals(jo.getString("ip"))) {
                            userBasic.setCity(CodeLib.getAddressByIp(jo.getString("ip")).replace("市", ""));
                        }else {
                            userBasic.setCity("");
                        }
                    }

                    userBasicService.save(userBasic);
                    userSecurity.setUserBasic(userBasic);
                    userSecurity=  userSecurityService.save(userSecurity);

                    OpenUser openUser=new OpenUser();
                    openUser.setPhone(jo.getString("username"));
                    openUser.setOpenid(jo.getString("openid"));
                    openUser.setOpentype(jo.getString("opentype"));
                    openUser.setUserSecurity(userSecurity);
                    openUserRepository.saveAndFlush(openUser);
                    Map<String, Object> requestBody = ImmutableMap.of(
                            "Identifier", userSecurity.getCode(),
                            "Nick", userBasic.getNikename(),
                            "FaceUrl","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588763946885&di=11fc71844ac400e62d61e5abbff4e4fb&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fdesign%2F00%2F67%2F59%2F63%2F58e89bee922a2.png");
                    restTemplate.postForObject(baseUrl+"v4/im_open_login_svc/account_import"+config()
                            ,requestBody, JSONObject.class);

                    JSONArray data =new JSONArray();
                    JSONObject item=new JSONObject();
                    item.put("Tag","Tag_Profile_Custom_usertype");
                    item.put("Value",typename);
                    data.add(item);

                    requestBody = ImmutableMap.of(
                            "From_Account", userSecurity.getCode(),
                            "ProfileItem",data
                    );
                    restTemplate.postForObject(baseUrl+portrait_set+config()
                            ,requestBody, JSONObject.class);

                    MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
                    map.add("grant_type","password");
                    map.add("scope","web");
                    map.add("login_type","open");
                    map.add("username",userSecurity.getPhonenumber());
                    map.add("password", "123456");
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    headers.setBasicAuth("turbid","turbid_anoax!@#321");
                    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
                    JSONObject object=restTemplate.postForObject("http://127.0.0.1:10002/oauth/token",request,JSONObject.class);
                    if (null==object){
                        return Mono.just(Info.ERROR("登录失败"));
                    }
                    object.put("userinfo",userSecurity);
                    return Mono.just(Info.SUCCESS(object));
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

    @ApiOperation(value = "第三方绑定")
    @PostMapping("/addopenuser")
    public Mono<Info> addopenuser(@RequestParam("phone")String phone,@RequestParam("smscode")String smscode,@RequestParam("openid")String openid,@RequestParam("opentype")String opentype,@RequestParam("os")String os){
        if(checkService.findMessagesByMebileAndAuthode(phone,smscode)>0) {
            UserSecurity userSecurity =userSecurityService.findByPhone(phone);
            if(0<openUserRepository.countByPhoneAndOpentype(openid)){
                return Mono.just(Info.ERROR("该用户已绑定第三方账号"));
            }
            OpenUser openUser=new OpenUser();
            openUser.setPhone(phone);
            openUser.setOpenid(openid);
            openUser.setOpentype(opentype);
            openUser.setUserSecurity(userSecurity);
            openUserRepository.saveAndFlush(openUser);
            if (null == userSecurity) {
                return Mono.just(Info.ERROR("未注册"));
            }
            LoginHis loginHis = new LoginHis();
            loginHis.setPhone(openid);
            loginHis.setOs(os);
            loginHis.setLogintype(opentype);
            try {
                MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                map.add("grant_type", "password");
                map.add("scope", "web");
                map.add("login_type", "open");
                map.add("username", userSecurity.getPhonenumber());
                map.add("password", "123456");
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                headers.setBasicAuth("turbid", "turbid_anoax!@#321");
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
                JSONObject object = restTemplate.postForObject("http://127.0.0.1:10002/oauth/token", request, JSONObject.class);
                if (null == object) {
                    loginHis.setStatus(0);
                    return Mono.just(Info.ERROR("登录失败"));
                }
                loginHis.setStatus(1);
                object.put("userinfo", userSecurity);
                return Mono.just(Info.SUCCESS(object));
            } catch (Exception e) {
                e.getStackTrace();
                loginHis.setStatus(0);
                return Mono.just(Info.ERROR("帐号或密码错误"));
            } finally {
                loginHisRepository.saveAndFlush(loginHis);
            }
        }else {
            return Mono.just(Info.ERROR("验证码错误"));
        }
    }

    @ApiOperation(value = "第三方登陆", notes="login_type为sms或password")
    @PostMapping("/openlogin")
    public Mono<Info> openlogin(@RequestParam("openid")String openid,@RequestParam("os")String os){
        OpenUser openUser= openUserRepository.findByOpenid(openid);
        if (null==openUser){
            return Mono.just(Info.ERROR("未注册"));
        }
        UserSecurity userSecurity=openUser.getUserSecurity();
        LoginHis loginHis=new LoginHis();
        loginHis.setPhone(openid);
        loginHis.setOs(os);
        loginHis.setLogintype("open");
        try {
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("grant_type","password");
            map.add("scope","web");
            map.add("login_type","open");
            map.add("username",userSecurity.getPhonenumber());
            map.add("password", "123456");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth("turbid","turbid_anoax!@#321");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            JSONObject object=restTemplate.postForObject("http://127.0.0.1:10002/oauth/token",request,JSONObject.class);
            if (null==object){
                loginHis.setStatus(0);
                return Mono.just(Info.ERROR("登录失败"));
            }
            loginHis.setStatus(1);
            object.put("userinfo",userSecurity);
            return Mono.just(Info.SUCCESS(object));
        }catch (Exception e){
            e.getStackTrace();
            loginHis.setStatus(0);
            return Mono.just(Info.ERROR("帐号或密码错误"));
        }finally {
            loginHisRepository.saveAndFlush(loginHis);
        }
    }

    @ApiOperation(value = "修改密码", notes="通过验证码修改密码")
    @PostMapping("/setPassword")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name="phone", dataType="String", required=true, value="手机号码"),
            @ApiImplicitParam(paramType="query", name="password", dataType="String", required=true, value="密码"),
            @ApiImplicitParam(paramType="query", name="sms", dataType="String", required=true, value="验证码"),
    })
    public Mono<Info> setPassword(@RequestParam(name = "phone")String phone,@RequestParam(name = "sms")String sms,@RequestParam(name = "password")String password){
        try {
            if(checkService.findMessagesByMebileAndAuthode(phone,sms)>0) {
                if (userSecurityService.findByPhoneCount(phone) > 0) {
                    UserSecurity userSecurity= userSecurityService.findByPhone(phone);
                    userSecurity.setPassword(password);
                    userSecurityService.save(userSecurity);
                    return Mono.just(Info.SUCCESS(null));
                }else {
                    return Mono.just(Info.ERROR("帐号不存在"));
                }
            }else {
                return Mono.just(Info.ERROR("无效验证码"));
            }


        }catch (Exception e){
            System.out.println(e);
            return Mono.just(Info.ERROR(e.getMessage()));
        }
    }

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private ShopService shopService;

    @ApiOperation(value = "商户认证", notes="商户认证")
    @PostMapping(value = "/userauth")
    public Mono<Info> userauth(@RequestBody Shop shop)  {
        try {
            UserSecurity userSecurity= userSecurityService.findByPhone(shop.getContactphone());
            shop.setStatus(1);
            if(null==userSecurity||userSecurity.equals(null)){
                userSecurity=new UserSecurity();
                userSecurity.setPhonenumber(shop.getContactphone());
                userSecurity.setPassword(CodeLib.encrypt("123456"));
                UserAuth userAuth =new UserAuth();
                userAuth.setName(shop.getContactname());
                userAuth.setStatus(1);
                userSecurity.setUserAuth(userAuthService.save(userAuth));
                UserBasic userBasic=new UserBasic();
                userBasic.setNikename(CodeLib.getNikeName(stringRedisTemplate));
                userBasic.setHeadportrait( CodeLib.getHeadimg());
                userBasicService.save(userBasic);
                userSecurity.setUserBasic(userBasic);
                userSecurity.setType(2);


            }else {
                UserAuth userAuth =new UserAuth();
                userAuth.setStatus(1);
                userSecurity.setUserAuth(userAuthService.save(userAuth));
                userAuthService.save( userSecurity.getUserAuth());
            }
            userSecurity= userSecurityService.save(userSecurity);
            Map<String, Object> requestBody = ImmutableMap.of(
                    "Identifier", userSecurity.getCode(),
                    "Nick", shop.getName(),
                    "FaceUrl",shop.getLogo());
            JSONObject jsonObject= restTemplate.postForObject(baseUrl+"v4/im_open_login_svc/account_import"+config()
                    ,requestBody, JSONObject.class);
            JSONArray data =new JSONArray();
            JSONObject item=new JSONObject();
            item.put("Tag","Tag_Profile_Custom_usertype");
            item.put("Value",2);
            data.add(item);

            requestBody = ImmutableMap.of(
                    "From_Account", userSecurity.getCode(),
                    "ProfileItem",data );
            jsonObject= restTemplate.postForObject(baseUrl+portrait_set+config()
                    ,requestBody, JSONObject.class);
            shop.setUserSecurity(userSecurity);
            shop=shopService.save(shop);
            userSecurity.setShopcode(shop.getCode());
            userSecurityService.save(userSecurity);
            return Mono.just(Info.SUCCESS(shop));
        }catch (Exception e){
            e.printStackTrace();
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }

    }

    @ApiOperation(value = "用户认证", notes="用户认证")
    @PostMapping(value = "/auth")
    public Mono<Info> auth(Principal principal,@RequestParam("name") String name,@RequestParam("idcard") String idcard,@RequestParam("idcardpositive") String idcardpositive,@RequestParam("idcardreverse") String idcardreverse)  {
        try {
            UserSecurity userSecurity= userSecurityService.findByPhone(principal.getName());
            UserAuth userAuth=userSecurity.getUserAuth();
            if (null==userAuth){
                userAuth=new UserAuth();
            }
            userAuth.setName(name);
            userAuth.setIdcard(idcard);
            userAuth.setIdcardpositive(idcardpositive);
            userAuth.setIdcardreverse(idcardreverse);
            userAuth.setStatus(1);

            if(userSecurity.getAuth()==0) {
                userSecurity.setShb(userSecurity.getShb()+100);
                userSecurity.setAuth(1);
            }

            userSecurity.setUserAuth(userAuthService.save(userAuth));
            return Mono.just(Info.SUCCESS(userSecurityService.save(userSecurity)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }

    }

    @ApiOperation(value = "检查验证码", notes="检查验证码是否正确,传入手机号和验证码,当data值为0时表示为失败，为其他数值时为成功")
    @PostMapping(value = "/check")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name="phone", dataType="String", required=true, value="手机号码"),
            @ApiImplicitParam(paramType="query", name="sms", dataType="String", required=true, value="验证码"),
    })
    public Mono<Info> check(@RequestParam(name = "phone")String phone,@RequestParam(name = "sms")String sms)  {
        return Mono.just(Info.SUCCESS(checkService.findMessagesByMebileAndAuthode(phone,sms)));
    }


    @ApiOperation(value = "获取用户信息", notes="通过token获取用户信息")
    @PostMapping(value = "/userinfo")
    public Mono<Info> userinfo(Principal principal,HttpServletRequest request)  {
        return Mono.just(Info.SUCCESS(userSecurityService.findByPhone(principal.getName())));
    }

    private static String getIpAddress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if(index != -1){
                return XFor.substring(0,index);
            }else{
                return XFor;
            }
        }
        XFor = Xip;
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }

    @ApiOperation(value = "获取用户信息", notes="通过USERCODE获取用户信息")
    @PostMapping(value = "/userinfoByCode")
    public Mono<Info> userinfoByCode(@RequestParam("usercode")String usercode)  {
        return Mono.just(Info.SUCCESS(userSecurityService.findByCode(usercode)));
    }

    @ApiOperation(value = "获取用户认证信息", notes="获取用户认证信息")
    @PostMapping(value = "/authinfo")
    public Mono<Info> authinfo(Principal principal)  {

        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        Map<String,Object> map=new HashMap<>();
        if(null!=userSecurity.getUserAuth()) {
            map.put("isauth", userSecurity.getUserAuth().getStatus());
            map.put("margin",userSecurity.getUserAuth().getMargin());
            map.put("vipday",userSecurity.getUserAuth().getVipday());
            map.put("isvip",isvip(userSecurity.getUserAuth().getVipday()));
        }
        map.put("usertype",userSecurity.getType());
        return Mono.just(Info.SUCCESS(map));
    }

    public boolean isvip(String day){
        if(day==null||day==""){
            return false;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
        try {
            Date date = simpleDateFormat.parse(day);
            if(date.getTime()>new Date().getTime()){ return true; }else { return false; }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    @ApiOperation(value = "判断是否vip是否有效", notes="判断是否vip是否有效")
    @PostMapping(value = "/isvip")
    public Mono<Info> vip(@RequestParam("usercode")String usercode) throws ParseException {
        boolean vip=false;
        UserSecurity userSecurity = userSecurityService.findByCode(usercode);
        UserAuth userAuth=userSecurity.getUserAuth();
        if(null!=userAuth){
            if (null!=userAuth.getVipday()){
                vip=isvip(userAuth.getVipday());
            }
        }
        return Mono.just(Info.SUCCESS(vip));
    }

    @Autowired
    private ComplaintSerivce complaintSerivce;

    @ApiOperation(value = "投诉用户", notes="投诉用户")
    @PostMapping(value = "/complaint")
    public Mono<Info> complaint(Principal principal, @RequestBody Complaint complaint)  {
        complaint.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        complaint.setComplaintUserSecurity(userSecurityService.findByCode(complaint.getComplaintUserSecurity().getCode()));
        return Mono.just(Info.SUCCESS(complaintSerivce.save(complaint)));
    }

    @ApiOperation(value = "修改用户信息", notes="修改用户信息")
    @PostMapping(value = "/update")
    public Mono<Info> update(Principal principal,@RequestParam(value = "nikename",required = false)String nikename,@RequestParam(value = "logo",required = false)String logo,@RequestParam(value = "likes",required = false)String likes)  {
        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        JSONArray data =new JSONArray();
        JSONObject item=null;
        if(nikename!=null&&!nikename.equals(null)){
            userSecurity.getUserBasic().setNikename(nikename);
            item=new JSONObject();
            item.put("Tag","Tag_Profile_IM_Nick");
            item.put("Value",nikename);
            data.add(item);
        }
        if(logo!=null&&!logo.equals(null)){
            userSecurity.getUserBasic().setHeadportrait(logo);
            userBasicService.save(userSecurity.getUserBasic());
            item=new JSONObject();
            item.put("Tag","Tag_Profile_IM_Image");
            item.put("Value",logo);
            data.add(item);
        }
        if(likes!=null&&!likes.equals(null)){
            userSecurity.setLikes(likes);
        }
        Map<String, Object> requestBody = ImmutableMap.of(
                "From_Account", userSecurity.getCode(),
                "ProfileItem",data
        );
        if(userSecurity.getInfo()==0) {
            userSecurity.setShb(userSecurity.getShb()+100);
            userSecurity.setInfo(1);
        }
        restTemplate.postForObject(baseUrl+portrait_set+config()
                ,requestBody, JSONObject.class);
        return Mono.just(Info.SUCCESS(userSecurityService.save(userSecurity)));
    }


    @ApiOperation(value = "所有第三方用户", notes="所有第三方用户")
    @PostMapping(value = "/allopenuser")
    public Mono<Info> allopenuser(Principal principal)  {
        return Mono.just(Info.SUCCESS(openUserRepository.findByPhone(principal.getName())));
    }

    @ApiOperation(value = "第三方用户解除绑定", notes="第三方用户解除绑定")
    @DeleteMapping(value = "/removeopenuser")
    @Transactional
    public Mono<Info> removeopenuser(Principal principal,@RequestParam("openid")String openid)  {
        return Mono.just(Info.SUCCESS(openUserRepository.deleteByPhoneAndOpenid(principal.getName(),openid)));
    }

    @ApiOperation(value = "第三方用户绑定", notes="第三方用户绑定")
    @PutMapping(value = "/addopen")
    @Transactional
    public Mono<Info> addopenuser(Principal principal,@RequestParam("openid")String openid,@RequestParam("opentype")String opentype)  {
        if(0<openUserRepository.countByPhoneAndOpentype(openid)){
            return Mono.just(Info.ERROR("该用户已绑定第三方账号"));
        }
        OpenUser openUser=new OpenUser();
        openUser.setPhone(principal.getName());
        openUser.setOpenid(openid);
        openUser.setOpentype(opentype);
        openUser.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        return Mono.just(Info.SUCCESS(openUserRepository.saveAndFlush(openUser)));
    }

    @Autowired
    private NeedsRelationRepositroy needsRelationRepositroy;

    @Autowired
    private StudyRelationRepository studyRelationRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private NoticeRepository noticeRepository;


    @ApiOperation(value = "修改手机号码", notes="修改手机号码")
    @PutMapping(value = "/updatephone")
    @Transactional
    public Mono<Info> updatephone(Principal principal,@RequestParam("phone")String phone,@RequestParam("smscode")String smscode)  {
        if(checkService.findMessagesByMebileAndAuthode(phone,smscode)>0) {
            UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
            userSecurity.setPhonenumber(phone);
            userSecurityService.save(userSecurity);
//            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
//            map.add("grant_type","password");
//            map.add("scope","web");
//            map.add("login_type","open");
//            map.add("username",phone);
//            map.add("password", "123456");
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//            headers.setBasicAuth("turbid","turbid_anoax!@#321");
//            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
//            JSONObject object=restTemplate.postForObject("http://127.0.0.1:10002/oauth/token",request,JSONObject.class);
//            if (null==object){
//                return Mono.just(Info.ERROR("登录失败"));
//            }
//            object.put("userinfo",userSecurity);
            openUserRepository.updatephone(principal.getName(),phone);
            needsRelationRepositroy.updatephone(principal.getName(),phone);
            studyRelationRepository.updatephone(principal.getName(),phone);
            orderRepository.updatephone(principal.getName(),phone);
            noticeRepository.updatephone(principal.getName(),phone);


            return Mono.just(Info.SUCCESS(null));
        }else {
            return Mono.just(Info.ERROR("验证码错误"));
        }
    }

    @ApiOperation(value = "店铺用户", notes="店铺用户")
    @GetMapping(value = "/shopuserlist")
    public Mono<Info> shopuserlist(Principal principal,@RequestParam(value = "shopcode",required = false)String shopcode,@RequestParam("page")Integer page)  {
        Shop shop=shopService.getByCode(shopcode);
        return Mono.just(Info.SUCCESS(shop.getUserSecurity().getCode(),userSecurityService.shopusers(shop.getCode(),null,page)));
    }


    @ApiOperation(value = "店铺用户", notes="店铺用户")
    @GetMapping(value = "/shopusers")
    public Mono<Info> shopusers(Principal principal,@RequestParam(value = "text",required = false)String text,@RequestParam("page")Integer page)  {
        Shop shop=shopService.getByUser(principal.getName());
        return Mono.just(Info.SUCCESS(shop.getUserSecurity().getCode(),userSecurityService.shopusers(shop.getCode(),text,page)));
    }

    @ApiOperation(value = "店铺用户总数", notes="店铺用户总数")
    @GetMapping(value = "/shopuserscount")
    public Mono<Info> shopuserscount(Principal principal,@RequestParam(value = "text",required = false)String text)  {
        String shopcode=  shopService.getByUser(principal.getName()).getCode();
        return Mono.just(Info.SUCCESS(userSecurityService.shopuserscount(shopcode,text)));
    }

    @ApiOperation(value = "绑定用户", notes="绑定用户")
    @PutMapping(value = "/addshopuser")
    public Mono<Info> addshopuser(Principal principal,@RequestParam(value = "code")String code)  {
        UserSecurity userSecurity= userSecurityService.findByCode(code);
        Shop shop=shopService.getByUser(principal.getName());
        userSecurity.setType(2);
        userSecurity.setShopcode(shop.getCode());
        userSecurityService.save(userSecurity);
        noticeRepository.save(new Notice(userSecurity.getPhonenumber(), "您已经成功成为【"+shop.getName()+"】店铺的子账号。", "系统通知", 0, 0));
        return Mono.just(Info.SUCCESS(null));
    }


    @ApiOperation(value = "取消绑定", notes="取消绑定")
    @PutMapping(value = "/delshopuser")
    public Mono<Info> delshopuser(Principal principal,@RequestParam(value = "code")String code)  {
        UserSecurity userSecurity= userSecurityService.findByCode(code);
        Shop shop=shopService.getByCode(userSecurity.getShopcode());
        userSecurity.setShopcode(null);
        userSecurityService.save(userSecurity);
        noticeRepository.save(new Notice(userSecurity.getPhonenumber(), "您已与【"+shop.getName()+"】店铺解除账号绑定。", "系统通知", 0, 0));
        return Mono.just(Info.SUCCESS(null));
    }

    @ApiOperation(value = "通过手机号码搜索用户", notes="通过手机号码搜索用户")
    @GetMapping(value = "/finduserbyphone")
    public Mono<Info> finduserbyphone(Principal principal,@RequestParam(value = "phone")String phone)  {
        return Mono.just(Info.SUCCESS(userSecurityService.finduserbyphone(phone)));
    }


    @ApiOperation(value = "通过手机号码搜索用户", notes="通过手机号码搜索用户")
    @GetMapping(value = "/alluserbypage")
    public Mono<Info> alluserbypage(Principal principal,@RequestParam(value = "page")Integer page,@RequestParam(value = "text",required = false)String text)  {
        return Mono.just(Info.SUCCESS(userSecurityService.alluserbypage(page,text)));
    }

    @ApiOperation(value = "通过手机号码搜索用户", notes="通过手机号码搜索用户")
    @GetMapping(value = "/allusercount")
    public Mono<Info> allusercount(Principal principal,@RequestParam(value = "text",required = false)String text)  {
        return Mono.just(Info.SUCCESS(userSecurityService.allusercount(text)));
    }

    @ApiOperation(value = "获取im资料")
    @GetMapping(value = "/getiminfo")
    public Mono<Info> getiminfo(Principal principal,@RequestParam(value = "usercode")String usercode)  {
        Map data =new HashMap();
        UserSecurity userSecurity=userSecurityService.findByCode(usercode);
        data.put("type",userSecurity.getType());
        data.put("name",userSecurity.getUserBasic().getNikename());
        data.put("headimg",userSecurity.getUserBasic().getHeadportrait());
        if(userSecurity.getType()>=2) {
            data.put("yx",shopService.getByCode(userSecurity.getShopcode()).getIschoose());
        }
        return Mono.just(Info.SUCCESS(data));
    }


    @ApiOperation(value = "推荐新用户")
    @GetMapping(value = "/newuser")
    public Mono<Info> newuser(Principal principal)  {

        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        if(userSecurity.getNewuser()==0) {
            userSecurity.setShb(userSecurity.getShb() + 100);
            userSecurity.setNewuser(1);
            userSecurity= userSecurityService.save(userSecurity);
        }
        return Mono.just(Info.SUCCESS(userSecurity));
    }



}


