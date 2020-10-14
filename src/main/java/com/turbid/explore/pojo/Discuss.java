package com.turbid.explore.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@Entity
@Table(name = "_discuss")
@Component
public class Discuss extends BaseEntity {

    @Column(name = "communityCode")
    private String communityCode;

    @Column(name = "content")
    private String content;

    @Column(name = "imgs")
    private String imgs;

    @Column(name = "star")
    private int star;

    @Column(name = "isstar")
    private boolean isstar;

    //用户基础信息
    @ManyToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "discuss_user_id",referencedColumnName = "code")
    private UserSecurity discussUserSecurity;

    //用户基础信息
    @ManyToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "reply_user_id",referencedColumnName = "code")
    private UserSecurity replyUserSecurity;

    @Column(name = "ucontent")
    private String ucontent;

    public String getUcontent() {
        return Community.unicode2String(content);
    }

}
