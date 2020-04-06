package com.turbid.explore.repository;

import com.turbid.explore.pojo.UserSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户安全信息仓库
 */
@Repository
public interface UserSecurityRepository extends JpaRepository<UserSecurity,String> {

    @Query("SELECT u from UserSecurity u where u.phonenumber=:phone  ")
    UserSecurity findByPhone( @Param("phone")String phone);

    @Query("SELECT count(u) from UserSecurity u where u.phonenumber=:phone  ")
    int findByPhoneCount( @Param("phone")String phone);
}
