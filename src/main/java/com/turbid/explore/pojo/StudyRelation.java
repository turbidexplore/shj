package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "studyrelation")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class StudyRelation extends BaseEntity{

    @ApiModelProperty(value = "订单号")
    @Column(name = "orderno")
    private String orderno;

    @ApiModelProperty(value = "studycode")
    @Column(name = "studycode")
    private String studycode;

    @Column(name = "phone")
    private String phone;

    @ApiModelProperty(value = "状态")
    @Column(name = "status")
    private Integer status;
}
