package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turbid.explore.pojo.bo.CollectionType;
import com.turbid.explore.pojo.bo.ComentType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "collection")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Collection extends BaseEntity {

    @ApiModelProperty(value = "收藏用户信息")
    @OneToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "user_id",referencedColumnName = "code")
    private UserSecurity userSecurity;

    //评论类型
    @ApiModelProperty(value = "收藏类型")
    @Column(name = "type",length = 5000)
    private CollectionType collectionType;

    //评论关联
    @ApiModelProperty(value = "收藏关联")
    @Column(name = "relation",length = 255)
    private String relation;
}
