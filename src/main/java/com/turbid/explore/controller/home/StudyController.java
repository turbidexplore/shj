package com.turbid.explore.controller.home;

import com.turbid.explore.configuration.AsyncTaskA;
import com.turbid.explore.pojo.*;
import com.turbid.explore.repository.*;
import com.turbid.explore.service.StudyService;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.tools.CodeLib;
import com.turbid.explore.tools.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(description = "达人研习社")
@RestController
@RequestMapping("/study")
@CrossOrigin
public class StudyController {

    @Autowired
    private StudyService studyService;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private StudtHisRepository studtHisRepository;

    @ApiOperation(value = "添加课程", notes="添加课程")
    @PutMapping("/add")
    public Mono<Info> add(@RequestBody Study study,@RequestParam("code") String code) {
        study.setSeecount(0);
        StudyGroup studyGroup=studyGroupRepository.getOne(code);
        studyGroup.setKscount(studyGroup.getKscount()+1);
        studyGroup= studyGroupRepository.saveAndFlush(studyGroup);
        study.setStudyGroup(studyGroup);
        return Mono.just(Info.SUCCESS(studyService.save(study)));
    }

    @ApiOperation(value = "添加课程", notes="添加课程")
    @PutMapping("/addnotice")
    public Mono<Info> addnotice(@RequestBody StudyNotice studyNotice) {
        return Mono.just(Info.SUCCESS(noticeRepository.saveAndFlush(studyNotice)));
    }

    @ApiOperation(value = "课程记录", notes="课程记录")
    @PutMapping("/studyhis")
    public Mono<Info> studyhis(Principal principal,@RequestParam(value = "studycode") String studycode,@RequestParam("groupcode")String groupcode) {
        Study study= studyService.get(studycode);
        study.setSeecount(study.getSeecount()+1);
        studyService.save(study);
        StudyHis studyHis= studtHisRepository.findByStudygroupCodeAndUsercode(groupcode,principal.getName());
       if(null==studyHis){
           studyHis=new StudyHis();
           studyHis.setStudyCode(studycode);
           studyHis.setStudygroupCode(groupcode);
           studyHis.setUsercode(principal.getName());
       }else {
           studyHis.setStudyCode(studycode);
           studyHis.setCreate_time(new Date());
       }
        return Mono.just(Info.SUCCESS(studtHisRepository.saveAndFlush(studyHis)));
    }

    @Autowired
    private AsyncTaskA asyncTaskA;

    @ApiOperation(value = "添加课程", notes="添加课程")
    @PutMapping("/addgroup")
    public Mono<Info> addgroup(@RequestBody StudyGroup studyGroup) {
        studyGroup.setShb(String.valueOf(Double.parseDouble(studyGroup.getPrice())*100));
        if(studyGroup.getShb().contains(".")){
            studyGroup.setShb(studyGroup.getShb().split("\\.")[0]);
        }
        studyGroup.setBalance(studyGroup.getPrice());
        studyGroup.setSeecount(0);
        studyGroup.setKscount(0);
        if(null==studyGroup.getCode()){
            asyncTaskA.addgroup(studyGroup);
        }
        studyGroup=studyGroupRepository.saveAndFlush(studyGroup);


        return Mono.just(Info.SUCCESS(studyGroup));
    }


