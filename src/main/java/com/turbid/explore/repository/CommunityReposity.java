package com.turbid.explore.repository;

import com.turbid.explore.pojo.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public interface CommunityReposity extends JpaRepository<Community,String> {


    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select c from Community c where ( c.label =:label or :label is null ) and ( c.userSecurity.code =:usercode or :usercode is null )")
    Page<Community> listByPage(Pageable pageable, @Param("label") String label,@Param("usercode")String usercode);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select c from Community c where c.label in ('设计','产品') and ( c.userSecurity.code =:usercode or :usercode is null )")
    Page<Community> listByPagea(Pageable pageable, @Param("usercode")String usercode);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select c from Community c where c.label not in ('设计','产品') and ( c.userSecurity.code =:usercode or :usercode is null )")
    Page<Community> listByPageb(Pageable pageable,@Param("usercode") String usercode);

    @Query("select count(c) from Community c where c.userSecurity.code =:usercode ")
   int countbyuser(@Param("usercode") String usercode);

    Community findByCode(String code);

}
