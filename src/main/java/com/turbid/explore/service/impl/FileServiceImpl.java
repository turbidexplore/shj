package com.turbid.explore.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.turbid.explore.configuration.TencentCOSConfig;
import com.turbid.explore.repository.FileGroupRepositroy;
import com.turbid.explore.repository.FileInfoRepositroy;
import com.turbid.explore.service.FileService;
import com.turbid.explore.tools.CodeLib;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    @Value("${com.turbid.upload-path.images}")
    private String	imagespath;

    @Autowired
    private FileInfoRepositroy fileInfoRepositroy;

    @Autowired
    private FileGroupRepositroy fileGroupRepositroy;

    @Autowired
    private TencentCOSConfig tencentOSS;

    private String url="http://anoax-1258088094.cos.accelerate.myqcloud.com/";

    public String images(MultipartFile multipartFile,String path) throws IOException {
        if (multipartFile != null) {
            if (multipartFile.getOriginalFilename() != null || "".equals(multipartFile.getOriginalFilename())) {
                String[] name = multipartFile.getOriginalFilename().split("\\.");
                if(name[name.length-1].equals("mp4")||name[name.length-1]=="mp4"){
                    File file = File.createTempFile(CodeLib.getSHC() ,".mp3");
                    FileUtils.copyInputStreamToFile(multipartFile.getInputStream(),file);
                    mp3(file,path);
                }
                File file = File.createTempFile(CodeLib.getSHC() ,"."+ name[name.length-1]);
                multipartFile.transferTo(file);
                String bucketName =tencentOSS.QCLOUD_FILE_BUCKET;
                String key = path+"/"+file.getName();
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
                COSClient cosClient=tencentOSS.cosClient();
                PutObjectResult putObjectResult =cosClient .putObject(putObjectRequest);
                cosClient.shutdown();
                CodeLib.deleteFile(file);
                return url+key;
            }
        }
        return "";
    }

    public String mp3(File file,String path) throws IOException {



                String bucketName =tencentOSS.QCLOUD_FILE_BUCKET;
                String key = path+"/"+file.getName();
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
                COSClient cosClient=tencentOSS.cosClient();
                PutObjectResult putObjectResult =cosClient .putObject(putObjectRequest);
                cosClient.shutdown();

                return url+key;

    }

}
