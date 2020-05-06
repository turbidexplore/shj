package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "visitor")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Visitor extends BaseEntity {

    //用户认证信息
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="usercode")
    private UserSecurity userSecurity;

    @Column(name = "shopcode")
    private String shopcode;
}
