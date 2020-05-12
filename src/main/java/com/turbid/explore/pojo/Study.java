package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "study")
@ApiModel(description= "Study")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Study extends BaseEntity {

    @Column(name = "title",  nullable = false, length = 32)
    private String title;

    @Column(name = "price",  nullable = false, length = 32)
    private Integer price;

    @Column(name = "shb",  nullable = false, length = 32)
    private Integer shb;

    @Column(name = "pricetype",  nullable = false, length = 32)
    private String pricetype;

    @Column(name = "type",  nullable = false, length = 32)
    private String type;

    @Column(name = "indeximgurl",  nullable = false, length = 5000)
    private String indeximgurl;

    @Column(name = "videourl",  nullable = false, length = 5000)
    private String videourl;

    @Column(name = "seecount",  nullable = false, length = 32)
    private Integer seecount;
}
