package com.turbid.explore.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "feedback")
public class Feedback extends BaseEntity {

    @ApiModelProperty(value = "发布者")
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="usercode")
    private UserSecurity userSecurity;

    @Column(name = "content",length = 500)
    private String content;

    @Column(name = "images",length = 1000)
    private String images;
}
