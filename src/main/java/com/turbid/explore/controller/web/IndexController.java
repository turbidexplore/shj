package com.turbid.explore.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
        return "system/auth";
    }

    @RequestMapping("/manageindex")
    public String manageindex(){
        return "manage/index";
    }

    @RequestMapping("/managecontent")
    public String content(){
        return "system/content";
    }

    @RequestMapping("/manageeditcontent")
    public String editcontent(@RequestParam("code")String code, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletRequest.setAttribute("code",code);
        return "system/content";
    }

    @RequestMapping("/manageeditcase")
    public String editcase(@RequestParam("code")String code, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletRequest.setAttribute("code",code);
        return "manage/case";
    }

    @RequestMapping("/managenativecontent")
    public String nativecontent(){
        return "system/nativecontent";
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

    @RequestMapping("/manageeditbrand")
    public String editbrand(@RequestParam("code")String code, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletRequest.setAttribute("code",code);
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
        return "manage/managecount";
    }

    @RequestMapping("/managecomments")
    public String comments(){
        return "manage/comments";
    }

    @RequestMapping("/managestudysgroup")
    public String studysgroup(){
        return "system/studysgroup";
    }

    @RequestMapping("/managestudygroup")
    public String studygroup(){
        return "system/studygroup";
    }

    @RequestMapping("/managestudys")
    public String studys(@RequestParam("code")String code, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletRequest.setAttribute("code",code);
        return "system/studys";
    }

    @RequestMapping("/managestudy")
    public String study(@RequestParam("code")String code, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletRequest.setAttribute("code",code);
        return "system/study";
    }

    @RequestMapping("/managebanners")
    public String managebanners(){
        return "system/banners";
    }

    @RequestMapping("/managebanner")
    public String managebanner(){
        return "system/banner";
    }

    @RequestMapping("/managecases")
    public String managecases(){
        return "manage/cases";
    }

    @RequestMapping("/managecase")
    public String managecase(){
        return "manage/case";
    }

    @RequestMapping("/manageuser")
    public String manageuser(){
        return "system/usermanage";
    }

    @RequestMapping("/manageshopfans")
    public String manageshopfans(){
        return "manage/shopfans";
    }

    @RequestMapping("/manageintegralgoods")
    public String manageintegralgoods(){
        return "system/integralgoods";
    }

    @RequestMapping("/manageintegralgood")
    public String manageintegralgood(){
        return "system/integralgood";
    }

    @RequestMapping("/manageshopingmanage")
    public String manageshopingmanage(){
        return "system/shopingmanage";
    }

    @RequestMapping("/managehtindex")
    public String managehtindex(){
        return "system/index";
    }

    @RequestMapping("/manageuserinfos")
    public String manageuserinfos(){
        return "system/userinfos";
    }

    @RequestMapping("/managescaseauth")
    public String managescaseauth(){
        return "system/cases";
    }

    @RequestMapping("/managescaseinfo")
    public String managescaseinfo(@RequestParam("code")String code, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        httpServletRequest.setAttribute("code",code);
        return "system/case";
    }

}
