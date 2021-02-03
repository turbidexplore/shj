package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "_liveroomurl")
@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class LiveRoomUrl extends BaseEntity {

    @Column(name = "livecode")
    private String livecode;

    @Column(name = "imgurl")
    private String imgurl;

    @Column(name = "title")
    private String title;

    @Column(name = "url")
    private String url;
}
