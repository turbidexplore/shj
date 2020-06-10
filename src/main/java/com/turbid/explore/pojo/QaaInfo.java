package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turbid.explore.tools.CodeLib;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

/**
 * Questions and answers
 */
@Data
@Entity
@Table(name = "qaa_info")
@ApiModel(description= "问答实体")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class QaaInfo extends BaseEntity{

    @ApiModelProperty(value = "标题")
    @Column(name = "title")
    private String title;

    @ApiModelProperty(value = "内容")
    @Column(name = "content",length = 500)
    private String content;

    @ApiModelProperty(value = "图片")
    @Column(name = "images",length = 5000)
    private String images;

    @ApiModelProperty(value = "标签")
    @Column(name = "label")
    private String label;

    //发布者信息
    @OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    UserSecurity userSecurity;

    //点赞者信息
    @OneToMany(fetch=FetchType.LAZY)
    private List<UserSecurity> stars;

    //评论信息
    @OneToMany(fetch= FetchType.LAZY)
    private List<Answer> answers;

    public List<Answer> getAnswers() {
        return null;
    }

    public List<Answer> getAnswersinfo() {
        return answers;
    }

    private int starcount;

    public int getStarcount() {
        if(null!=stars){
            return CodeLib.removeDuplicate(stars).size();}else {return 0; }
    }


    private int answercount;

    public int getAnswercount() {

        if(null!=answers){

        return CodeLib.removeDuplicate(answers).size();
        }else {return 0;}
    }


    @Column(name = "isdel")
    private boolean isdel=false;
}
