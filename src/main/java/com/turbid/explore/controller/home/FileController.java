package com.turbid.explore.controller.home;

import com.turbid.explore.service.file.FileService;
import com.turbid.explore.utils.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping(value = "/upload")
    public Mono<Info> images(@RequestParam("file") MultipartFile multipartFile) {
        try {
            return Mono.just(Info.SUCCESS(fileService.images(multipartFile,"public")));
        } catch (IOException e) {
            return Mono.just(Info.ERROR(e.getMessage()));
        }
    }
}
