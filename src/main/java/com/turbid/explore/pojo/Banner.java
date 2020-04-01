package com.turbid.explore.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "banner")
public class Banner  extends BaseEntity {

    //商品图片组
    @OneToOne(targetEntity = FileInfo.class)
    @JoinColumn(name = "fileinfo_id",referencedColumnName = "code")
    private FileInfo fileInfo;
}
