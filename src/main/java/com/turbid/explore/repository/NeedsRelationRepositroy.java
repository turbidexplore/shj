package com.turbid.explore.repository;

import com.turbid.explore.pojo.NeedsRelation;
import com.turbid.explore.pojo.ProjectNeeds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NeedsRelationRepositroy extends JpaRepository<NeedsRelation,String> {

    @Query("select count(n) from NeedsRelation n where n.phone=:name and n.needsorderno=:code and n.status=1")
    int findneedsR(@Param("name") String name,@Param("code") String code);

    @Modifying
    @Query("update NeedsRelation p set p.status=1 where p.orderno=:orderno")
    int updateSEE(@Param("orderno") String orderno);

    @Query("select n from ProjectNeeds n where n.orderno in(select p.needsorderno from NeedsRelation p where p.orderno=:orderno) ")
    ProjectNeeds getByOrder(@Param("orderno")String orderno);

    @Modifying
    @Query("update NeedsRelation o set o.phone=:phone where o.phone=:oldphone")
    int updatephone(@Param("oldphone") String oldphone, @Param("phone") String phone);
}
