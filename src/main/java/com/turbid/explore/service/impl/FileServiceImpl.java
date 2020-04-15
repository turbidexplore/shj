package com.turbid.explore.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.turbid.explore.configuration.TencentCOSConfig;
import com.turbid.explore.pojo.FileGroup;
import com.turbid.explore.pojo.FileInfo;
import com.turbid.explore.repository.FileGroupRepositroy;
import com.turbid.explore.repository.FileInfoRepositroy;
import com.turbid.explore.service.FileService;
import com.turbid.explore.tools.CodeLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

    private String url="https://anoax-1258088094.cos.ap-chengdu.myqcloud.com/";

    public String images(MultipartFile multipartFile,String path) throws IOException {
        if (multipartFile != null) {
            if (multipartFile.getOriginalFilename() != null || "".equals(multipartFile.getOriginalFilename())) {
                String[] name = multipartFile.getOriginalFilename().split("\\.");
                File file = File.createTempFile(CodeLib.getSHC() ,"."+ name[1]);
                multipartFile.transferTo(file);
                String bucketName =tencentOSS.QCLOUD_FILE_BUCKET;
                String key = path+"/"+file.getName();
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
                COSClient cosClient=tencentOSS.cosClient();
                PutObjectResult putObjectResult =cosClient .putObject(putObjectRequest);
                cosClient.shutdown();
                CodeLib.deleteFile(file);
                FileGroup fileGroup= new FileGroup();
                FileInfo fileInfo=new FileInfo();
                fileInfo.setSize(file.length());
                fileInfo.setType(name[1]);
                fileInfo.setFileGroup(fileGroup);
                fileInfo.setUrl(url+key);
                fileGroup.setFileInfos(new ArrayList<>());
                fileGroup.getFileInfos().add(fileInfo);
                fileGroupRepositroy.save(fileGroup);
                return url+key;
            }
        }
        return "";
    }



}
