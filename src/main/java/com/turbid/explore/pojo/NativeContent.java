package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turbid.explore.utils.CodeLib;
import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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
    @OneToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    UserSecurity userSecurity;

    //浏览者信息
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<UserSecurity> sees;

    //软文内容
    @Column(name = "content",length = 66666)
    private String content;

    @Column(name = "firstimage",length = 255)
    private String firstimage;

    //点赞者信息
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<UserSecurity> stars;

    //评论信息
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<Comment> comments;

    private String addtime;

    public String getAddtime() {
        return CodeLib.getStandardDate(this.getCreate_time().getTimeInMillis());
    }
}
