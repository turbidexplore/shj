package com.turbid.explore.repository;

import com.turbid.explore.pojo.Call;
import com.turbid.explore.pojo.CallCount;
import com.turbid.explore.pojo.ProjectNeeds;
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
public interface CallCountRepository extends JpaRepository<CallCount,String> {


    @Query("SELECT count(c) from CallCount c where c.shopcode=:shopcode and (c.create_time LIKE CONCAT('%',:time,'%') or :time is null ) ")
    int callshopcount(@Param("shopcode")String shopcode,@Param("time") String time);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c.userhredimg from CallCount c where c.shopcode=:shopcode and (c.create_time LIKE CONCAT('%',:time,'%') or :time is null ) ")
    Page<String> callshop(Pageable pageable, @Param("shopcode") String shopcode, @Param("time") String time);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from CallCount c where c.shopcode=:shopcode and (c.create_time LIKE CONCAT('%',:time,'%') or :time is null ) ")
    Page<CallCount> callcountshop(Pageable pageable, @Param("shopcode") String shopcode, @Param("time") String time);

}
