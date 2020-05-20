package com.turbid.explore.repository;

import com.turbid.explore.pojo.StudyRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRelationRepository extends JpaRepository<StudyRelation,String> {

    @Query("select count(s) from StudyRelation s where s.phone=:name and s.studycode=:studycode and s.status=1")
    int issee(@Param("name") String name,@Param("studycode") String studycode);

    StudyRelation findByOrderno(String orderno);

    @Modifying
    @Query("update StudyRelation p set p.status=1 where p.orderno=:orderno")
    int updateSTUDY(@Param("orderno")String orderno);
}
