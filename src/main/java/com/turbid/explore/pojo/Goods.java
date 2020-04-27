package com.turbid.explore.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "goods")
@Data
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

    @ApiModelProperty(value = "价格价格：0一口价，1可议价")
    @Column(name = "pricetype")
    private Integer pricetype;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)
    @JoinColumn(name="shop_code")
    private Shop shop;

    @ApiModelProperty(value = "内容展示")
    @Column(name = "content")
    private String content;

    @ApiModelProperty(value = "感兴趣数")
    @Column(name = "like")
    private Integer like;

    @ApiModelProperty(value = "图片集")
    @Column(name = "imgs")
    private String imgs;


}
