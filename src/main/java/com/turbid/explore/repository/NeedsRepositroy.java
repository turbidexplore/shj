package com.turbid.explore.repository;

import com.turbid.explore.pojo.Needs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;


import javax.persistence.QueryHint;

@Repository
public interface NeedsRepositroy extends JpaRepository<Needs,String> {

    @QueryHints(value = { @QueryHint(name = "query", value = "a query for pageable")})
    @Query("SELECT n from Needs n  ")
    Page<Needs> listByPage(Pageable pageable);
}
