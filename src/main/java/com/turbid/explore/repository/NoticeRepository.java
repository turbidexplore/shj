package com.turbid.explore.repository;

import com.turbid.explore.pojo.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,String> {


    List<Notice> findByUserphoneAndTypeAndStatus(String userphone,Integer type,Integer status);
}
