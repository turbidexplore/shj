package com.turbid.explore.repository;

import com.turbid.explore.pojo.Visitor;
import com.turbid.explore.pojo.bo.BrandCountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor,String> {

    @Query("select count (v) from Visitor v where (v.create_time LIKE CONCAT('%',:dateStr,'%') or :dateStr is null ) and v.shopcode=:code")
    int countNumber(@Param("dateStr")String dateStr,@Param("code") String code);

    @Query("select count (v) from Visitor v where (v.create_time LIKE CONCAT('%',:time,'%') or :time is null ) and v.shopcode in(select a.code from Brand a where a.company.code=:code)")
    int brandCount(@Param("time")String time,@Param("code") String code);

    @Query("select count (v) from Visitor v where (v.create_time LIKE CONCAT('%',:time,'%') or :time is null ) and v.shopcode in(select a.code from Goods a where a.company.code =:code)")
    int goodsCount(@Param("time")String time,@Param("code") String code);

    @Query("select new BrandCountInfo(b.name,count (v)) from Visitor v,Brand b where v.shopcode=b.code and b.company.userSecurity.phonenumber=:name group by b.name")
    List<BrandCountInfo> brandinfo(@Param("name") String name);

    int countByShopcode(String code);

    @Query("select count (v) from Visitor v where (v.create_time LIKE CONCAT('%',:time,'%') or :time is null )")
    int countByTime(@Param("time") String time);


    @Query("delete from Visitor v where v.code=:code ")
    @Modifying
    Integer removestar(@Param("code") String code);

    @Query("select v.code from Visitor v where v.userSecurity.phonenumber = :name and v.shopcode = :code ")
    String findByCode(@Param("name")String name,@Param("code") String code);

    @Query("select count (v) from Visitor v where v.userSecurity.phonenumber = :name and v.shopcode = :code ")
    int countByName(@Param("name")String name,@Param("code") String code);
}
