package com.turbid.explore.service;

import com.turbid.explore.pojo.Banner;
import org.springframework.stereotype.Service;

@Service
public interface BannerService {
    Banner save(Banner banner);
}
