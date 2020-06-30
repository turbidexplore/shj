package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "openuser")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class OpenUser extends BaseEntity {

    //手机号码
    @Column(name = "openid",length = 255)
    private String openid;

    //手机号码
    @Column(name = "opentype",length = 32)
    private String opentype;

    //手机号码
    @Column(name = "phone",length = 32)
    private String phone;

    //评论信息
    @ManyToOne(cascade= CascadeType.REFRESH,fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "code")
    private UserSecurity userSecurity;

}
