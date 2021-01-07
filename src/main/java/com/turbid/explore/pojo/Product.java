package com.turbid.explore.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "_product")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Product extends BaseEntity {


    @Column(name="type")
    private Integer type;

    @Column(name="word")
    private String word;

    @Column(name="imgs",length = 9000)
    private String imgs;

    @Column(name="label")
    private String label;

    @Column(name="subject")
    private String subject;

    @Column(name="city")
    private String city;

    //用户基础信息
    @OneToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "user_id",referencedColumnName = "code")
    private UserSecurity userSecurity;

    @ManyToOne
    @JoinColumn(name="company_code")
    private Shop company;

    @Column(name = "starcount")
    private int starcount;

    @Column(name = "remove")
    private int remove;

    @Column(name = "isstar")
    private boolean isstar;

    @Column(name = "commentcount")
    private int commentcount;

}
