package com.turbid.explore.repository;

import com.turbid.explore.pojo.TJ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TJReposity extends JpaRepository<TJ,String> {
}
