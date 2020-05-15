package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "callinfo")
@JsonIgnoreProperties(value = { "hibernateEAGERInitializer"})
public class Call extends BaseEntity{

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

    @ManyToOne(cascade= CascadeType.ALL,fetch= FetchType.LAZY)
    @JoinColumn(name="projectinfo")
    private ProjectNeeds projectinfo;

    public Call() {
    }
}
