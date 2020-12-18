package com.turbid.explore.repository;

import com.turbid.explore.pojo.DayTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DayTaskReposity extends JpaRepository<DayTask,String> {

    @Query("SELECT d from DayTask d where d.userSecurity.phonenumber=:usercode and d.time =:time ")
    DayTask findByDay(@Param("usercode")String usercode,@Param("time")String time);
}
