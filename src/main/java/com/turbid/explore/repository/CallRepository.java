package com.turbid.explore.repository;

import com.turbid.explore.pojo.Call;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallRepository extends JpaRepository<Call,String> {
}
