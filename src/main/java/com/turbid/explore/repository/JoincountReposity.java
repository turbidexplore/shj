package com.turbid.explore.repository;

import com.turbid.explore.pojo.Joincount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoincountReposity  extends JpaRepository<Joincount,String> {
}
