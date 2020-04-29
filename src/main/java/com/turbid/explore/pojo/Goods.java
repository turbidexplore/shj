package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "goods")
@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Goods extends BaseEntity {


    @ApiModelProperty(value = "标题")
    @Column(name = "title")
    private String title;

    @ApiModelProperty(value = "标签")
    @Column(name = "lable")
    private String lable;

    @ApiModelProperty(value = "价格")
    @Column(name = "price")
    private Integer price;

    @ApiModelProperty(value = "最小价格")
    @Column(name = "minprice")
    private Integer minprice;

    @ApiModelProperty(value = "最大价格")
    @Column(name = "maxprice")
    private Integer maxprice;

    @ApiModelProperty(value = "价格价格：0一口价，1可议价")
    @Column(name = "pricetype")
    private Integer pricetype;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)
    @JoinColumn(name="company_code")
    private Shop company;

    @ApiModelProperty(value = "内容展示")
    @Column(name = "content",length = 5000)
    private String content;

    @ApiModelProperty(value = "感兴趣数")
    @Column(name = "likes")
    private Integer likes;

    @ApiModelProperty(value = "图片集")
    @Column(name = "imgs")
    private String imgs;

    @ApiModelProperty(value = "库存")
    @Column(name = "skucount")
    private Integer skucount;


}
