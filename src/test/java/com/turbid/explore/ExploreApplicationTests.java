package com.turbid.explore;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.turbid.explore.pojo.DayTask;
import com.turbid.explore.push.api.client.push.PushV3Client;
import com.turbid.explore.repository.DayTaskReposity;
import com.turbid.explore.repository.UserSecurityRepository;
import com.turbid.explore.service.NeedsRelationService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.tools.TLSSigAPIv2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExploreApplicationTests {

    @Autowired
    private UserSecurityRepository userSecurityRepository;

    private String baseUrl="https://console.tim.qq.com/";
    private String portrait_set="v4/profile/portrait_set";
    private long appid=1400334582;

    public String config(){
        return "?sdkappid="+appid+"&identifier=administrator"+"&usersig="+ TLSSigAPIv2.genSig("administrator",680000000)
                +"&random="+ UUID.randomUUID().toString().replace("-", "").toLowerCase()+"&contenttype=json";
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    DayTaskReposity dayTaskReposity;

    @Autowired
    UserSecurityService userSecurityService;

    @Test
    public void contextLoads() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        DayTask dayTask=  dayTaskReposity.findByDay("17312460001",dateStr);
        if(null==dayTask){
            dayTask=new DayTask();
            System.out.println(dayTask.getCreate_time());
        }
        dayTask.setUserSecurity(userSecurityService.findByPhone("17312460001"));
        dayTask=dayTaskReposity.saveAndFlush(dayTask);
        System.out.println(dayTask.getCreate_time());
    }





}
