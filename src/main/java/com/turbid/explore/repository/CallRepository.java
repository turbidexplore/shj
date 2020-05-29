package com.turbid.explore.repository;

import com.turbid.explore.pojo.Call;
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
public interface CallRepository extends JpaRepository<Call,String> {

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c.projectinfo from Call c where c.usercode =:name group by c.callusercode,c.usercode,c.projectinfo.code")
    Page<ProjectNeeds> listByUserMy(Pageable pageable,@Param("name") String name);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c.projectinfo from Call c where c.callusercode =:name group by c.callusercode,c.usercode,c.projectinfo.code")
    Page<ProjectNeeds> listByUserMe(Pageable pageable, @Param("name") String name);

    @Query("SELECT c.projectinfo from Call c where c.usercode =:name group by c.callusercode,c.usercode,c.projectinfo.code")
    List<ProjectNeeds> mycallcount(@Param("name")String name);

    @Query("SELECT c from Call c where c.callusercode =:name group by c.callusercode,c.usercode,c.projectinfo.code")
    List<Call> callmecount(@Param("name")String name);

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT c from Call c where c.callusercode =:name group by c.callusercode,c.usercode,c.projectinfo.code")
    Page<Call> callme(Pageable pageable,@Param("name") String name);
}
