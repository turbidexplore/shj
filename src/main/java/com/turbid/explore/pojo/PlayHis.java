package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "_playhis")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class PlayHis extends BaseEntity{

    //用户基础信息
    @OneToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "user_id",referencedColumnName = "code")
    private UserSecurity userSecurity;

    //用户基础信息
    @OneToOne(targetEntity = Study.class)
    @JoinColumn(name = "study_id",referencedColumnName = "code")
    private Study study;

    @Column(name = "progress")
    private String progress;

}
