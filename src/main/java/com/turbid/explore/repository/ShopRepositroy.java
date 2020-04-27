package com.turbid.explore.repository;

import com.turbid.explore.pojo.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepositroy extends JpaRepository<Shop,String> {

    @Query("select s from Shop s where s.userSecurity.phonenumber=:name")
    Shop getByUser(@Param("name") String name);

    @Query("select new Shop(s.code,s.companyname,s.logo) from Shop s where s.status=1 and s.label LIKE CONCAT(:label,'%') and s.brandgroup LIKE CONCAT(:brandgroup,'%') ")
    List<Shop> getByLabel(@Param("label")String label,@Param("brandgroup") String brandgroup);

    @Query("select s from Shop s where s.ischoose=1 and (s.label like %:label% or :label is null ) ")
    List<Shop> getByChoose(@Param("label") String label);
}
