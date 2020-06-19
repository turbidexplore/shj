package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "callcountinfo")
@JsonIgnoreProperties(value = { "hibernateEAGERInitializer"})
public class CallCount extends BaseEntity{

    @Column(name="username")
    private String username;

    @Column(name="usercode")
    private String usercode;

    @Column(name="usertype")
    private String usertype;

    @Column(name="userhredimg")
    private String userhredimg;

    @Column(name="callusername")
    private String callusername;

    @Column(name="callusercode")
    private String callusercode;

    @Column(name="callusertype")
    private String callusertype;

    @Column(name="calluserhredimg")
    private String calluserhredimg;

    @Column(name="type")
    private String type;

    @Column(name="rcode")
    private String rcode;

    @Column(name="shopcode")
    private String shopcode;

    public CallCount() {
    }
}
