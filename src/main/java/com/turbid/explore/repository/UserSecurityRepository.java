package com.turbid.explore.repository;

import com.turbid.explore.pojo.UserSecurity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * 用户安全信息仓库
 */
@Repository
public interface UserSecurityRepository extends JpaRepository<UserSecurity,String> {

    @Query("SELECT u from UserSecurity u where u.phonenumber=:phone or u.code=:phone ")
    UserSecurity findByPhone( @Param("phone")String phone);

    @Query("SELECT count(u) from UserSecurity u where u.phonenumber=:phone  ")
    int findByPhoneCount( @Param("phone")String phone);

    @Query("SELECT count(u) from UserSecurity u where u.phonenumber=:name and u.signintime =:dateStr  ")
    int issignin(@Param("name")String name,@Param("dateStr") String dateStr);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT u from UserSecurity u where (u.userAuth.name =:text or :text is null ) and u.shopcode=:code ")
    Page<UserSecurity> listByPage(Pageable pageable,@Param("text") String text,@Param("code") String code);

    @Query("SELECT count(u) from UserSecurity u where (u.userAuth.name =:text or :text is null ) and u.shopcode=:code ")
    int shopuserscount(@Param("text") String text,@Param("code") String code);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT u from UserSecurity u where (u.phonenumber LIKE CONCAT('%',:phone,'%') or :phone is null ) and u.userAuth.name is not null and u.shopcode is null ")
    Page<UserSecurity> findByUserSecurityPhone(Pageable pageable,@Param("phone") String phone);

    @Query("select count (v) from UserSecurity v where (v.create_time LIKE CONCAT('%',:time,'%') or :time is null )")
    Long countByTime(@Param("time") String time);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT u from UserSecurity u where (u.phonenumber LIKE CONCAT('%',:text,'%') or :text is null )  ")
    Page<UserSecurity> alluserbypage(Pageable pageable,@Param("text") String text);

    @Query("select count (u) from UserSecurity u where (u.phonenumber LIKE CONCAT('%',:text,'%') or :text is null ) ")
    int allusercount(@Param("text")String text);

    @Query("select a from UserSecurity a ")
    List<UserSecurity> aaaaa();
}
