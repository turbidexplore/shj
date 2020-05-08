package com.turbid.explore.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexController {

    @RequestMapping("/")
    public String home(){
        return "index";
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/aboutus")
    public String  aboutus(){
        return "about-us";
    }

    @RequestMapping("/contact")
    public String  contact(){
        return "contact";
    }

    @RequestMapping("/projects")
    public String  projects(){
        return "projects";
    }

    @RequestMapping("/project-details")
    public String  projectdetails(){
        return "project-details";
    }

    @RequestMapping("/needs")
    public String  needs(){
        return "needs";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/auth")
    public String auth(){
        return "manage/auth";
    }

    @RequestMapping("/manage/index")
    public String manageindex(){
        return "manage/index";
    }

    @RequestMapping("/manage/content")
    public String content(){
        return "manage/content";
    }

    @RequestMapping("/manage/nativecontent")
    public String nativecontent(){
        return "/manage/nativecontent";
    }

    @RequestMapping("/manage/usercenter")
    public String usercenter(){
        return "/manage/usercenter";
    }

    @RequestMapping("/privacyprotocol")
    public String privacyprotocol(){
        return "privacyprotocol";
    }

    @RequestMapping("/disclaimer")
    public String disclaimer(){
        return "disclaimer";
    }

    @RequestMapping("/business")
    public String business(){
        return "business";
    }

    @RequestMapping("/manage/tables")
    public String tables(){
        return "/manage/tables";
    }

    @RequestMapping("/manage/brands")
    public String brands(){
        return "/manage/brands";
    }

    @RequestMapping("/manage/brand")
    public String brand(){
        return "/manage/brand";
    }

    @RequestMapping("/manage/goods")
    public String goods(){
        return "/manage/goods";
    }

    @RequestMapping("/manage/good")
    public String good(){
        return "/manage/good";
    }

    @RequestMapping("/manage/count")
    public String count(){
        return "/manage/count";
    }

    @RequestMapping("/manage/cases")
    public String cases(){
        return "/manage/count";
    }
    @RequestMapping("/manage/comments")
    public String comments(){
        return "/manage/count";
    }

}
