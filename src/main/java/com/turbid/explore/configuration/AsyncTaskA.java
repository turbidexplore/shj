package com.turbid.explore.configuration;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.turbid.explore.pojo.Community;
import com.turbid.explore.pojo.Discuss;
import com.turbid.explore.pojo.StudyGroup;
import com.turbid.explore.pojo.UserSecurity;
import com.turbid.explore.push.api.client.push.PushV3Client;
import com.turbid.explore.repository.CommunityReposity;
import com.turbid.explore.repository.DiscussRepository;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.tools.TLSSigAPIv2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.util.Map;
import java.util.UUID;

@Component
public class AsyncTaskA {

    private String baseUrl="https://console.tim.qq.com/";;
    private long appid=1400334582;

    public String config(){
        return "?sdkappid="+appid+"&identifier=administrator"+"&usersig="+ TLSSigAPIv2.genSig("administrator",680000000)
                +"&random="+ UUID.randomUUID().toString().replace("-", "").toLowerCase()+"&contenttype=json";
    }

    @Autowired
    private RestTemplate restTemplate;

    @Async(value = "asyncService")
    public void checkOrderStatus(String id) throws InterruptedException {
        Thread.sleep(1000*60*120);
        JSONArray data =new JSONArray();
        JSONObject item=new JSONObject();
        item.put("UserID",id);
        data.add(item);

        Map<String, Object> requestBody = ImmutableMap.of(
                "DeleteItem", data
        );
        JSONObject jsonObject= restTemplate.postForObject(baseUrl+"v4/im_open_login_svc/account_delete"+config()
                ,requestBody, JSONObject.class);
    }

    @Async(value = "asyncService")
    public void pushcommunity(Community community)  {
        if(community.getLabel()=="产品"||community.getLabel().equals("产品")||community.getLabel()=="设计"||community.getLabel().equals("设计")) {
            PushV3Client.pushAll(community.getCode(),  "(｡･∀･)ﾉﾞ嗨  有新的"+community.getLabel()+"需求，快去看看吧", community.getContent()+" 详情>>", "code", community.getCode(),"shehuijia://com.shehuijia.explore/communitydemand");
        }
    }

    @Autowired
    private CommunityReposity communityReposity;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private DiscussRepository discussRepository;

    @Async(value = "asyncService")
    public void pushdiscuss(Discuss discuss)  {
        Community community=communityReposity.findByCode(discuss.getCommunityCode());
        if(null!=community) {
            UserSecurity userSecurity=userSecurityService.findByPhone(community.getUserSecurity().getCode());
            PushV3Client.pushByAlias(discuss.getCode(), "您有一条新的评论，快去看看吧", discuss.getContent() + " 详情>>", "code", community.getCode(), userSecurity.getPhonenumber(),"shehuijia://com.shehuijia.explore/communitydemand");
        }else {
            community=communityReposity.findByCode(discussRepository.findByCode(discuss.getCommunityCode()).getCommunityCode());
            UserSecurity userSecurity=userSecurityService.findByPhone(community.getUserSecurity().getCode());
            PushV3Client.pushByAlias(discuss.getCode(), "您有一条新的回复，快去看看吧", discuss.getContent() + " 详情>>", "code", community.getCode(),  userSecurity.getPhonenumber(),"shehuijia://com.shehuijia.explore/communitydemand");
        }
    }

    @Async(value = "asyncService")
    public void addgroup(StudyGroup studyGroup)  {
        PushV3Client.pushAll(studyGroup.getCode(), "课程上新了!赶紧来看看吧 (*^_^*)", studyGroup.getTitle() + " 详情>>", "code", studyGroup.getCode(),"shehuijia://com.shehuijia.explore/course");

    }



}
