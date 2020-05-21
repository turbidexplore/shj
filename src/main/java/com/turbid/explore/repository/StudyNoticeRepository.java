package com.turbid.explore.repository;

import com.turbid.explore.pojo.StudyNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyNoticeRepository extends JpaRepository<StudyNotice,String> {

}
