package com.turbid.explore.repository;

import com.turbid.explore.pojo.Configinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiginfoRepository extends JpaRepository<Configinfo,Integer> {
}
