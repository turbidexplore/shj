package com.turbid.explore.repository;

import com.turbid.explore.pojo.LiveRoomUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiveRoomUrlReposity extends JpaRepository<LiveRoomUrl,String> {

    @Query("select l from LiveRoomUrl l where l.livecode=:livecode")
    List<LiveRoomUrl> getliveRoomUrl(@Param("livecode") String livecode);
}
