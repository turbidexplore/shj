package com.turbid.explore.controller.home;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.turbid.explore.configuration.AsyncTaskA;
import com.turbid.explore.pojo.LiveInfo;
import com.turbid.explore.pojo.LiveRoomUrl;
import com.turbid.explore.pojo.Shop;
import com.turbid.explore.pojo.UserSecurity;
import com.turbid.explore.repository.LiveInfoReposity;
import com.turbid.explore.repository.LiveRoomUrlReposity;
import com.turbid.explore.repository.ShopRepositroy;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.sokect.WebSocketServer;
import com.turbid.explore.tools.Info;
import com.turbid.explore.tools.TLSSigAPIv2;
import io.swagger.annotations.Api;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(description = "直播")
@RestController
@RequestMapping("/live")
@CrossOrigin
public class LiveController {

    @Autowired
    private LiveInfoReposity liveInfoReposity;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private RestTemplate restTemplate;

    private String baseUrl="https://console.tim.qq.com/";

    private long appid=1400475685;


    public String config(){
        return "?sdkappid="+appid+"&identifier=administrator"+"&usersig="+ TLSSigAPIv2.genSig("administrator",680000000)
                +"&random="+ UUID.randomUUID().toString().replace("-", "").toLowerCase()+"&contenttype=json";
    }

    public JSONObject createGroup(String usercode,String title){
        Map<String, Object> requestBody = ImmutableMap.of(
                "Owner_Account", usercode,
                "Type","AVChatRoom",
                "Name",title
        );
        JSONObject jsonObject= restTemplate.postForObject(baseUrl+"v4/group_open_http_svc/create_group"+config()
                ,requestBody, JSONObject.class);
        System.out.println(jsonObject.toJSONString());
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

    @PostMapping("/checklive")
    public Mono<Info> checklive(Principal principal) {

        List<LiveInfo> liveInfos=liveInfoReposity.findByUser(principal.getName());
        if(liveInfos.size()==1){
            return Mono.just(Info.SUCCESS(liveInfos.get(0)));
        }else {
            return Mono.just(Info.SUCCESS(new LiveInfo()));
        }


    }

    @PostMapping("/livelist")
    public Mono<Info> livelist() {
        return Mono.just(Info.SUCCESS(liveInfoReposity.livelist()));
    }

    @PostMapping("/livebacklist")
    public Mono<Info> livebacklist(@RequestParam("page")Integer page) {
        Pageable pageable = new PageRequest(page, 15, Sort.Direction.DESC, "create_time");
        return Mono.just(Info.SUCCESS(liveInfoReposity.livebacklist(pageable).getContent()));
    }

    @PostMapping("/mylives")
    public Mono<Info> mylives(@RequestParam("usercode")String usercode,@RequestParam("page")Integer page) {
        Pageable pageable = new PageRequest(page, 15, Sort.Direction.DESC, "create_time");
        return Mono.just(Info.SUCCESS(liveInfoReposity.mylives(pageable,usercode).getContent()));
    }

    @PostMapping("/liveByCode")
    public Mono<Info> liveByCode(@RequestParam("code")String code) {
        return Mono.just(Info.SUCCESS(liveInfoReposity.getOne(code)));
    }

    @Autowired
    private AsyncTaskA asyncTaskA;

    @PostMapping("/livestart")
    public Mono<Info> livestart(@RequestParam("code") String code,@RequestParam(value = "livetype",required = false)Integer livetype) {
        LiveInfo liveInfo=liveInfoReposity.getOne(code);
        if(liveInfo.getType()==2){
            //liveInfo.setGroupid("@TGS#aSTN2J5G4");
            liveInfo.setGroupid(createGroup(liveInfo.getUserSecurity().getCode(),liveInfo.getTitle()).getString("GroupId"));
        }
        if(null!=livetype){
            liveInfo.setLivetype(livetype);
        }
        liveInfo.setType(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(new Date());
        liveInfo.setLivebegintime(dateStr);
        asyncTaskA.live(liveInfo.getCode());
        return Mono.just(Info.SUCCESS(liveInfoReposity.saveAndFlush(liveInfo)));
    }

    @PostMapping("/killroom")
    public Mono<Info> killroom(@RequestParam("code") String code) {
        LiveInfo liveInfo=liveInfoReposity.getOne(code);
        liveInfo.setType(4);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(new Date());
        liveInfo.setLiveendtime(dateStr);
        removeGroup(liveInfo.getGroupid());
        return Mono.just(Info.SUCCESS(liveInfoReposity.saveAndFlush(liveInfo)));
    }

    @PostMapping("/liveover")
    public Mono<Info> liveover(@RequestParam("code") String code) {
        LiveInfo liveInfo=liveInfoReposity.getOne(code);
        liveInfo.setType(3);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(new Date());
        liveInfo.setLiveendtime(dateStr);
       removeGroup(liveInfo.getGroupid());
        return Mono.just(Info.SUCCESS(liveInfoReposity.saveAndFlush(liveInfo)));
    }

    @PutMapping("/live")
    public Mono<Info> live(Principal principal, @RequestBody LiveInfo liveInfo) {
        liveInfo.setSeecount(0);
        liveInfo.setStarcount(0);
        liveInfo.setChatcount(0);
        liveInfo.setBackcount(0);
        liveInfo.setUserSecurity(userSecurityService.findByPhone(principal.getName()));
        liveInfo.setShop(shopRepositroy.getOne(liveInfo.getUserSecurity().getShopcode()));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.WEEK_OF_MONTH, 1); //把日期往后增加一周，负数减一周
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        liveInfo.setPushurl("rtmp://127218.livepush.myqcloud.com/live/");
        switch (liveInfo.getType()){
           case 0:
               liveInfo=liveInfoReposity.saveAndFlush(liveInfo);
               if(liveInfo.getLivetype()==1){
                   liveInfo.setType(2);
               }else {

                   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   String dateStr = sdf.format(new Date());
                   liveInfo.setLivebegintime(dateStr);
//                   liveInfo.setGroupid("@TGS#aSTN2J5G4");
                   liveInfo.setGroupid(createGroup(liveInfo.getUserSecurity().getCode(),liveInfo.getTitle()).getString("GroupId"));
                   asyncTaskA.live(liveInfo.getCode());
               }
               liveInfo.setPullurl("rtmp://v.anoax.com/live/"+liveInfo.getCode());

               break;
           case 1:
               liveInfo=liveInfoReposity.saveAndFlush(liveInfo);
               liveInfo.setPullurl("rtmp://v.anoax.com/live/"+liveInfo.getCode());
               //liveInfo.setGroupid("@TGS#aSTN2J5G4");
               liveInfo.setGroupid(createGroup(liveInfo.getUserSecurity().getCode(),liveInfo.getTitle()).getString("GroupId"));

               break;
       }
        liveInfo.setPushkey(liveInfo.getCode()
               // +"/"+getSafeUrl("9faecd751d94b5bd93123083df53df85",liveInfo.getCode() , calendar.getInstance().getTimeInMillis()/1000L)
        );
                return Mono.just(Info.SUCCESS(liveInfoReposity.saveAndFlush(liveInfo)));
    }

    public static void main(String[] args) {
        System.out.println(getSafeUrl("txrtmp", "11212122", 1469762325L));
    }

    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /*
     * KEY+ streamName + txTime
     */
    private static String getSafeUrl(String key, String streamName, long txTime) {
        String input = new StringBuilder().
                append(key).
                append(streamName).
                append(Long.toHexString(txTime).toUpperCase()).toString();

        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret  = byteArrayToHexString(
                    messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return txSecret == null ? "" :
                new StringBuilder().
                        append("txSecret=").
                        append(txSecret).
                        append("&").
                        append("txTime=").
                        append(Long.toHexString(txTime).toUpperCase()).
                        toString();
    }

    private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];

        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }

