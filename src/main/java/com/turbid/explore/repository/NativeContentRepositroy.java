package com.turbid.explore.repository;

import com.turbid.explore.pojo.Brand;
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
import java.util.List;

@Repository
public interface NativeContentRepositroy extends JpaRepository<NativeContent,String> {

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from NativeContent n where ( n.del is null or n.del=false ) and ( n.label LIKE CONCAT('%',:label,'%') or :label is null ) and  ( n.fromv LIKE CONCAT('%',:fromv,'%') or :fromv is null )")
    Page<NativeContent> listByPageLabel(Pageable pageable,@Param("label")String label,@Param("fromv")String fromv);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from NativeContent n where ( n.del is null or n.del=false ) and (n.userSecurity.phonenumber =:username or :username ='administrator')")
    Page<NativeContent> listByPage(Pageable pageable, @Param(value = "username")String username);

    @Query("SELECT n from NativeContent n where ( n.del is null or n.del=false ) and n.code=:code  ")
    NativeContent newsByCode(@Param("code") String code);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from NativeContent n where ( n.del is null or n.del=false ) and ( n.content LIKE CONCAT('%',:text,'%') or n.label LIKE CONCAT('%',:text,'%') or n.title LIKE CONCAT('%',:text,'%') or n.userSecurity.userBasic.nikename LIKE CONCAT('%',:text,'%') or n.company.name LIKE CONCAT('%',:text,'%') ) ")
    Page<NativeContent> search(Pageable pageable,@Param("text") String text);

    @Modifying
    @Query("update Brand p set p.del=:del where p.code=:code")
    int updataDelete(@Param("del")Boolean del,@Param("code") String code);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from NativeContent n where ( n.del is null or n.del=false ) and ( n.freesubject =:freesubject or :freesubject is null ) and (n.subject =:subject or :subject is null ) and ( n.fromv =:abroad or :abroad is null ) and ( n.label =:label or :label is null )")
    Page<NativeContent> allbypage(Pageable pageable, @Param("freesubject")String freesubject, @Param("subject")String subject,@Param("abroad") Integer abroad,@Param("label") String label);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from NativeContent n where ( n.del is null or n.del=false ) and ( n.freesubject =:freesubject or :freesubject is null ) and (n.subject =:subject or :subject is null ) and ( n.fromv =:abroad or :abroad is null ) and ( n.label =:label or :label is null ) and n.company.code is not null ")
    Page<NativeContent> allbypageandshop(Pageable pageable, @Param("freesubject")String freesubject, @Param("subject")String subject,@Param("abroad") Integer abroad,@Param("label") String label);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from NativeContent n where ( n.del is null or n.del=false ) and n.company.code=:shopcode")
    Page<NativeContent> infobyshopcode(Pageable pageable,@Param("shopcode") String shopcode);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from NativeContent n where ( n.del is null or n.del=false ) and n.brandcode=:brandcode")
    Page<NativeContent> getbybrand(Pageable pageable, @Param("brandcode") String brandcode);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n.firstimage from NativeContent n where ( n.del is null or n.del=false ) and n.company.code=:code")
    Page<String> wuyu(Pageable pageable,@Param("code") String code);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from NativeContent n where ( n.del is null or n.del=false ) and n.brandcode=:brandcode")
    Page<NativeContent> findByBrandcode(Pageable pageable,@Param("brandcode") String brandcode);
}
