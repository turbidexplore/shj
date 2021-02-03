package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "zb_Info")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class ZbInfo extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "time")
    private String time;

    @Column(name = "address")
    private String address;

    @Column(name = "indexurl")
    private String indexurl;

    @Column(name = "type")
    private int type;

    @Column(name = "content")
    private String content;

}
