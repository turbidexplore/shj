package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "banner")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Banner  extends BaseEntity {

    //商品图片组
    @OneToOne(targetEntity = FileInfo.class)
    @JoinColumn(name = "fileinfo_id",referencedColumnName = "code")
    private FileInfo fileInfo;

    @Column(name = "type",length = 255)
    private String type;

    @Column(name = "url",length = 255)
    private String url;
}
