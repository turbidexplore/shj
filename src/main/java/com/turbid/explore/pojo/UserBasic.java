package com.turbid.explore.pojo;

import com.turbid.explore.pojo.BaseEntity;
import lombok.Data;

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


    //用户昵称
    @Column(name = "nikename", unique = true, nullable = false, length = 32)
    private String nikename;

    //用户头像
    @Column(name = "headportrait", unique = true, nullable = false, length = 32)
    private String headportrait;

    //用户所在国家
    @Column(name = "country", unique = true, nullable = false, length = 32)
    private String country;

    //用户所在省份
    @Column(name = "province", unique = true, nullable = false, length = 32)
    private String province;

    //用户所在城市
    @Column(name = "city", unique = true, nullable = false, length = 32)
    private String city;

    //用户所在区域
    @Column(name = "area", unique = true, nullable = false, length = 32)
    private String area;


}

