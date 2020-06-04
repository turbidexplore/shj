package com.turbid.explore.service.impl;

import com.turbid.explore.pojo.Banner;
import com.turbid.explore.repository.BannerRepository;
import com.turbid.explore.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    @Override
    public Banner save(Banner banner) {
        return bannerRepository.saveAndFlush(banner);
    }

    @Override
    public void del(String code) {
         bannerRepository.deleteById(code);
    }

    @Override
    public List<Banner> listBytype(String type) {
        return bannerRepository.findByType(type);
    }
}
