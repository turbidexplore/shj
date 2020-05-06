package com.turbid.explore.repository;

import com.turbid.explore.pojo.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor,String> {

    @Query("select count (v) from Visitor v where v.create_time LIKE CONCAT(:dateStr,'%') and v.shopcode=:code")
    int countNumber(@Param("dateStr")String dateStr,@Param("code") String code);
}
