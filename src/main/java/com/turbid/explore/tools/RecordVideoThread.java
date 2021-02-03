package com.turbid.explore.tools;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.turbid.explore.configuration.AsyncTaskA;
import com.turbid.explore.pojo.LiveInfo;
import com.turbid.explore.repository.LiveInfoReposity;
import lombok.SneakyThrows;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;


/**
 * Description: 直播流保存本地线程
 *
 * @author ZhuZiKai
 * @date 2020/8/5 0005
 */
@Component
public class RecordVideoThread implements Runnable {

    public String streamURL;// 流地址（测试可以用obs推流）
    public String filePath;// 文件路径
    public Long timesSec = 1000000000L;// 停止录制时长 0为不限制时长
    public String fileFormat = "flv";//录制的文件格式
    public boolean isAudio = true;//是否录制声音
    public String code;


    public void setStreamURL(String streamURL) {
        this.streamURL = streamURL;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static void main(String[] args) {
//        "E:/video/one.flv"
//        "rtmp://192.168.1.224:1935/live/home"
        RecordVideoThread recordVideoThread = new RecordVideoThread();
        recordVideoThread.filePath = "/Users/anzhang/Downloads/one.flv";
        recordVideoThread.streamURL = "rtmp://v.anoax.com/live/test";// 最好设置结束时长 如直接停止程序会造成输出文件的损坏无法正常播放
        new Thread(recordVideoThread).start();
//        RecordVideoThread thread = new RecordVideoThread();
//        thread.pushVideoAsRTSP(1, null);
    }


    @SneakyThrows
    @Override
    public void run() {
        Thread.sleep(10000);
        System.out.println(streamURL);
        // 获取视频源
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(streamURL);
        FFmpegFrameRecorder recorder = null;
        try {
            grabber.start();
            Frame frame = grabber.grabFrame();
            createFile();
            //"rtmp://192.168.1.225:1935/live/home"
            // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
            recorder = new FFmpegFrameRecorder(filePath, 1280, 720, isAudio ? 1 : 0);
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);// 直播流格式
            recorder.setFormat(fileFormat);// 录制的视频格式
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

        }
    }

    private void createFile() {
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