package com.turbid.explore.service;

import com.turbid.explore.pojo.Banner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BannerService {
    Banner save(Banner banner);

    void del(String code);

    List<Banner> listBytype(String type);
}
