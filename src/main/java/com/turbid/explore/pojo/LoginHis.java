package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "loginhis")
@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class LoginHis extends BaseEntity {

    @Column(name = "phone")
    private String phone;

    @Column(name = "logintype")
    private String logintype;

    @Column(name = "os")
    private String os;

    @Column(name = "device")
    private String device;

    @Column(name = "version")
    private String version;

    @Column(name = "status")
    private Integer status;
}
