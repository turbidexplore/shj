package com.turbid.explore.repository;

import com.turbid.explore.pojo.ZbInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZbInfoReposity extends JpaRepository<ZbInfo,String> {
}
