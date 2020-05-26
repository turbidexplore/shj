package com.turbid.explore.repository;

import com.turbid.explore.pojo.ProjectNeeds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import javax.persistence.QueryHint;

@Repository
public interface ProjectNeedsRepositroy extends JpaRepository<ProjectNeeds,String> {

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from ProjectNeeds n where n.type=:type and n.status=0 and (n.style like %:style% or :style is null ) and (n.category like %:category% or :category is null) ")
    Page<ProjectNeeds> listByPage(Pageable pageable, @Param("style") String style, @Param("category") String category, @Param("type") String type);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from ProjectNeeds n where n.userSecurity.phonenumber=:name and n.status=:status  ")
    Page<ProjectNeeds> getMyNeeds(Pageable pageable, @Param("name") String name,@Param("status") Integer status);

    @Query("SELECT count (n) from ProjectNeeds n where n.userSecurity.phonenumber=:name and n.status=:status  ")
    int countByStatus( @Param("name")String name, @Param("status") int status);

    @Modifying
    @Query("update ProjectNeeds p set p.isurgent=1 where p.orderno=:orderno")
    int updateURGENT( @Param("orderno")String orderno);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from ProjectNeeds n ")
    Page<ProjectNeeds> newneeds(Pageable pageable);

    @Query("SELECT n from ProjectNeeds n where n.orderno=:orderno  ")
    ProjectNeeds getByOrder( @Param("orderno")String orderno);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from ProjectNeeds n where n.title LIKE CONCAT(:text,'%') or n.type LIKE CONCAT(:text,'%') or n.category  LIKE CONCAT(:text,'%') or n.style LIKE CONCAT(:text,'%') ")
    Page<ProjectNeeds> search(Pageable pageable,@Param("text") String text);

    @Modifying
    @Query("update ProjectNeeds p set p.status=1 where p.overtime>:time")
    int colseOverNeeds(@Param("time") String time);
}
