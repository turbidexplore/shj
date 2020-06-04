package com.turbid.explore.repository;

import com.turbid.explore.pojo.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,String> {


    List<Notice> findByUserphoneAndTypeAndStatus(String userphone,Integer type,Integer status);

    @Modifying
    @Query("update Notice o set o.userphone=:phone where o.userphone=:oldphone")
    int updatephone(@Param("oldphone") String oldphone, @Param("phone") String phone);
}
