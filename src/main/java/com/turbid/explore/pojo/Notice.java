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

    @Column(name = "type")
    private Integer type;

    @Column(name = "time")
    private String time;

    @Column(name = "status")
    private Integer status;

    public String getTime(){
        return this.getAddftime();
    }

    public Notice() {
    }

    public Notice(String userphone,String message,String form,Integer type,Integer status) {
        this.userphone=userphone;
        this.message=message;
        this.form=form;
        this.type=type;
        this.status=status;
    }

    public Notice(String code,String userphone,String message,String form,Integer type,Integer status) {
        this.setCode(code);
        this.userphone=userphone;
        this.message=message;
        this.form=form;
        this.type=type;
        this.status=status;
    }
}
