package com.turbid.explore.repository;

import com.turbid.explore.pojo.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,String> {
}
