package com.turbid.explore.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "integralgoodsorder")
@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class IntegralGoodsOrder extends BaseEntity {

    @ManyToOne(targetEntity = Address.class)
    @JoinColumn(name = "address_id",referencedColumnName = "code")
    private Address address;

    @ManyToOne(targetEntity = IntegralGoods.class)
    @JoinColumn(name = "integralgoods_id",referencedColumnName = "code")
    private IntegralGoods integralGoods;

    @ManyToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "user_id",referencedColumnName = "code")
    private UserSecurity userSecurity;

    @Column(name = "status")
    private Integer status;

    @Column(name = "orderno")
    private String orderno;

    @Column(name = "kdd")
    private String kdd;
}