    @ApiOperation(value = "获取课程组分页列表", notes="获取课程组分页列表")
    @PostMapping(value = "/grouplist")
    public Mono<Info> grouplist(@RequestParam(name = "page")Integer page,
                           @RequestParam(name = "style", required = false)String style) {
        try {
            Pageable pageable = new PageRequest(page,15, Sort.Direction.DESC,"create_time");
            Page<StudyGroup> pages=  studyGroupRepository.grouplist(pageable,style);
            List<StudyGroup> list=new ArrayList<>();
            pages.getContent().forEach(v->{
                try {
                    v.setSeecount(studyService.countByGroup(v.getCode()));
                }catch (Exception e){
                    v.setSeecount(0);
                }
                list.add(v);
            });
            return Mono.just(Info.SUCCESS(list ));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @ApiOperation(value = "删除", notes="删除")
    @PutMapping("/delnotice")
    public Mono<Info> delnotice(@RequestParam("code") String code) {
        noticeRepository.deleteById(code);
        return Mono.just(Info.SUCCESS(null));
    }

    @ApiOperation(value = "删除", notes="删除")
    @PutMapping("/delgroup")
    public Mono<Info> delgroup(@RequestParam("code") String code) {
       StudyGroup studyGroup= studyGroupRepository.getOne(code);
       studyGroup.setStatus(1);
        return Mono.just(Info.SUCCESS(studyGroupRepository.saveAndFlush(studyGroup)));
    }

    @ApiOperation(value = "删除", notes="删除")
    @PutMapping("/del")
    public Mono<Info> del(@RequestParam("code") String code) {
        Study study=studyService.get(code);
        StudyGroup studyGroup= study.getStudyGroup();
        studyGroup.setKscount(studyGroup.getKscount()-1);
        study.setStudyGroup(studyGroupRepository.saveAndFlush(studyGroup));

        return Mono.just(Info.SUCCESS(studyService.del(code)));
    }

    @ApiOperation(value = "获取课程详情", notes="获取课程详情")
    @GetMapping("/get")
    public Mono<Info> get(@RequestParam(name = "code")String code) {
        Study study= studyService.get(code);
        study.setSeecount(study.getSeecount()+1);
        study=studyService.save(study);
        return Mono.just(Info.SUCCESS(study));
    }

    @ApiOperation(value = "获取课程组详情", notes="获取课程组详情")
    @GetMapping("/getgroup")
    public Mono<Info> getgroup(@RequestParam(name = "code")String code) {
        StudyGroup studyGroup= studyGroupRepository.getOne(code);
        try {
            if(null==studyGroup.getSeecount()){
                studyGroup.setSeecount(0);
            }else {
                studyGroup.setSeecount( studyService.countByGroup(studyGroup.getCode()));
            }
            studyGroup=studyGroupRepository.saveAndFlush(studyGroup);
        }catch (Exception e){

        }
        return Mono.just(Info.SUCCESS(studyGroup));
    }

    @ApiOperation(value = "获取课程视频url", notes="获取课程视频url")
    @GetMapping("/getVideo")
    public Mono<Info> getVideo(@RequestParam(name = "code")String code) {
        Study study=studyService.get(code);
        Map data=new HashMap();
        data.put("audio", study.getAudiourl());
        data.put("type",study.getUrltype());
        if(study.getUrltype()==0) {
            data.put("video", study.getVideourl());
        }else {
            data.put("audio", study.getVideourl());
        }
        return Mono.just(Info.SUCCESS(data));
    }

    @ApiOperation(value = "获取课程分页列表", notes="获取课程分页列表")
    @PostMapping(value = "/studyByPage")
    public Mono<Info> studyByPage(@RequestParam(name = "page")Integer page,
                                  @RequestParam(name = "style", required = false)String style,@RequestParam(name = "code")String code) {
        try {
            return Mono.just(Info.SUCCESS(studyService.listByPage(page,style,code)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @Autowired
    private DiscussRepository discussRepository;

    @ApiOperation(value = "获取课程分页列表", notes="获取课程分页列表")
    @PostMapping(value = "/list")
    public Mono<Info> list( Principal principal,@RequestParam(name = "page",required = false)Integer page,
                                  @RequestParam(name = "style", required = false)String style,@RequestParam(name = "code")String code) {
        try {
            List<Study> list=new ArrayList<>();
            studyService.list(page,style,code).forEach(v->{
                v.setCommentcount(discussRepository.countByCommunityCode(v.getCode()));
                list.add(v);
            });
            return Mono.just(Info.SUCCESS(list));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @ApiOperation(value = "获取课程列表", notes="获取课程列表")
    @PostMapping(value = "/studylist")
    public Mono<Info> studylist(@RequestParam(name = "studycode")String studycode) {
        try {
            return Mono.just(Info.SUCCESS( studyService.studylist(studycode)));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @ApiOperation(value = "热门课程", notes="热门课程")
    @PostMapping(value = "/hatstudyByPage")
    public Mono<Info> hatstudyByPage(@RequestParam(name = "page")Integer page) {
        try {
            List<StudyGroup> list=studyService.hatstudyByPage(page);
            list.forEach(v->{
                v.setSeecount(studyService.countByGroup(v.getCode()));
            });
            return Mono.just(Info.SUCCESS(list ));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }


    @ApiOperation(value = "免费专区", notes="免费专区")
    @PostMapping(value = "/free")
    public Mono<Info> free(@RequestParam(name = "page")Integer page) {
        try {
            List<StudyGroup> list=studyService.free(page);
            list.forEach(v->{
                try {
                    v.setSeecount(studyService.countByGroup(v.getCode()));
                }catch (Exception e){
                    v.setSeecount(0);
                }
            });
            return Mono.just(Info.SUCCESS(list ));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @ApiOperation(value = "推荐", notes="推荐")
    @PostMapping(value = "/recommend")
    public Mono<Info> recommend(@RequestParam(name = "code")String code) {
        try {
            List<StudyGroup> list=studyService.hatstudyByPage(0);
            list.forEach(v->{
                v.setSeecount(studyService.countByGroup(v.getCode()));
            });
            return Mono.just(Info.SUCCESS(list ));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @Autowired
    private StudyNoticeRepository noticeRepository;

    @ApiOperation(value = "预告", notes="预告")
    @PostMapping(value = "/notice")
    public Mono<Info> notice() {
        try {
            return Mono.just(Info.SUCCESS(noticeRepository.find()));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @Autowired
    private StudyRelationRepository studyRelationRepository;

    @ApiOperation(value = "购买课程", notes="购买课程")
    @PostMapping(value = "/buyStudy")
    @Transactional
    public Mono<Info> buyStudy(Principal principal,@RequestParam("studycode")String studycode) {
        try {
            StudyRelation studyRelation=new StudyRelation();
            studyRelation.setOrderno(CodeLib.randomCode(12,1));
            studyRelation.setPhone(principal.getName());
            studyRelation.setStudycode(studycode);
            studyRelation.setStatus(0);
            return Mono.just(Info.SUCCESS(studyRelationRepository.saveAndFlush(studyRelation).getOrderno()));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }

    @Autowired
    private DayTaskReposity dayTaskReposity;

    @Autowired
    private UserSecurityService userSecurityService;

    @ApiOperation(value = "是否可看", notes="是否可看")
    @PostMapping(value = "/issee")
    public Mono<Info> issee(Principal principal,@RequestParam("studycode")String studycode) {
        try {
            if(0<studyRelationRepository.issee(principal.getName(),studycode)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = sdf.format(new Date());
                DayTask dayTask=dayTaskReposity.findByDay(principal.getName(),dateStr);
                UserSecurity userSecurity=userSecurityService.findByPhone(principal.getName());
                if(null==dayTask){
                    dayTask=new DayTask();
                }
                dayTask.setUserSecurity(userSecurity);
                dayTask.setTaskb();
                if(dayTask.getTaskb()==3){
                    userSecurity.setShb(userSecurity.getShb()+10);
                    userSecurityService.save(userSecurity);
                }
                dayTask=dayTaskReposity.saveAndFlush(dayTask);
                return Mono.just(Info.SUCCESS(true));
            }else {
                return Mono.just(Info.SUCCESS(false));
            }
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }


    @ApiOperation(value = "获取课程组分页列表", notes="获取课程组分页列表")
    @PostMapping(value = "/allgrouplist")
    public Mono<Info> allgrouplist() {
        try {
            Map data=new HashMap();
            Pageable pageable = new PageRequest(0,3, Sort.Direction.DESC,"create_time");
            Page<StudyGroup> pages=  studyGroupRepository.grouplist(pageable,"色彩风格课");
            List<StudyGroup> list=new ArrayList<>();
            pages.getContent().forEach(v->{
                try {
                    v.setSeecount(studyService.countByGroup(v.getCode()));
                }catch (Exception e){
                    v.setSeecount(0);
                }

                list.add(v);
            });
            data.put("sga",list);
            pages=  studyGroupRepository.grouplist(pageable,"意式品牌课");
            List<StudyGroup> list1=new ArrayList<>();
            pages.getContent().forEach(v->{
                try {
                    v.setSeecount(studyService.countByGroup(v.getCode()));
                }catch (Exception e){
                    v.setSeecount(0);
                }

                list1.add(v);
            });
            data.put("sgb",list1);
            pages=  studyGroupRepository.grouplist(pageable,"软装搭配课");
            List<StudyGroup> list2=new ArrayList<>();
            pages.getContent().forEach(v->{
                try {
                    v.setSeecount(studyService.countByGroup(v.getCode()));
                }catch (Exception e){
                    v.setSeecount(0);
                }
                list2.add(v);
            });
            data.put("sgc",list2);
            data.put("notice",noticeRepository.find());
            data.put("zn","<p><img src=\"http://anoax-1258088094.cos.accelerate.myqcloud.com/public/20210121085834721539586357061552.gif\" width=\"1500\" /><img  src=\"http://anoax-1258088094.cos.accelerate.myqcloud.com/public/20210121085831113976182449485450.gif\" width=\"1500\" /><img src=\"http://anoax-1258088094.cos.accelerate.myqcloud.com/public/202101210858391028371375934597717.gif\" width=\"1500\" /><img src=\"http://anoax-1258088094.cos.accelerate.myqcloud.com/public/202101210858324477066422341874702.gif\" width=\"1500\" /><img src=\"http://anoax-1258088094.cos.accelerate.myqcloud.com/public/202101210858383131302150546475591.gif\" width=\"1500\" /><img  src=\"http://anoax-1258088094.cos.accelerate.myqcloud.com/public/202101210858368713189176227394840.jpg\" width=\"1500\" /></p>\n");
            return Mono.just(Info.SUCCESS(data));
        }catch (Exception e){
            return Mono.just(Info.SUCCESS(e.getMessage()));
        }
    }
}
