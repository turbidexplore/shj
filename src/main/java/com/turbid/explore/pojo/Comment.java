package com.turbid.explore.pojo;


import com.turbid.explore.pojo.bo.ComentType;
import com.turbid.explore.service.CommentService;
import com.turbid.explore.service.impl.CommentServiceImpl;
import com.turbid.explore.tools.CodeLib;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.text.SimpleDateFormat;

@Data
@Entity
@Table(name = "comment")
@Component
public class Comment extends BaseEntity {

    public Comment() {
    }

    @ApiModelProperty(value = "评论用户信息")
    @OneToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "user_id",referencedColumnName = "code")
    private UserSecurity userSecurity;

    //评论内容
    @ApiModelProperty(value = "评论内容")
    @Column(name = "content",length = 255)
    private String content;

    //评论类型
    @ApiModelProperty(value = "评论类型")
    @Column(name = "type",length = 5000)
    private ComentType comentType;

    //评论关联
    @ApiModelProperty(value = "评论关联")
    @Column(name = "relation",length = 255)
    private String relation;

    private int count;



}
