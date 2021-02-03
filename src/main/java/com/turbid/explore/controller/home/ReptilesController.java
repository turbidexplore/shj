package com.turbid.explore.controller.home;

import com.alibaba.fastjson.JSONObject;
import com.turbid.explore.tools.AutoNewsCrawler;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "爬虫")
@RestController
@RequestMapping("/reptiles")
@CrossOrigin
public class ReptilesController {

    @ApiOperation(value = "获取微信公众号资源", notes="获取微信公众号资源")
    @PostMapping("/wechat")
    public Mono<Info> get(@RequestBody String[] urls) {
        try {
            List data=new ArrayList<>();
            for (String url:
                 urls) {
                AutoNewsCrawler crawler = new AutoNewsCrawler("crawl", true,url);
                crawler.start(4);//启动爬虫
                Map<String,Object> item=new HashMap<>();
                item.put("title",crawler.getTitle());
                item.put("content",crawler.getContent());
                item.put("images",crawler.getImages());
                data.add(item);
            }
            return Mono.just(Info.SUCCESS(data));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(null));
        }
    }

    @Autowired
    RestTemplate restTemplate;

    @ApiOperation(value = "获取网站资源", notes="获取网站资源")
    @PostMapping("/info")
    public Mono<JSONObject> info(@RequestParam("page")Integer page) {
        try {

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("start", String.valueOf(page*15));
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, null);
            JSONObject jsonObject= restTemplate.postForObject("http://new.rushi.net/Home/Index/worksMore.html"
                    ,entity, JSONObject.class);
            return Mono.just(jsonObject);
        }catch (Exception e){
            return Mono.just(null);
        }
    }




}
