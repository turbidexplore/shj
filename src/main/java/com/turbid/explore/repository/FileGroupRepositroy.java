package com.turbid.explore.repository;

import com.turbid.explore.pojo.FileGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileGroupRepositroy extends JpaRepository<FileGroup,String> {
}
