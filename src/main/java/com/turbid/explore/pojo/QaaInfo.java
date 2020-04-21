package com.turbid.explore.pojo;

import com.turbid.explore.tools.CodeLib;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Questions and answers
 */
@Data
@Entity
@Table(name = "qaa_info")
@ApiModel(description= "问答实体")
public class QaaInfo extends BaseEntity{

    @ApiModelProperty(value = "标题")
    @Column(name = "title")
    private String title;

    @ApiModelProperty(value = "内容")
    @Column(name = "content")
    private String content;

    @ApiModelProperty(value = "标签")
    @Column(name = "label")
    private String label;

    //发布者信息
    @OneToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    UserSecurity userSecurity;

    //点赞者信息
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<UserSecurity> stars;

    //评论信息
    @OneToMany(cascade= CascadeType.ALL,fetch= FetchType.LAZY)
    private List<Comment> comments;

    private String addftime;

    public String getAddftime() {
        return CodeLib.getFriendlyTime(this.getCreate_time(),true);
    }

    private String addtime;

    public String getAddtime() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(this.getCreate_time().getTime());
        return dateStr;
    }
}
