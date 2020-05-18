package com.turbid.explore.repository;

import com.turbid.explore.pojo.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.security.Principal;
import java.util.List;

@Repository
public interface ShopRepositroy extends JpaRepository<Shop,String> {

    @Query("select s from Shop s where s.userSecurity.phonenumber=:name")
    Shop getByUser(@Param("name") String name);

    @Query("select new Shop(s.code,s.companyname,s.logo) from Shop s where s.status=1 and (s.label LIKE CONCAT(:label,'%') or :label is null ) and ( s.brandgroup LIKE CONCAT(:brandgroup,'%') or :brandgroup is null ) ")
    List<Shop> getByLabel(@Param("label")String label,@Param("brandgroup") String brandgroup);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select s from Shop s where s.ischoose=1 and (s.label like %:label% or :label is null ) ")
    Page<Shop> getByChoose(Pageable pageable, @Param("label") String label);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select s from Shop s  ")
    Page<Shop> recommend(Pageable pageable);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select s from Shop s where (s.label like %:type% or :type is null )")
    Page<Shop> zsjm(Pageable pageable, @Param("type") String type);

    @Query("select s from Shop s where s.userSecurity.code=:usercode")
    Shop getByUsercode(@Param("usercode") String usercode);
}
