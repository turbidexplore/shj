package com.turbid.explore.repository;

import com.turbid.explore.pojo.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,String> {

    @Query("select o from Order o where o.orderno=:orderno")
    Order findByOrderNo(@Param("orderno") String orderno);
}
