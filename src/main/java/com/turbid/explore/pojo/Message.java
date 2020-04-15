package com.turbid.explore.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "message")
public class Message extends BaseEntity {

    public Message() {
    }

    //授权码
    @Column(name = "authcode",length = 10)
    private String authcode;

    //手机号码
    @Column(name = "mebile",length = 32)
    private String mebile;

    //邮箱
    @Column(name = "email",length = 32)
    private String email;

    public Message(String authcode, String mebile, String email) {
        this.authcode = authcode;
        this.mebile = mebile;
        this.email = email;
    }
}
