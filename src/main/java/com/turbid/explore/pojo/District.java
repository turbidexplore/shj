package com.turbid.explore.pojo;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "district")
public class District {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "pid")
    private Integer pid;

    @Column(name = "district_name")
    private String district_name;

    @Column(name = "district_sqe")
    private String district_sqe;

    @Column(name = "type")
    private Integer type;

    @Column(name = "sname")
    private String sname;

    @Column(name = "mername")
    private String mername;

    @Column(name = "yzcode")
    private String yzcode;

    @Column(name = "Lng")
    private String lng;

    @Column(name = "Lat")
    private String lat;

    @Column(name = "pinyin")
    private String pinyin;
}
