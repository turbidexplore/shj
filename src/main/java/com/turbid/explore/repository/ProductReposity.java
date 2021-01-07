package com.turbid.explore.repository;

import com.turbid.explore.pojo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public interface ProductReposity extends JpaRepository<Product,String> {

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT p from Product p where p.remove=0 and (p.type=1 or p.type=2 or p.type=3)")
    Page<Product> productsByPagea(Pageable pageable);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT p from Product p where p.remove=0 and ( p.type=4 or p.type=5 or p.type=6)")
    Page<Product> productsByPageb(Pageable pageable);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT p from Product p where p.remove=0 and ( p.type=2 or p.type=3 )")
    Page<Product> productsByPagec(Pageable pageable);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT p from Product p where p.remove=0 and ( p.type=:type or :type is null )")
    Page<Product> productsByPage(Pageable pageable, @Param("type") Integer type);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from Product n where n.remove=0 and ( ( n.city LIKE CONCAT('%',:text,'%') or n.word LIKE CONCAT('%',:text,'%') or n.subject LIKE CONCAT('%',:text,'%') or  n.label LIKE CONCAT('%',:text,'%') or n.userSecurity.userBasic.nikename LIKE CONCAT('%',:text,'%') or n.company.name LIKE CONCAT('%',:text,'%')) ) ")
    Page<Product> search(Pageable pageable, @Param("text") String text);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT p from Product p where p.remove=0 and ( p.userSecurity.code=:usercode or :usercode is null )")
    Page<Product> allByPage(Pageable pageable,@Param("usercode") String usercode);

    @Query("SELECT count(p) from Product p where p.remove=0 ")
    int counta();

    @Query("SELECT count(p) from Product p where p.remove=0 and p.userSecurity.code=:usercode")
    int countbyuser(@Param("usercode") String usercode);
}
