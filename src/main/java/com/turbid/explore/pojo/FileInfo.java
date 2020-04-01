package com.turbid.explore.pojo;

import lombok.Data;

import javax.persistence.*;


/**
 * 文件类
 */

@Entity
@Table(name = "file_info")
@Data
public class FileInfo extends BaseEntity {

    //文件组
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)
    @JoinColumn(name="group_code")
    private FileGroup fileGroup;

    //文件标题
    @Column(name = "title",  length = 32)
    private String title;

    //文件大小
    @Column(name = "size",  length = 32)
    private Long size;

    //文件地址
    @Column(name = "url",  length = 255)
    private String url;

    //文件类型
    @Column(name = "type",  length = 32)
    private String type;



}
