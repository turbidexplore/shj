package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "call")
@JsonIgnoreProperties(value = { "hibernateEAGERInitializer"})
public class Call extends BaseEntity{

    @ManyToOne(cascade= CascadeType.ALL,fetch= FetchType.LAZY)
    @JoinColumn(name="userinfod")
    private UserSecurity userinfo;

    @ManyToOne(cascade= CascadeType.ALL,fetch= FetchType.LAZY)
    @JoinColumn(name="calluserinfo")
    private UserSecurity calluserinfo;

    @ManyToOne(cascade= CascadeType.ALL,fetch= FetchType.LAZY)
    @JoinColumn(name="projectNeeds")
    private ProjectNeeds projectNeeds;
}
