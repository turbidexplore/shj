package com.turbid.explore.service.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileService {

     public String images(MultipartFile multipartFile,String path) throws IOException;

}
