package com.turbid.explore.service;

import com.turbid.explore.pojo.QaaInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QaaInfoService {
    QaaInfo save(QaaInfo qaaInfo);

    List<QaaInfo> listByPage(Integer page, String label);

    QaaInfo qaaByCode(String code);

    List<QaaInfo> listByUser(String name, Integer page);

    List<QaaInfo> search(String text, Integer page);
}
