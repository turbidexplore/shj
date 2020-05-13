package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "needsrelation")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class NeedsRelation extends BaseEntity{

    @ApiModelProperty(value = "订单号")
    @Column(name = "orderno")
    private String orderno;

    @ApiModelProperty(value = "needsorderno")
    @Column(name = "needsorderno")
    private String needsorderno;

    @ApiModelProperty(value = "用户code")
    @Column(name = "phone")
    private String phone;

    @ApiModelProperty(value = "状态")
    @Column(name = "status")
    private Integer status;
}
