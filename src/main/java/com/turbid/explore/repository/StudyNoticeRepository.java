package com.turbid.explore.repository;

import com.turbid.explore.pojo.StudyNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyNoticeRepository extends JpaRepository<StudyNotice,String> {

    @Query("select a from StudyNotice a order by a.create_time desc ")
    List<StudyNotice> find();
}
