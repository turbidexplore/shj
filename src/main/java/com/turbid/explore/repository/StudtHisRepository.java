package com.turbid.explore.repository;

import com.turbid.explore.pojo.StudyHis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudtHisRepository extends JpaRepository<StudyHis,String> {

    StudyHis findByStudygroupCodeAndUsercode(String groupCode,String usercode);
}
