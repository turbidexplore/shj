package com.turbid.explore.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "address")
@ApiModel(description= "收货地址")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Address extends BaseEntity {

    public Address() {
    }

    public Address(String name, String phone, Integer status, String province, String city, String area, String address, String usercode, Boolean isdef) {
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.province = province;
        this.city = city;
        this.area = area;
        this.address = address;
        this.usercode = usercode;
        this.isdef = isdef;
    }

    //收货人名称
    @Column(name = "name",length = 20)
    private String name;

    //收货人联系方式
    @Column(name = "phone",length = 11)
    private String phone;

    @Column(name = "status")
    private Integer status;

    //收货人所在省份
    @Column(name = "province",length = 10)
    private String province;

    //收货人所在城市
    @Column(name = "city",length = 10)
    private String city;

    //收货人所在区域
    @Column(name = "area",length = 10)
    private String area;

    //收货人详细地址
    @Column(name = "address",length = 80)
    private String address;

    //收货人用户code
    @Column(name = "usercode",length = 32)
    private String usercode;

    //收货人用户code
    @Column(name = "isdef",length = 32)
    private Boolean isdef;

    public Address(String code) {
        this.setCode(code);
    }
}
