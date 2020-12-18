package com.turbid.explore.pojo;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turbid.explore.tools.CodeLib;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "nativecontent")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class NativeContent extends BaseEntity{

    public NativeContent() {
    }

    //软文标题
    @Column(name = "title",length = 255)
    private String title;

    //发布者信息
    @OneToOne(cascade=CascadeType.ALL,fetch= FetchType.EAGER)
    UserSecurity userSecurity;

    //浏览者信息
    @ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    private Set<UserSecurity> sees;

    //软文内容
    @Column(name = "content",length = 66666)
    private String content;

    @ApiModelProperty(value = "主题")
    @Column(name = "subject")
    private String subject;

    @ApiModelProperty(value = "自由标签")
    @Column(name = "freesubject")
    private String freesubject;

    //软文内容
    @Column(name = "label",length = 60000)
    private String label;

    @Column(name = "hat",length = 10)
    private Integer hat;

    @Column(name = "fromv",length = 2)
    private String fromv;

    @Column(name = "firstimage",length = 1000)
    private String firstimage;

    //点赞者信息
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private Set<UserSecurity> stars;

    //评论信息
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private Set<Comment> comments;


    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},fetch= FetchType.EAGER)
    @JoinColumn(name="company_code")
    private Shop company;


    public int seecount;
    public int starcount;
    public int  commentcount;

    public int imgsize;

    public int getImgsize() {
        return CodeLib.listImgSrc(this.content).size();
    }

    public int getSeecount() {
        if(null==sees){
            return 0;
        }else {
            int count = sees.size();
            this.sees = null;
            return count;
        }
    }

    public int getStarcount() {
        if(null==stars){
            return 0;
        }
        int count=stars.size();
        this.stars=null;
        return count;
    }

    public int getCommentcount() {
        if(null==comments){
            return 0;
        }
        int count=comments.size();
        this.comments=null;
        return count;
    }

    private String imgs;

    public String getImgs() {
        int i=0;
        imgs="";
        CodeLib.listImgSrc(this.content).forEach(v->{
            if(imgs==""&&imgs.equals("")){
                imgs=imgs+v;
            }else {
                imgs=imgs+","+v;
            }

        });
        return imgs;
    }
}
