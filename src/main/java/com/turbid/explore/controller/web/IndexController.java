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

    @RequestMapping("/manageindex")
    public String manageindex(){
        return "manage/index";
    }

    @RequestMapping("/managecontent")
    public String content(){
        return "manage/content";
    }

    @RequestMapping("/managenativecontent")
    public String nativecontent(){
        return "manage/nativecontent";
    }

    @RequestMapping("/manageusercenter")
    public String usercenter(){
        return "manage/usercenter";
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

    @RequestMapping("/managetables")
    public String tables(){
        return "manage/tables";
    }

    @RequestMapping("/managebrands")
    public String brands(){
        return "manage/brands";
    }

    @RequestMapping("/managebrand")
    public String brand(){
        return "manage/brand";
    }

    @RequestMapping("/managegoods")
    public String goods(){
        return "manage/goods";
    }

    @RequestMapping("/managegood")
    public String good(){
        return "manage/good";
    }

    @RequestMapping("/managecount")
    public String count(){
        return "manage/count";
    }

    @RequestMapping("/managecases")
    public String cases(){
        return "manage/count";
    }
    @RequestMapping("/managecomments")
    public String comments(){
        return "manage/count";
    }

}
