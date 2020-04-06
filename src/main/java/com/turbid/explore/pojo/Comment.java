package com.turbid.explore.pojo;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    public Comment() {
    }

    @OneToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "user_id",referencedColumnName = "code")
    private UserSecurity userSecurity;

    //评论内容
    @Column(name = "content",length = 255)
    private String content;

    //评论类型
    @Column(name = "type",length = 255)
    private Integer type;

    //评论关联
    @Column(name = "relation",length = 255)
    private String relation;


}
