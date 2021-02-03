package com.turbid.explore.configuration;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.turbid.explore.pojo.*;
import com.turbid.explore.push.api.client.push.PushV3Client;
import com.turbid.explore.repository.CommunityReposity;
import com.turbid.explore.repository.DiscussRepository;
import com.turbid.explore.repository.LiveInfoReposity;
import com.turbid.explore.repository.ProductReposity;
import com.turbid.explore.service.UserSecurityService;
import com.turbid.explore.tools.TLSSigAPIv2;
import lombok.SneakyThrows;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class AsyncTaskA {

    private String baseUrl="https://console.tim.qq.com/";;
    @Autowired
    private LiveInfoReposity liveInfoReposity;
    @Autowired
    private RestTemplate restTemplate;

    private long appid=1400475685;

    public String config(){
        return "?sdkappid="+appid+"&identifier=administrator"+"&usersig="+ TLSSigAPIv2.genSig("administrator",680000000)
                +"&random="+ UUID.randomUUID().toString().replace("-", "").toLowerCase()+"&contenttype=json";
    }

    private String account_import_url="v4/im_open_login_svc/account_import";


    @Async(value = "asyncService")
    public void checkOrderStatus(String id)  {

        JSONArray data =new JSONArray();
        JSONObject item=new JSONObject();
        item.put("UserID",id);
        data.add(item);

        Map<String, Object> requestBody = ImmutableMap.of(
                "DeleteItem", data
        );
        JSONObject jsonObject= restTemplate.postForObject(baseUrl+"v4/im_open_login_svc/account_delete"+config()
                ,requestBody, JSONObject.class);
        UserSecurity userSecurity=userSecurityService.findByPhone(id);
       requestBody = ImmutableMap.of(
                "Identifier", userSecurity.getCode(),
                "Nick", userSecurity.getUserBasic().getNikename(),
                "FaceUrl",userSecurity.getUserBasic().getHeadportrait());
         jsonObject= restTemplate.postForObject(baseUrl+account_import_url+config()
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

    @Autowired
    private ProductReposity productReposity;

    @Async(value = "asyncService")
    public void pushdiscuss(Discuss discuss)  {


        Community community=communityReposity.findByCode(discuss.getCommunityCode());
        if(null!=community) {
            UserSecurity userSecurity=userSecurityService.findByPhone(community.getUserSecurity().getCode());
            PushV3Client.pushByAliasa(discuss.getCode(), "您有一条新的评论，快去看看吧", discuss.getContent() + " 详情>>", "code", community.getCode(), userSecurity.getPhonenumber(),"shehuijia://com.shehuijia.explore/communitydemand");
        }else {
            try {
                community=communityReposity.findByCode(discussRepository.findByCode(discuss.getCommunityCode()).getCommunityCode());
                UserSecurity userSecurity=userSecurityService.findByPhone(community.getUserSecurity().getCode());
                PushV3Client.pushByAliasa(discuss.getCode(), "您有一条新的回复，快去看看吧", discuss.getContent() + " 详情>>", "code", community.getCode(),  userSecurity.getPhonenumber(),"shehuijia://com.shehuijia.explore/communitydemand");

            }catch (Exception e){}
        }
    }

    @Async(value = "asyncService")
    public void addgroup(StudyGroup studyGroup)  {
        PushV3Client.pushAll(studyGroup.getCode(), "课程上新了!赶紧来看看吧 (*^_^*)", studyGroup.getTitle() + " 详情>>", "code", studyGroup.getCode(),"shehuijia://com.shehuijia.explore/course");

    }



    @SneakyThrows
    @Async(value = "asyncService")
    @Transactional
    public void live(String code) {

        String filePath = "D:/nginx/video/"+code+".flv";
        String streamURL = "rtmp://v.anoax.com/live/"+code;// 最好设置结束时长 如直接停止程序会造成输出文件的损坏无法正常播放
        LiveInfo liveInfo=liveInfoReposity.getOne(code);
        PushV3Client.pushAll(UUID.randomUUID().toString().replace("-",""), liveInfo.getUserSecurity().getUserBasic().getNikename()+" 开播了!赶紧来看看吧 (*^_^*)", liveInfo.getTitle() + " 详情>>", "code", liveInfo.getCode(),"shehuijia://com.shehuijia.explore/live");
        int width=1440;
        int height=900;
        if(liveInfo.getLivetype()==0){
             width=1080;
             height=1920;
        }
        Long timesSec = 1000000000L;
        Thread.sleep(10000);
        System.out.println(streamURL);
        // 获取视频源
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(streamURL);
        FFmpegFrameRecorder recorder = null;
        try {
            grabber.start();
            Frame frame = grabber.grabFrame();
            createFile(filePath);
            //"rtmp://192.168.1.225:1935/live/home"
            // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
            recorder = new FFmpegFrameRecorder(filePath, width, height, true ? 1 : 0);
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);// 直播流格式
            recorder.setFormat("flv");// 录制的视频格式
            recorder.setFrameRate(25);// 帧数
            //百度翻译的比特率，默认400000，但是我400000贼模糊，调成800000比较合适
            recorder.setVideoBitrate(800000);
            recorder.start();
            System.out.println("推流开始");
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    RecordVideoThread thread = new RecordVideoThread();
//                    thread.pushVideoAsRTSP(1, null);
//                }
//            }).start();
            // 计算结束时间
            long endTime = System.currentTimeMillis() + timesSec * 1000;
            // 如果没有到录制结束时间并且获取到了下一帧则继续录制
            while ((System.currentTimeMillis() < endTime) && (frame != null)) {
                System.out.println("开始");
                recorder.record(frame);//录制
                frame = grabber.grabFrame();//获取下一帧
                System.out.println("结束");
                 liveInfo=liveInfoReposity.getOne(code);
            }
            recorder.record(frame);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //停止录制
            if (null != grabber) {
                try {
                    grabber.stop();
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
            if (recorder != null) {
                try {
                    recorder.stop();
                } catch (FrameRecorder.Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("录制完成，录制时长：" + timesSec + "秒");
            liveInfo=liveInfoReposity.getOne(code);
            liveInfo.setType(3);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = sdf.format(new Date());
            liveInfo.setLiveendtime(dateStr);
            try {
                Map<String, Object> requestBody = ImmutableMap.of(
                        "GroupId", liveInfo.getGroupid()
                );
                JSONObject jsonObject= restTemplate.postForObject(baseUrl+"v4/group_open_http_svc/destroy_group"+config(),requestBody, JSONObject.class);
            }catch (Exception e){
            }
            liveInfo.setUrl("http://v.deslibs.com/"+code+".flv");
            liveInfoReposity.saveAndFlush(liveInfo);
        }
    }

    private void createFile(String filePath) {
        File outFile = new File(filePath);
        if (filePath.isEmpty() || !outFile.exists() || outFile.isFile()) {
            try {
                outFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }




}
