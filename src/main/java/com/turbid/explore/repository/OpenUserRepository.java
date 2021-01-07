package com.turbid.explore.repository;

import com.turbid.explore.pojo.OpenUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OpenUserRepository extends JpaRepository<OpenUser,String> {

    OpenUser findByOpenid(String openid);

    @Query("select count(o) from OpenUser o where o.openid=:openid ")
    int countByPhoneAndOpentype(@Param("openid")String openid);

    List<OpenUser> findByPhone(String name);

    @Modifying
    int deleteByPhoneAndOpenid(String phone,String openid);

    @Modifying
    @Query("update OpenUser o set o.phone=:phone where o.phone=:name")
    int updatephone(@Param("name") String name,@Param("phone") String phone);
}
