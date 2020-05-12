package com.turbid.explore.repository;

import com.turbid.explore.pojo.Case;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface CaseRepositroy extends JpaRepository<Case,String> {

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Case c where (c.subject like %:subject% or :subject is null ) and (c.label like %:label% or :label is null) ")
    Page<Case> listByPage(Pageable pageable, @Param("subject") String subject,@Param("label") String label);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Case c where c.userSecurity.phonenumber=:name ")
    Page<Case> mycases(Pageable pageable, @Param("name") String name);

    @Query("select c from Case c where c.code=:code")
    Case caseByCode(@Param("code") String code);

    @Query("select c from Case c where c.label=:label and c.subject=:subject")
    List<Case> recommend(@Param("label")String label,@Param("subject") String subject);

    @Query("select sum (c.stars.size) from Case c where  c.userSecurity.phonenumber=:name")
    Integer starcount(@Param("name")String name);

    @Query("select count(c) from Case c where c.userSecurity.phonenumber=:name")
    int casecount(@Param("name")String name);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Case c where c.userSecurity.code=:usercode ")
    Page<Case> casesByUsercode(Pageable pageable, @Param("usercode") String usercode);
}
