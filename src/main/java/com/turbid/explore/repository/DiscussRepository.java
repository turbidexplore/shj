package com.turbid.explore.repository;

import com.turbid.explore.pojo.Discuss;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public interface DiscussRepository extends JpaRepository<Discuss,String> {

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select d from Discuss d where d.communityCode=:communityCode ")
    Page<Discuss> listByPage(Pageable pageable, @Param("communityCode") String communityCode);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("select d from Discuss d where d.communityCode=:communityCode and d.star>:size ")
    Page<Discuss> hotlistByPage(Pageable pageable, @Param("communityCode") String communityCode,@Param("size") Integer size);

    int countByCommunityCode(String communityCode);

    Discuss findByCode(String code);
}
