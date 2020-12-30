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
    @Query("SELECT c from Case c where c._cj=0 and  (c.subject like CONCAT('%',:subject,'%') or :subject is null ) and (c.label like  CONCAT('%',:label,'%') or :label is null) ")
    Page<Case> listByPage(Pageable pageable, @Param("subject") String subject,@Param("label") String label);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Case c where c.userSecurity.phonenumber=:name ")
    Page<Case> mycases(Pageable pageable, @Param("name") String name);

    @Query("select c from Case c where c.code=:code")
    Case caseByCode(@Param("code") String code);

    @Query("select c from Case c where c._cj=0 and  c.label=:label and c.subject=:subject")
    List<Case> recommend(@Param("label")String label,@Param("subject") String subject);

    @Query("select sum (c.stars.size) from Case c where c._cj=0 and   c.userSecurity.phonenumber=:name or c.userSecurity.code=:name")
    Integer starcount(@Param("name")String name);

    @Query("select count(c) from Case c where c._cj=0 and  c.userSecurity.phonenumber=:name or c.userSecurity.code=:name")
    int casecount(@Param("name")String name);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Case c where c._cj=0 and  c.userSecurity.code=:usercode ")
    Page<Case> casesByUsercode(Pageable pageable, @Param("usercode") String usercode);

    @Query("select count(c) from Comment c where   c.relation in (select caseinfo.code from Case caseinfo where caseinfo.userSecurity.code =:usercode)")
    int commentcount( @Param("usercode")String usercode);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Case c where c._cj=0 and  c.label LIKE CONCAT('%',:text,'%') or c.content LIKE CONCAT('%',:text,'%') or c.title LIKE CONCAT('%',:text,'%') or c.name LIKE CONCAT('%',:text,'%') or c.company LIKE CONCAT('%',:text,'%') or c.subject LIKE CONCAT('%',:text,'%') or c.address LIKE CONCAT('%',:text,'%') or c.team LIKE CONCAT('%',:text,'%')  ")
    Page<Case> search(Pageable pageable,@Param("text") String text);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Case c where  c._cj=0 and c.label LIKE CONCAT('%',:text,'%') or c.city LIKE CONCAT('%',:text,'%') or c.subject LIKE CONCAT('%',:text,'%') or :text is null  ")
    Page<Case> casebylabel(Pageable pageable, String text);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Case c where c.userSecurity.shopcode=:code and c._cj=1 ")
    Page<Case> getcj(Pageable pageable,@Param("code") String code);
}
