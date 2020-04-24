package com.turbid.explore.repository;

import com.turbid.explore.pojo.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepositroy extends JpaRepository<Shop,String> {

    @Query("select s from Shop s where s.userSecurity.phonenumber=:name")
    Shop getByUser(@Param("name") String name);
}
