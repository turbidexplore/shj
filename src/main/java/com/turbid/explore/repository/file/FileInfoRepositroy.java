package com.turbid.explore.repository.file;

import com.turbid.explore.pojo.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoRepositroy extends JpaRepository<FileInfo,String> {
}