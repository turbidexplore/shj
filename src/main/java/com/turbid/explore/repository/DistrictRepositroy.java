package com.turbid.explore.repository;


import com.turbid.explore.pojo.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepositroy extends JpaRepository<District,Integer> {

    @Query("SELECT d from District d where d.pid=:id ")
    List<District> getAreaByPid(@Param("id") Integer id);
}
