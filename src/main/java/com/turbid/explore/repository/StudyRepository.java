package com.turbid.explore.repository;

import com.turbid.explore.pojo.Study;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study,String> {
}
