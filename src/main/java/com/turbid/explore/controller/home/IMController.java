package com.turbid.explore.controller.home;

import com.alibaba.fastjson.JSONObject;
import com.turbid.explore.tools.CodeLib;
import com.turbid.explore.tools.Info;
import com.turbid.explore.tools.TLSSigAPIv2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@Api(description = "即时通讯接口")
@RestController
@RequestMapping("/im")
@CrossOrigin
public class IMController {

    @Autowired
    private RestTemplate restTemplate;

    private String baseUrl="https://console.tim.qq.com/";

    private String account_import_url="v4/im_open_login_svc/account_import";

    private long appid=1400334582;

    private String key="72d736f1307d531aeb773c80f55a891a2c30a360a4a9d3841deb893c2cb54551";

    @ApiOperation(value = "导入账号", notes="导入账号")
    @PutMapping("/account_import")
    public Mono<Info> account_import(@Param("identifier") String identifier) {
        JSONObject jsonObject= restTemplate.getForObject(baseUrl+account_import_url+
               "?sdkappid="+appid+"&identifier="+identifier+"&usersig="+new TLSSigAPIv2(appid,key).genSig(identifier,68000000)
               +"&random="+CodeLib.randomCode(32,1)+"&contenttype=json", JSONObject.class);
        return Mono.just(Info.SUCCESS(jsonObject));
    }
}
