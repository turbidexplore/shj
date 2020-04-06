package com.turbid.explore.repository;

import com.turbid.explore.pojo.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户认证信息仓库
 */
@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth,String> {
}
