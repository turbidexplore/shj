package com.turbid.explore.repository;

import com.turbid.explore.pojo.ShopFans;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface ShopFansRepository extends JpaRepository<ShopFans,String> {


    List<ShopFans> findByUserSecurityPhonenumber(String phonenumber);

    List<ShopFans> findByShopCode(String shopcode);

    int countByUserSecurityPhonenumber(String phonenumber);

    int countByShopCode(String code);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT a from ShopFans a where a.userSecurity.phonenumber=:phonenumber ")
    Page<ShopFans> findByUserSecurityPage(Pageable pageable,@Param("phonenumber") String phonenumber);


    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT a from ShopFans a where a.shop.code=:shopcode ")
    Page<ShopFans> findByShopPage(Pageable pageable,@Param("shopcode") String shopcode);

    @Query("SELECT count(c) from ShopFans c where c.shop.code= :shopcode and ((c.create_time LIKE CONCAT('%',:time,'%') or :time is null ) or  (c.create_time LIKE CONCAT('%',:time,'%') or :time is null ))")
    int newfollowmeCount(@Param("shopcode") String shopcode, String time);
}
