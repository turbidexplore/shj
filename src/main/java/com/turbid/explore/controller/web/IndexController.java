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

}
