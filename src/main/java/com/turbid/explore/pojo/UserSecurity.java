package com.turbid.explore.pojo;


import com.turbid.explore.utils.Validation;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Pattern;


/**
 * 用户安全信息
 */
@Data
@Entity
@Table(name = "user_security")
public class UserSecurity extends BaseEntity {

    //密码
    @Column(name = "password",length = 255)
    private String password;


    //手机号码
    @Pattern(regexp = Validation.PHONE,message = "手机格式不合法")
    @Column(name = "phonenumber",length = 11)
    private String phonenumber;


    //用户基础信息
    @OneToOne(targetEntity = UserBasic.class)
    @JoinColumn(name = "basic_id",referencedColumnName = "code")
    private UserBasic userBasic;

    //用户认证信息
    @OneToOne(targetEntity = UserAuth.class)
    @JoinColumn(name = "auth_id",referencedColumnName = "code")
    private UserAuth userAuth;



}
