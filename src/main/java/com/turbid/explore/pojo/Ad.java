package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "_ad")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Ad extends BaseEntity{

    //1商铺主页 2品牌主页 3特加仓 4课程组 5Url
    @Column(name = "relationtype",length = 255)
    private String relationtype;

    @Column(name = "imgs",length = 255)
    private String imgs;

    @Column(name = "name",length = 255)
    private String name;

    @Column(name = "logo",length = 255)
    private String logo;

    @Column(name = "title",length = 255)
    private String title;

    @Column(name = "relationcode",length = 255)
    private String relationcode;
}
