package com.turbid.explore.repository;

import com.turbid.explore.pojo.IntegralGoodsOrder;
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
public interface IntegralGoodsOrderRepository extends JpaRepository<IntegralGoodsOrder,String> {


    IntegralGoodsOrder findByOrderno(String orderno);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select i from IntegralGoodsOrder i where i.status =:status ")
    Page<IntegralGoodsOrder> findByPage(Pageable pageable, @Param("status")Integer status);

    @Query("select count(i) from IntegralGoodsOrder i where  i.status =:status ")
    int integralgoodsordercount( @Param("status")Integer status);
}
