package com.turbid.explore.repository;

import com.turbid.explore.pojo.Answer;
import com.turbid.explore.pojo.LoginHis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository
public interface LoginHisRepository extends JpaRepository<LoginHis,String> {


}
