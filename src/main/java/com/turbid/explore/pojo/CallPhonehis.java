package com.turbid.explore.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "callphonehis")
@JsonIgnoreProperties(value = { "hibernateEAGERInitializer"})
public class CallPhonehis extends BaseEntity {

    @ApiModelProperty(value = "店铺code")
    @Column(name = "shopcode")
    private String shopcode;

    @ApiModelProperty(value = "店铺code")
    @Column(name = "name")
    private String name;

    @ApiModelProperty(value = "店铺code")
    @Column(name = "phone")
    private String phone;

    @OneToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "user_id",referencedColumnName = "code")
    private UserSecurity userSecurity;
}