    @Autowired
    private ShopRepositroy shopRepositroy;

    @PostMapping("/istolive")
    public Mono<Info> istolive(Principal principal) {
        UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
        if(!TextUtils.isEmpty(userSecurity.getShopcode())){
          Shop shop= shopRepositroy.getOne(userSecurity.getShopcode());
          if(!TextUtils.isEmpty(shop.getCode())&&isVip(shop)&&userSecurity.getCode().equals(shop.getUserSecurity().getCode())){
              return Mono.just(Info.SUCCESS(true));
          }
        }
        return Mono.just(Info.SUCCESS(false));
    }

    @PostMapping("/canlive")
    public Mono<Info> canlive(@RequestParam("usercode")String usercode) {
        UserSecurity userSecurity=userSecurityService.findByCode(usercode);
        if(!TextUtils.isEmpty(userSecurity.getShopcode())){
            Shop shop= shopRepositroy.getOne(userSecurity.getShopcode());
            if(!TextUtils.isEmpty(shop.getCode())&&isVip(shop)&&userSecurity.getCode().equals(shop.getUserSecurity().getCode())){
                return Mono.just(Info.SUCCESS(true));
            }
        }
        return Mono.just(Info.SUCCESS(false));
    }

    public static boolean isVip(Shop data) {
        if(data == null) return false;
        if (null!=data.getMargin()&&data.getMargin() > 0) {
            return true;
        }
        if (!TextUtils.isEmpty(data.getVipday())) {
            int gapCount = getGapCount(new Date(), strToDate(data.getVipday()));
            if (gapCount > 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * string   to  date
     */
    public static Date strToDate(String string) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取两个日期之间的间隔天数
     *
     * @return
     */
    public static int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    @Transactional
    @PostMapping("/setcount")
    public Mono<Info> setcount(@RequestParam("code") String code,@RequestParam("type") Integer type) throws IOException {
        LiveInfo liveInfo=liveInfoReposity.getOne(code);
        if(type==0){
            liveInfo.setSeecount(liveInfo.getSeecount()+1);
            WebSocketServer.sendInfo(liveInfo.getSeecount()+"",code);
        }else if(type==1){
            liveInfo.setStarcount(liveInfo.getStarcount()+1);
        }else if(type==2){
            liveInfo.setChatcount(liveInfo.getChatcount()+1);
        }else if(type==3){
            liveInfo.setBackcount(liveInfo.getBackcount()+1);
        }
        return Mono.just(Info.SUCCESS(liveInfoReposity.saveAndFlush(liveInfo)));
    }


    @Autowired
    private LiveRoomUrlReposity liveRoomUrlReposity;

    @PutMapping(value = "/addliveRoomUrl")
    public Mono<Info> addliveRoomUrl(@RequestBody LiveRoomUrl liveRoomUrl)  {
        return Mono.just(Info.SUCCESS(liveRoomUrlReposity.saveAndFlush(liveRoomUrl)));
    }

    @PostMapping(value = "/getliveRoomUrl")
    public Mono<Info> getliveRoomUrl(@RequestParam("livecode") String livecode)  {
        return Mono.just(Info.SUCCESS(liveRoomUrlReposity.getliveRoomUrl(livecode)));
    }
}
