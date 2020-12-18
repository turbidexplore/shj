package com.turbid.explore.service;

import com.turbid.explore.pojo.NativeContent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface NativeContentService {
    NativeContent save(NativeContent nativeContent);

    List<NativeContent> listByPageLabel(Integer page,  String label,String from);

    List<NativeContent> listByPage(Integer page,String username);

    NativeContent newsByCode(String code);

    List<NativeContent> search(String text, Integer page);

    void del(String code);

    List<NativeContent> allbypage(Integer page, String freesubject, String subject, Integer abroad, Integer isshop,String label);
}
