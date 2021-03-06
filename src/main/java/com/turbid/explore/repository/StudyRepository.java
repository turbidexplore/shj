package com.turbid.explore.repository;

import com.turbid.explore.pojo.Study;

import com.turbid.explore.pojo.StudyGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<Study,String> {

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT new Study (s.code,s.create_time,s.indeximgurl,s.seecount,s.price,s.pricetype,s.shb,s.title,s.type,s.studyGroup,s.videourl) from Study s where (s.type =:style or :style is null ) and s.status is null and s.studyGroup.code=:code")
    Page<Study> listByPage(Pageable pageable,@Param("style") String style,@Param("code") String code);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT s.studyGroup from Study s where s.status is null and s.studyGroup.code is not null group by s.studyGroup.code")
    Page<StudyGroup> hatstudyByPage(Pageable pageable);


    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT new Study (s.code,s.create_time,s.indeximgurl,s.seecount,s.price,s.pricetype,s.shb,s.title,s.type,s.studyGroup,s.videourl) from Study s where (s.title  LIKE CONCAT('%',:text,'%') or s.type  LIKE CONCAT('%',:text,'%')) and s.status is null ")
    Page<Study> search(Pageable pageable,@Param("text") String text);

    @Query("select s from Study s where s.code in(select sr.studycode from StudyRelation sr where sr.orderno=:orderno) and s.status is null")
    Study getByOrder(@Param("orderno") String orderno);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT s from Study s where (s.type =:style or :style is null ) and s.status is null and s.studyGroup.code=:code")
    Page<Study> list(Pageable pageable,@Param("style") String style,@Param("code") String code);

    @Modifying
    @Query("update Study p set p.status=0 where p.code=:code")
    int del(@Param("code") String code);

    @Query("select sum (p.seecount) from Study p where p.studyGroup.code=:code")
    int countByGroup(@Param("code") String code);

    @Query("select p from Study p where p.studyGroup.code=:code")
    List<Study> findByGroup(@Param("code") String code);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT sg from StudyGroup sg where 0 =(select count(sg1) from  Study sg1 where sg1.pricetype='1' and sg1.studyGroup.code=sg.code)")
    Page<StudyGroup> free(Pageable pageable);
}
