package com.turbid.explore.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "integralgoods")
@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class IntegralGoods extends BaseEntity {

    public IntegralGoods() {
    }

    public IntegralGoods(String title, String lable, Integer price, Integer status, String content, String imgs, Integer skucount) {
        this.title = title;
        this.lable = lable;
        this.price = price;
        this.status = status;
        this.content = content;
        this.imgs = imgs;
        this.skucount = skucount;
    }

    @ApiModelProperty(value = "标题")
    @Column(name = "title")
    private String title;

    @ApiModelProperty(value = "标签")
    @Column(name = "lable")
    private String lable;

    @ApiModelProperty(value = "价格")
    @Column(name = "price")
    private Integer price;

    @ApiModelProperty(value = "状态")
    @Column(name = "status")
    private Integer status;

    @ApiModelProperty(value = "内容展示")
    @Column(name = "content",length = 5000)
    private String content;

    @ApiModelProperty(value = "图片集")
    @Column(name = "imgs",length = 5000)
    private String imgs;

    @ApiModelProperty(value = "库存")
    @Column(name = "skucount")
    private Integer skucount;

    public IntegralGoods(String code) {
        this.setCode(code);
    }
}
