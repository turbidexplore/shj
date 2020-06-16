package com.turbid.explore.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "shopfans")
@ApiModel(description= "shopfans")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class ShopFans extends BaseEntity{

    //用户基础信息
    @OneToOne(targetEntity = Shop.class)
    @JoinColumn(name = "shop_id",referencedColumnName = "code")
    private Shop shop;

    //用户认证信息
    @OneToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "user_id",referencedColumnName = "code")
    private UserSecurity userSecurity;

    @Column(name = "remarks")
    private String remarks;
}
