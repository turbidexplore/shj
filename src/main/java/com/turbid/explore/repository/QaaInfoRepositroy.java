package com.turbid.explore.repository;

import com.turbid.explore.pojo.QaaInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public interface QaaInfoRepositroy extends JpaRepository<QaaInfo,String> {

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT q from QaaInfo q where q.isdel=false and (q.label LIKE CONCAT('%',:label,'%') or :label is null ) ")
    Page<QaaInfo> listByPage(Pageable pageable,@Param("label") String label);

    @Query("SELECT q from QaaInfo q where q.code=:code ")
    QaaInfo qaaByCode(@Param("code") String code);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT q from QaaInfo q where q.isdel=false and q.userSecurity.phonenumber=:name")
    Page<QaaInfo> listByUser(Pageable pageable,@Param("name") String name);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT q from QaaInfo q where q.isdel=false and (q.title LIKE CONCAT('%',:text,'%') or q.content LIKE CONCAT('%',:text,'%') or q.label LIKE CONCAT('%',:text,'%') )")
    Page<QaaInfo> search(Pageable pageable,@Param("text") String text);
}
