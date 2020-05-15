package com.turbid.explore.repository;

import com.turbid.explore.pojo.Goods;
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
public interface GoodsRepository extends JpaRepository<Goods,String> {

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select g from Goods g where g.status=0 and (g.lable LIKE CONCAT(:label,'%')  or :label is null ) ")
    Page<Goods> listByPage(Pageable pageable, @Param("label") String label);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select g from Goods g where  g.status=0 and g.company.code=:shopcode ")
    Page<Goods> newGoods(Pageable pageable, @Param("shopcode") String shopcode);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select g from Goods g where g.company.userSecurity.phonenumber=:name ")
    Page<Goods> mylistByPage(Pageable pageable,@Param("name") String name);

    @Modifying
    @Query("update Goods p set p.status=:status where p.code=:code")
    int updatastatus(@Param("code")String code,@Param("status") Integer status);
}
