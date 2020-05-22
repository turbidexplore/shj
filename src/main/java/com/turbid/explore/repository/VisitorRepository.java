package com.turbid.explore.repository;

import com.turbid.explore.pojo.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor,String> {

    @Query("select count (v) from Visitor v where (v.create_time LIKE CONCAT(:dateStr,'%') or :dateStr is null ) and v.shopcode=:code")
    int countNumber(@Param("dateStr")String dateStr,@Param("code") String code);

    @Query("select count (v) from Visitor v where (v.create_time LIKE CONCAT(:time,'%') or :time is null ) and v.shopcode in(select a.code from Brand a where a.company.code=:code)")
    int brandCount(@Param("time")String time,@Param("code") String code);

    @Query("select count (v) from Visitor v where (v.create_time LIKE CONCAT(:time,'%') or :time is null ) and v.shopcode in(select a.code from Goods a where a.company.code =:code)")
    int goodsCount(@Param("time")String time,@Param("code") String code);
}
