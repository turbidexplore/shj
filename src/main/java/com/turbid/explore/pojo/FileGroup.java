package com.turbid.explore.pojo;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 文件组
 */
@Data
@Entity
@Table(name = "file_group")
public class FileGroup extends BaseEntity {

    @OneToMany(mappedBy = "fileGroup",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<FileInfo> fileInfos;
}
