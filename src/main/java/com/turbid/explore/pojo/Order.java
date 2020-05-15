package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "_order")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Order extends BaseEntity {

    @Column(name = "orderno")
    private String orderno;

    @Column(name = "userphone")
    private String userphone;

    @Column(name = "paytype")
    private String paytype;

    @Column(name = "goodscode")
    private String goodscode;

    @Column(name = "price")
    private String price;

    @Column(name = "body")
    private String body;

    @Column(name = "status")
    private int status;
}
