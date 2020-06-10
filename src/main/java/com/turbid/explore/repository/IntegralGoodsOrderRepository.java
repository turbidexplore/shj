package com.turbid.explore.repository;

import com.turbid.explore.pojo.IntegralGoodsOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IntegralGoodsOrderRepository extends JpaRepository<IntegralGoodsOrder,String> {


    IntegralGoodsOrder findByOrderno(String orderno);
}
