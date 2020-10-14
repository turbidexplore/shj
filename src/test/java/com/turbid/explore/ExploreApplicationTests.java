package com.turbid.explore;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
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

    @Test
    public void contextLoads() {
//       userSecurityRepository.aaaaa().forEach(userSecurity->{
//               String typename = "";
//               switch (userSecurity.getType()) {
//                   case 0:
//                       typename = "设计师";
//                       break;
//                   case 1:
//                       typename = "经销商";
//                       break;
//                   case 2:
//                       typename = "工厂";
//                       break;
//                   case 3:
//                       typename = "设计公司";
//                       break;
//               }
//               Map<String, Object> requestBody = ImmutableMap.of(
//                       "Identifier", userSecurity.getCode(),
//                       "Nick", userSecurity.getUserBasic().getNikename(),
//                       "FaceUrl", userSecurity.getUserBasic().getHeadportrait());
//           System.out.println( restTemplate.postForObject(baseUrl + "v4/im_open_login_svc/account_import" + config()
//                       , requestBody, JSONObject.class));
//
//               JSONArray data = new JSONArray();
//               JSONObject item = new JSONObject();
//               item.put("Tag", "Tag_Profile_Custom_usertype");
//               item.put("Value", typename);
//               data.add(item);
//
//               requestBody = ImmutableMap.of(
//                       "From_Account", userSecurity.getCode(),
//                       "ProfileItem", data
//               );
//           System.out.println( restTemplate.postForObject(baseUrl + portrait_set + config()
//                       , requestBody, JSONObject.class));
//           System.out.println("----------------");
//
//       });
    }

}
