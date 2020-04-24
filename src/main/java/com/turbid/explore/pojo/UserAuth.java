package com.turbid.explore.pojo;
import lombok.Data;
import javax.persistence.*;


/**
 * 用户认证信息
 */
@Data
@Entity
@Table(name = "user_auth")
public class UserAuth extends BaseEntity {

    public UserAuth() {
    }

    //审核状态
    @Column(name = "status",nullable = false,length = 1)
    private Integer status;

    //保证金
    @Column(name = "margin",length = 10)
    private Integer margin;

    //vip有效期
    @Column(name = "vipday",length = 32)
    private String vipday;






}
