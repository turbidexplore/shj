package com.turbid.explore.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "studynotice")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class StudyNotice extends BaseEntity{

    @Column(name = "title")
    private String title;

    @Column(name = "_desc",length = 9999)
    private String desc;

    @Column(name = "img",length = 500)
    private String img;

    @Column(name = "_time")
    private String time;

    @Column(name = "address")
    private String address;

    @Column(name = "_type")
    private int type;

}
