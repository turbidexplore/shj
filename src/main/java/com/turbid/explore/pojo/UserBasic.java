package com.turbid.explore.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 用户信息
 */
@Data
@Entity
@Table(name = "user_basic")
public class UserBasic extends BaseEntity {

    public UserBasic() {
    }

    //用户昵称
    @Column(name = "nikename", unique = true, nullable = false, length = 32)
    private String nikename;

    //用户头像
    @Column(name = "headportrait", length = 255)
    private String headportrait;

    //用户所在国家
    @Column(name = "country",   length = 32)
    private String country;

    //用户所在省份
    @Column(name = "province",  length = 32)
    private String province;

    //用户所在城市
    @Column(name = "city",  length = 32)
    private String city;


}

