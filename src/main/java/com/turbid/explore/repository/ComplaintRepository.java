package com.turbid.explore.repository;

import com.turbid.explore.pojo.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint,String> {
}
