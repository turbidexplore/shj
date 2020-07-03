package com.turbid.explore.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "configinfo")
public class Configinfo {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "androidversion")
    private String androidversion;

    @Column(name = "androidcontent")
    private String androidcontent;

    @Column(name = "iosversion")
    private String iosversion;

    @Column(name = "ioscontent")
    private String ioscontent;

    @Column(name = "iosupdate")
    private boolean iosupdate;

    @Column(name = "androidupdate")
    private boolean androidupdate;
}
