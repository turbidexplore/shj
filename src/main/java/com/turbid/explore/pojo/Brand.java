package com.turbid.explore.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "brand")
public class Brand extends BaseEntity{

    @ApiModelProperty(value = "品牌logo")
    @Column(name = "logo",length = 255)
    private String logo;

    @ApiModelProperty(value = "品牌名称")
    @Column(name = "name",length = 255)
    private String name;

    @ApiModelProperty(value = "品牌标签")
    @Column(name = "label",length = 255)
    private String label;

    @ApiModelProperty(value = "品牌内容")
    @Column(name = "content",length = 5000)
    private String content;

    @ApiModelProperty(value = "品牌介绍")
    @Column(name = "introduce",length = 500)
    private String introduce;

    @ApiModelProperty(value = "招商信息")
    @Column(name = "business",length = 500)
    private String business;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)
    @JoinColumn(name="company_code")
    private UserAuth company;

    //评论信息
    @OneToMany(cascade= CascadeType.ALL,fetch= FetchType.LAZY)
    private List<Comment> comments;

}
