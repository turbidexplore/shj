package com.turbid.explore.repository;

import com.turbid.explore.pojo.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdRepository extends JpaRepository<Ad,String> {
}
