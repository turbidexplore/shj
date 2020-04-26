package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turbid.explore.tools.CodeLib;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "case_info")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
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

    @ApiModelProperty(value = "封面图片")
    @Column(name = "indexurl",length = 255)
    private String indexurl;

    @ApiModelProperty(value = "图片地址")
    @Column(name = "urls",length = 5000)
    private String urls;

    @ApiModelProperty(value = "发布者")
    @OneToOne(fetch=FetchType.LAZY)
    private UserSecurity userSecurity;

    @ManyToMany(targetEntity=Comment.class,cascade = {CascadeType.DETACH} ,fetch= FetchType.LAZY)
    private Set<Comment> comments;

    @ManyToMany(targetEntity=UserSecurity.class,cascade = {CascadeType.DETACH} ,fetch= FetchType.LAZY)
    private Set<UserSecurity> stars;

    @ManyToMany(targetEntity=UserSecurity.class,cascade = {CascadeType.DETACH} ,fetch= FetchType.LAZY)
    private Set<UserSecurity> browsers;

    public int seecount;
    public int starcount;
    public int  commentcount;

    public Set<Comment> getComments() {
        return null;
    }

    public Set<UserSecurity> getStars() {
        return null;
    }

    public Set<UserSecurity> getStarsInfo() {
        return stars;
    }

    public Set<UserSecurity> getBrowsers() {
        return null;
    }

    public Set<UserSecurity> getBrowsersInfo() {
        return browsers;
    }

    public int getSeecount() {
        if (null!=browsers){
        int count=browsers.size();
        return count;}else {
            return 0;
        }
    }

    public int getStarcount() {
        if(null!=stars){
        int count=stars.size();
        return count;}else {
            return 0;
        }
    }

}
