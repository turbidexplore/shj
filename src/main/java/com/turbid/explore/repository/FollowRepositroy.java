package com.turbid.explore.repository;

import com.turbid.explore.pojo.Follow;
import com.turbid.explore.pojo.bo.AreaCount;
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
public interface FollowRepositroy extends JpaRepository<Follow,String> {
    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Follow c where c.user.phonenumber= :name order by c.user.code,c.userFollow.code")
    Page<Follow> myfollow(Pageable pageable,@Param("name") String name);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Follow c where c.userFollow.phonenumber= :name order by c.user.code,c.userFollow.code ")
    Page<Follow> followme(Pageable pageable,@Param("name") String name);

    @Query("SELECT count(c) from Follow c where c.user.phonenumber= :name or c.user.code=:name ")
    int myfollowCount(@Param("name")String name);

    @Query("SELECT count(c) from Follow c where c.userFollow.phonenumber= :name or c.userFollow.code=:name ")
    int followmeCount(@Param("name")String name);

    @Query("SELECT count(c) from Follow c where (c.userFollow.code= :phone or c.userFollow.phonenumber= :phone ) and c.user.phonenumber=:name ")
    int findByCount(@Param("name")String name,@Param("phone") String phone);

    @Query("delete from Follow c where c.code=:code")
    @Modifying
    int cancelfollow(@Param("code") String code);

    @Query("SELECT c.code from Follow c where (c.userFollow.code= :phone or c.userFollow.phonenumber= :phone ) and c.user.phonenumber=:name ")
    String find(@Param("name")String name,@Param("phone") String phone);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Follow c where c.user.code= :name order by c.user.code,c.userFollow.code")
    Page<Follow> hefollow(Pageable pageable,@Param("name") String name);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Follow c where c.userFollow.code= :name order by c.user.code,c.userFollow.code")
    Page<Follow> followhe(Pageable pageable,@Param("name") String name);

    @Query("SELECT count(c) from Follow c where c.user.code= :name ")
    int hefollowCount(@Param("name")String name);

    @Query("SELECT count(c) from Follow c where c.userFollow.code= :name ")
    int followheCount(@Param("name")String name);

    @Query("SELECT count(c) from Follow c where c.userFollow.phonenumber= :name and (c.create_time LIKE CONCAT('%',:time,'%') or :time is null ) or c.userFollow.code=:name and (c.create_time LIKE CONCAT('%',:time,'%') or :time is null )")
    int newfollowmeCount(@Param("name")String name,@Param("time") String time);

    @Query("select new AreaCount(u.userBasic.city,count(u)) from UserSecurity u where u.code in (SELECT c.userFollow.code from Follow c where c.user.phonenumber= :name ) group by u.userBasic.city")
    List<AreaCount> areaCount(@Param("name")String name);
}
