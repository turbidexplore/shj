package com.turbid.explore.repository;

import com.turbid.explore.pojo.PlayHis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public interface PlayHisReposity extends JpaRepository<PlayHis,String> {

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from PlayHis n where n.userSecurity.phonenumber=:phone ")
    Page<PlayHis> findByUser(Pageable pageable, @Param("phone") String phone);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT distinct n from PlayHis n where n.userSecurity.phonenumber=:phone and n.code not in (select  a.code from PlayHis a where a.progress='0.00') and (n.study.code=:studycode or :studycode is null ) ")
    Page<PlayHis> findallByUser(Pageable pageable,  @Param("phone") String phone,  @Param("studycode") String studycode);
}
