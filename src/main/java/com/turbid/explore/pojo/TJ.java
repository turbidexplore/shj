package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "_tj")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class TJ extends BaseEntity {

    //用户认证信息
    @OneToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "user_id",referencedColumnName = "code")
    private UserSecurity userSecurity;

    //0首页 1找产品列表 2找产品发布页面 3找产品详情页面 4案例列表 5案列详情页面 6图库列表 7图库详情 8社区列表 9社区详情 10商铺列表 11商铺详情 12课程页面 13课程组 14课程详情 15积分商城 16商铺详情 17列表
    @Column(name = "type",length = 255)
    private int type;

    @Column(name = "relationcode",length = 255)
    private String relationcode;
}
