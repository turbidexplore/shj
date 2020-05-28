package com.turbid.explore.repository;

import com.turbid.explore.pojo.Collection;
import com.turbid.explore.pojo.Comment;
import com.turbid.explore.pojo.bo.CollectionType;
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
public interface CollectionRepositroy extends JpaRepository<Collection,String> {

    @Query("SELECT c from Collection c where c.relation= :relation ")
    List<Collection> listByPage(@Param("relation") String relation);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Collection c where c.userSecurity.phonenumber= :phone and c.collectionType=:collectionType ")
    Page<Collection> listByPagePhone(Pageable pageable, @Param("phone") String phone,@Param("collectionType") CollectionType collectionType);

    @Query("SELECT count(c) from Collection c where c.userSecurity.phonenumber=:name and c.relation= :relation  ")
    int findByCount(@Param("name") String name,@Param("relation")  String relation);

    @Query("SELECT c from Collection c where c.userSecurity.phonenumber=:name and c.relation= :relation  ")
    Collection find(@Param("name") String name,@Param("relation")  String relation);

    @Query("delete from Collection c where c.code=:code  ")
    @Modifying
    Integer cancelcollection(@Param("code") String code);

    @Query("select count (v) from Collection v where (v.create_time LIKE CONCAT('%',:time,'%') or :time is null ) and v.relation in(select a.code from Goods a where a.company.code =:code)")
    int goodslikes(@Param("time")String time,@Param("code") String code);

    int countByRelation(String code);
}
