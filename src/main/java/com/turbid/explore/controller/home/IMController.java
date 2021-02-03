package com.turbid.explore.controller.home;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.turbid.explore.pojo.*;
import com.turbid.explore.pojo.bo.Message;
import com.turbid.explore.repository.CallCountRepository;
import com.turbid.explore.repository.CallPhonehisRepository;
import com.turbid.explore.repository.NoticeRepository;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.sokect.WebSocketServer;
import com.turbid.explore.tools.Info;
import com.turbid.explore.tools.TLSSigAPIv2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Api(description = "即时通讯接口")
@RestController
@RequestMapping("/im")
@CrossOrigin
public class IMController {

    @Autowired
    private RestTemplate restTemplate;

    private String baseUrl="https://console.tim.qq.com/";

    private String account_import_url="v4/im_open_login_svc/account_import";

    private String push_="v4/openim/im_push";

    private String send_msg="v4/openim/sendmsg";

    private String portrait_set="v4/profile/portrait_set";

    private long appid=1400475685;

    public String config(){
        return "?sdkappid="+appid+"&identifier=administrator"+"&usersig="+TLSSigAPIv2.genSig("administrator",680000000)
                +"&random="+ UUID.randomUUID().toString().replace("-", "").toLowerCase()+"&contenttype=json";
    }

    public Object createGroup(String usercode,String title){
        Map<String, Object> requestBody = ImmutableMap.of(
                "Owner_Account", usercode,
                "Type","AVChatRoom",
                "Owner_Account",title
        );
        JSONObject jsonObject= restTemplate.postForObject(baseUrl+"v4/group_open_http_svc/create_group"+config()
                ,requestBody, JSONObject.class);
        return jsonObject;
    }

    public Object removeGroup(String usercode){
        Map<String, Object> requestBody = ImmutableMap.of(
                "GroupId", usercode
        );
        JSONObject jsonObject= restTemplate.postForObject(baseUrl+"v4/group_open_http_svc/destroy_group"+config()
                ,requestBody, JSONObject.class);
        return jsonObject;
    }

    @ApiOperation(value = "签名", notes="签名")
    @PutMapping("/sig")
    public Mono<Info> sig(@RequestParam("identifier") String identifier) {

        return Mono.just(Info.SUCCESS(TLSSigAPIv2.genSig(identifier,680000000)));
    }

    @ApiOperation(value = "导入账号", notes="导入账号")
    @PutMapping("/account_import")
    public Mono<Info> account_import(@RequestParam("identifier") String identifier,@RequestParam("nikename") String nikename) {
        Map<String, Object> requestBody = ImmutableMap.of(
                "Identifier", identifier,
                "Nick", nikename,
                "FaceUrl","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1588763946885&di=11fc71844ac400e62d61e5abbff4e4fb&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fdesign%2F00%2F67%2F59%2F63%2F58e89bee922a2.png");
        JSONObject jsonObject= restTemplate.postForObject(baseUrl+account_import_url+config()
               ,requestBody, JSONObject.class);
        return Mono.just(Info.SUCCESS(jsonObject));
    }


    @ApiOperation(value = "设置资料", notes="设置资料")
    @PutMapping("/changename")
    public Mono<Info> changename(@RequestParam("identifier") String identifier,@RequestParam("name") String name) {
        JSONArray data =new JSONArray();
        JSONObject item=new JSONObject();
        item.put("Tag","Tag_Profile_IM_Nick");
        item.put("Value",name);
        data.add(item);

        Map<String, Object> requestBody = ImmutableMap.of(
                "From_Account", identifier,
                "ProfileItem",data
        );
        JSONObject jsonObject= restTemplate.postForObject(baseUrl+portrait_set+config()
                ,requestBody, JSONObject.class);
        return Mono.just(Info.SUCCESS(jsonObject));
    }

    @ApiOperation(value = "设置资料", notes="设置资料")
    @PutMapping("/deleteuser")
    public Mono<Info> deleteuser(@RequestParam("identifier") String identifier) {
        JSONArray data =new JSONArray();
        JSONObject item=new JSONObject();
        item.put("UserID",identifier);
        data.add(item);

        Map<String, Object> requestBody = ImmutableMap.of(
                "DeleteItem", data
        );
        JSONObject jsonObject= restTemplate.postForObject(baseUrl+"v4/im_open_login_svc/account_delete"+config()
                ,requestBody, JSONObject.class);
        return Mono.just(Info.SUCCESS(jsonObject));
    }

