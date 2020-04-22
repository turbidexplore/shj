package com.turbid.explore.pojo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "follow")
@Data
public class Follow extends  BaseEntity {

    @ApiModelProperty(value = "被关注者")
    @OneToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "user_follow_id",referencedColumnName = "code")
    private UserSecurity userFollow;


    @ApiModelProperty(value = "关注者")
    @OneToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "user_id",referencedColumnName = "code")
    private UserSecurity user;
}
