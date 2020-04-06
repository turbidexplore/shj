package com.turbid.explore.controller.home;

import com.alibaba.fastjson.JSON;
import com.turbid.explore.service.file.FileService;
import com.turbid.explore.utils.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
@Api(description = "文件接口")
@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation(value = "文件上传", notes="文件上传")
    @PostMapping(value = "/upload")
    public Mono<Info> images(@RequestParam("file") MultipartFile multipartFile) {
        try {
            return Mono.just(Info.SUCCESS(fileService.images(multipartFile,"public")));
        } catch (IOException e) {
            return Mono.just(Info.ERROR(e.getMessage()));
        }
    }

    @PostMapping(value = "pimage")
    public String uploadPicture(HttpServletRequest request,HttpServletResponse response) {
        try {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            Map<String, String> reMap = new HashMap<String, String>();
            while (iter.hasNext()) {
                // 记录上传过程起始时的时间，用来计算上传时间
                // int pre = (int) System.currentTimeMillis();
                // 取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    //必须返回这样格式的json字符串
                    reMap.put("uploaded", "1");
                    reMap.put("url", fileService.images(file, "public"));
                }
            }
            return JSON.toJSONString(reMap);
        } catch (Exception e) {
            return "false";
        }
    }

    @PostMapping(value = "/image")
    public void image(HttpServletRequest request,HttpServletResponse response) {
        try {
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                // 取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    // 记录上传过程起始时的时间，用来计算上传时间
                    // int pre = (int) System.currentTimeMillis();
                    // 取得上传文件
                    MultipartFile file = multiRequest.getFile(iter.next());
                    if (file != null) {
                        String imageContextPath = fileService.images(file, "public");
                        response.setContentType("text/html;charset=UTF-8");
                        String callback = request.getParameter("CKEditorFuncNum");
                        PrintWriter out = response.getWriter();
                        out.println("<script type=\"text/javascript\">");
                        out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + imageContextPath + "',''" + ")");
                        out.println("</script>");
                        out.flush();
                        out.close();
                    }
            }
        } catch (Exception e) {

        }
    }
}
