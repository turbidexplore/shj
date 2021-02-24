package com.turbid.explore.pojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import javax.persistence.*;
import java.util.List;


/**
 * 用户安全信息
 */
@Data
@Entity
@Table(name = "user_security")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class UserSecurity extends BaseEntity {

    public UserSecurity() {
    }

    //密码
    @Column(name = "password",length = 255)
    private String password;

    //密码 0设计师，1经销商，2工厂，3设计公司，4地产商，5其他
    @Column(name = "type",length = 255)
    private Integer type;

    //手机号码
    @Column(name = "phonenumber",length = 11)
    private String phonenumber;

    //手机号码
    @Column(name = "qaalevel")
    private int qaalevel;

    //手机号码
    @Column(name = "likes")
    private String likes;

    //手机号码
    @Column(name = "shb")
    private Integer shb;

    @Column(name = "signintime")
    private String signintime;

    //用户基础信息
    @OneToOne(targetEntity = UserBasic.class)
    @JoinColumn(name = "basic_id",referencedColumnName = "code")
    private UserBasic userBasic;

    //用户认证信息
    @OneToOne(targetEntity = UserAuth.class)
    @JoinColumn(name = "auth_id",referencedColumnName = "code")
    private UserAuth userAuth;

    //用户认证信息
    @Column(name = "shopcode")
    private String shopcode;

    //用户认证信息
    @Column(name = "balance")
    private String balance;

    @Column(name = "qdtime")
    private String qdtime;

    @Column(name = "qdcount")
    private Integer qdcount;

    @Column(name = "info")
    private Integer info;

    @Column(name = "auth")
    private Integer auth;

    @Column(name = "newuser")
    private Integer newuser;

    @Column(name = "experience")
    private Integer experience;


}
