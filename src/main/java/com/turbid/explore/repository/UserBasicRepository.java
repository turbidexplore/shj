package com.turbid.explore.repository;


import com.turbid.explore.pojo.UserBasic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户基本信息仓库
 */
@Repository
public interface UserBasicRepository extends JpaRepository<UserBasic,String> {
}
