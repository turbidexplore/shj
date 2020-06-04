package com.turbid.explore.repository;

import com.turbid.explore.pojo.NativeContent;
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
public interface NativeContentRepositroy extends JpaRepository<NativeContent,String> {

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from NativeContent n where ( n.label LIKE CONCAT('%',:label,'%') or :label is null )")
    Page<NativeContent> listByPageLabel(Pageable pageable,@Param("label")String label);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from NativeContent n where n.userSecurity.phonenumber= :username ")
    Page<NativeContent> listByPage(Pageable pageable, @Param(value = "username")String username);

    @Query("SELECT n from NativeContent n where n.code=:code ")
    NativeContent newsByCode(@Param("code") String code);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from NativeContent n where n.content LIKE CONCAT('%',:text,'%') or n.label LIKE CONCAT('%',:text,'%') or n.title LIKE CONCAT('%',:text,'%') or n.userSecurity.userBasic.nikename LIKE CONCAT('%',:text,'%')")
    Page<NativeContent> search(Pageable pageable,@Param("text") String text);

    @Modifying
    @Query("update Brand p set p.del=:del where p.code=:code")
    int updataDelete(@Param("del")Boolean del,@Param("code") String code);
}
