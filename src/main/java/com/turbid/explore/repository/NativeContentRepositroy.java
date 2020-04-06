package com.turbid.explore.repository;

import com.turbid.explore.pojo.NativeContent;
import com.turbid.explore.pojo.Needs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public interface NativeContentRepositroy extends JpaRepository<NativeContent,String> {

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from NativeContent n  ")
    Page<NativeContent> listByPage(Pageable pageable);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from NativeContent n where n.userSecurity.phonenumber= :username ")
    Page<NativeContent> listByPage(Pageable pageable, @Param(value = "username")String username);
}
