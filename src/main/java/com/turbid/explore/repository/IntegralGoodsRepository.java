package com.turbid.explore.repository;

import com.turbid.explore.pojo.IntegralGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public interface IntegralGoodsRepository extends JpaRepository<IntegralGoods,String> {

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select g from IntegralGoods g where g.status=0 ")
    Page<IntegralGoods> listbypage(Pageable pageable);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select g from IntegralGoods g where g.status=0 and g.type=:type")
    Page<IntegralGoods> listbypagea(Pageable pageable, @Param("type")Integer type);


}
