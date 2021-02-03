package com.turbid.explore.repository;

import com.turbid.explore.pojo.Brand;
import com.turbid.explore.pojo.LiveInfo;
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
public interface LiveInfoReposity extends JpaRepository<LiveInfo,String> {

    @Query("select l from LiveInfo l where l.userSecurity.phonenumber=:name and (l.type =0 or l.type =1)")
    List<LiveInfo> findByUser(@Param("name") String name);

    @Query("select l from LiveInfo l where l.type =0 or l.type =1")
    List<LiveInfo> livelist();

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select l from LiveInfo l where l.type =3")
    Page<LiveInfo> livebacklist(Pageable pageable);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select l from LiveInfo l where l.type =3 and l.userSecurity.code=:name")
    Page<LiveInfo> mylives(Pageable pageable, @Param("name") String name);

    @Query("select l from LiveInfo l where l.type =0 and l.code not in (select a.code from LiveInfo a where l.userSecurity.phonenumber =:name)")
    List<LiveInfo> findlive(@Param("name") String name);
}
