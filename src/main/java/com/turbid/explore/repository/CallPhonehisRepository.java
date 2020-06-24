package com.turbid.explore.repository;

import com.turbid.explore.pojo.CallPhonehis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallPhonehisRepository extends JpaRepository<CallPhonehis,String> {

}
