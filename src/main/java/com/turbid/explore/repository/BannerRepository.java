package com.turbid.explore.repository;

import com.turbid.explore.pojo.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner,String> {

    List<Banner> findByType(String type);
}
