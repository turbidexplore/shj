package com.turbid.explore.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "case_info")
public class Case extends BaseEntity {

    @ApiModelProperty(value = "项目名称")
    @Column(name = "name")
    private String name;

    @ApiModelProperty(value = "项目公司")
    @Column(name = "company")
    private String company;

    @ApiModelProperty(value = "项目团队")
    @Column(name = "team")
    private String team;

    @ApiModelProperty(value = "项目面积")
    @Column(name = "area")
    private String area;

    @ApiModelProperty(value = "项目地址")
    @Column(name = "address")
    private String address;

    @ApiModelProperty(value = "主题")
    @Column(name = "subject")
    private String subject;

    @ApiModelProperty(value = "标签")
    @Column(name = "label")
    private String label;

    @ApiModelProperty(value = "标题")
    @Column(name = "title")
    private String title;

    @ApiModelProperty(value = "内容")
    @Column(name = "content",length = 99)
    private String content;

    @ApiModelProperty(value = "图片地址")
    @Column(name = "urls",length = 9999)
    private String urls;

    @ApiModelProperty(value = "发布者")
    @OneToOne(fetch=FetchType.LAZY)
    private UserSecurity userSecurity;
}
