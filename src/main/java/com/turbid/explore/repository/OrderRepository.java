package com.turbid.explore.repository;

import com.turbid.explore.pojo.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public interface OrderRepository extends JpaRepository<Order,String> {

    @Query("select o from Order o where o.orderno=:orderno")
    Order findByOrderNo(@Param("orderno") String orderno);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT o from Order o where o.userphone=:name and o.paytype = 'iospay' and o.code not in(SELECT a.code from Order a where a.userphone=:name and a.paytype = 'iospay')")
    Page<Order> findByUser(Pageable pageable,@Param("name") String name);

    @Modifying
    @Query("update Order o set o.userphone=:phone where o.userphone=:oldphone ")
    int updatephone(@Param("oldphone") String oldphone, @Param("phone") String phone);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT o from Order o where o.userphone=:name and o.paytype='iospay'")
    Page<Order> findByUserIos(Pageable pageable,@Param("name")String name);
}
