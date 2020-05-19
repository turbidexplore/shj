package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "notice")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Notice extends BaseEntity {

    @Column(name = "userphone")
    private String userphone;

    @Column(name = "message")
    private String message;

    @Column(name = "form")
    private String form;

    @Column(name = "time")
    private String time;

    public String getTime(){
        return this.getAddftime();
    }
}
