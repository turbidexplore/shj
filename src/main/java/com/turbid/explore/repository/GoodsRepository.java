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
import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods,String> {

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select g from Goods g where g.status=0 and (g.lable LIKE CONCAT('%',:label,'%')  or :label is null ) ")
    Page<Goods> listByPage(Pageable pageable, @Param("label") String label);


    @Query("select g from Goods g where  g.status=0 and g.company.code=:shopcode and (g.lable=:label or :label is null )")
    List<Goods> newGoods( @Param("shopcode") String shopcode, @Param("label") String label);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select g from Goods g where g.company.userSecurity.phonenumber=:name and g.status=0 ")
    Page<Goods> mylistByPage(Pageable pageable,@Param("name") String name);

    @Modifying
    @Query("update Goods p set p.status=:status where p.code=:code")
    int updatastatus(@Param("code")String code,@Param("status") Integer status);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select g from Goods g where  g.status=0 and ( g.content LIKE CONCAT('%',:text,'%') or g.title  LIKE CONCAT('%',:text,'%') or g.lable  LIKE CONCAT('%',:text,'%') or :text is null )")
    Page<Goods> search(Pageable pageable,@Param("text") String text);

    @Modifying
    @Query("update Goods p set p.del=:del where p.code=:code")
    int updataDelete(@Param("del")Boolean del,@Param("code") String code);

    @Modifying
    @Query("select g.lable from Goods g where g.company.code=:shopcode and g.status=0 group by g.lable")
    List<String> goodsclassByShopcode( @Param("shopcode") String shopcode);
}
