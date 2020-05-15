package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turbid.explore.tools.CodeLib;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Set;

@Data
@Entity
@Table(name = "answer")
@ApiModel(description= "回答实体")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Answer extends BaseEntity{

    @ApiModelProperty(value = "回答用户信息")
    @OneToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "user_id",referencedColumnName = "code")
    private UserSecurity userSecurity;

    @ApiModelProperty(value = "回答内容")
    @Column(name = "content",length = 500)
    private String content;

    @ApiModelProperty(value = "图片")
    @Column(name = "images",length = 5000)
    private String images;

    @ApiModelProperty(value = "qaacode")
    @Column(name = "qaacode")
    private String qaacode;

    //点赞者信息
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    private Set<UserSecurity> stars;

    private int starscount;

    public int getStarscount() {
        if(null!=stars) {
            return stars.size();
        }else {
            return 0;
        }
    }

    private int commentcount;

    public int getCommentcount() {
        return commentcount;
    }

    private boolean isstar;


    @Column(name = "isdel")
    private boolean isdel=false;

}
