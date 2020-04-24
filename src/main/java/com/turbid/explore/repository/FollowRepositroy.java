package com.turbid.explore.repository;

import com.turbid.explore.pojo.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public interface FollowRepositroy extends JpaRepository<Follow,String> {
    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Follow c where c.user.phonenumber= :name ")
    Page<Follow> myfollow(Pageable pageable,@Param("name") String name);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Follow c where c.userFollow.phonenumber= :name ")
    Page<Follow> followme(Pageable pageable,@Param("name") String name);

    @Query("SELECT count(c) from Follow c where c.user.phonenumber= :name ")
    int myfollowCount(@Param("name")String name);

    @Query("SELECT count(c) from Follow c where c.userFollow.phonenumber= :name ")
    int followmeCount(@Param("name")String name);

    @Query("SELECT count(c) from Follow c where c.userFollow.phonenumber= :phone and c.user.phonenumber=:name ")
    int findByCount(@Param("name")String name,@Param("phone") String phone);

    @Query("delete from Follow c where c.userFollow.phonenumber= :phone and c.user.phonenumber=:name ")
    Object cancelfollow(@Param("name")String name,@Param("phone") String phone);
}