    @ApiOperation(value = "设置资料", notes="设置资料")
    @PutMapping("/portrait_set")
    public Mono<Info> portrait_set(@RequestParam("identifier") String identifier,@RequestParam("type") String type) {
        JSONArray data =new JSONArray();
        JSONObject item=new JSONObject();
        item.put("Tag","Tag_Profile_Custom_usertype");
        item.put("Value",type);
        data.add(item);
        item=new JSONObject();
        item.put("Tag","Tag_Profile_Custom_yx");
        item.put("Value","严选");
        data.add(item);

        Map<String, Object> requestBody = ImmutableMap.of(
                "From_Account", identifier,
                "ProfileItem",data
        );
        JSONObject jsonObject= restTemplate.postForObject(baseUrl+portrait_set+config()
                ,requestBody, JSONObject.class);
        return Mono.just(Info.SUCCESS(jsonObject));
    }

    @ApiOperation(value = "单发单聊", notes="单发单聊")
    @PostMapping("/sendmsg")
    public Mono<Info> sendmsg(@RequestBody Message message) {

        JSONObject jsonObject= restTemplate.postForObject(baseUrl+send_msg+config(),message, JSONObject.class);
        return Mono.just(Info.SUCCESS(jsonObject));
    }

    @ApiOperation(value = "推送", notes="推送")
    @PostMapping("/imPush")
    public Mono<Info> imPush(@RequestBody Message message) {

        JSONObject jsonObject= restTemplate.postForObject(baseUrl+push_+config(),message, JSONObject.class);
        return Mono.just(Info.SUCCESS(jsonObject));
    }

    @ApiOperation(value = "推送报告", notes="推送报告")
    @PostMapping("/imGetPushReport")
    public Mono<Info> imGetPushReport(@RequestParam("taskIds") String taskIds) {
        Map<String, Object> requestBody = ImmutableMap.of("TaskIds", taskIds);
        JSONObject jsonObject= restTemplate.postForObject(baseUrl+push_+config(),requestBody, JSONObject.class);
        return Mono.just(Info.SUCCESS(jsonObject));
    }

    @Autowired
    private NoticeRepository noticeRepository;

    @ApiOperation(value = "通知", notes="通知")
    @PostMapping("/notice")
    public Mono<Info> notice(Principal principal) {
        Map<String,List<Map<String,String>>> data=new HashMap<>();
        List sys=new ArrayList<>();
        List msg=new ArrayList<>();
        noticeRepository.findByUserphoneAndTypeAndStatus(principal.getName(),0,0).forEach(v->{
            v.setStatus(1);
            sys.add(noticeRepository.saveAndFlush(v));
        });
        noticeRepository.findByUserphoneAndTypeAndStatus(principal.getName(),1,0).forEach(v->{
            v.setStatus(1);
            msg.add(noticeRepository.saveAndFlush(v));
        });
        data.put("sys",sys);
        data.put("msg",msg);
        return Mono.just(Info.SUCCESS(data));
    }

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private CallCountRepository callCountRepository;


    @ApiOperation(value = "联系", notes="联系 0需求 \n" +
            "1社区\n" +
            "2案例\n" +
            "3图库\n" +
            "4企业主页")
    @PostMapping(value = "/call")
    public Mono<Info> call(Principal principal,@RequestParam(name = "usercode")String usercode,@RequestParam(name = "type")String type,@RequestParam("shopcode")String shopcode) {
        try {
            CallCount call=new CallCount();
            UserSecurity userinfo= userSecurityService.findByPhone(principal.getName());
            call.setUsercode(userinfo.getCode());
            call.setUsername(userinfo.getUserBasic().getNikename());
            call.setUserhredimg(userinfo.getUserBasic().getHeadportrait());
            call.setUsertype(userinfo.getType().toString());
            UserSecurity calluserinfo= userSecurityService.findByCode(usercode);
            call.setCallusercode(calluserinfo.getCode());
            call.setCallusername(calluserinfo.getUserBasic().getNikename());
            call.setCalluserhredimg(calluserinfo.getUserBasic().getHeadportrait());
            call.setCallusertype(calluserinfo.getType().toString());
            call.setType(type);
            call.setShopcode(shopcode);
            return Mono.just(Info.SUCCESS(callCountRepository.saveAndFlush(call)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @Autowired
    private CallPhonehisRepository callPhonehisRepository;


    @PostMapping(value = "callhis")
    public Mono<Info> callhis(Principal principal,@RequestParam("usercode")String usercoce,@RequestParam("shopcode")String shopcode){
        CallPhonehis callPhonehis=new CallPhonehis();
        callPhonehis.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        callPhonehis.setCallUserSecurity(userSecurityService.findByCode(usercoce));
        callPhonehis.setShopcode(shopcode);
        return Mono.just(Info.SUCCESS(callPhonehisRepository.saveAndFlush(callPhonehis)));
    }

    @GetMapping("/push/{toUserId}")
    public  Mono<Info> pushToWeb(@RequestParam("message")String message, @PathVariable String toUserId) throws IOException {
        WebSocketServer.sendInfo(message,toUserId);
        return  Mono.just(Info.SUCCESS("ok"));
    }






}
