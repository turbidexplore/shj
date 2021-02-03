package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "_liveInfo")
@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class LiveInfo extends BaseEntity {


    @Column(name = "title")
    private String title;

    @Column(name = "indeximg")
    private String indeximg;

    @Column(name = "type")
    private int type;

    @Column(name = "starttime")
    private String starttime;

    @Column(name = "livetype")
    private int livetype;

    @Column(name = "seecount")
    private int seecount;

    @Column(name = "starcount")
    private int starcount;

    @Column(name = "backcount")
    private int backcount;

    @Column(name = "chatcount")
    private int chatcount;

    @Column(name = "livebegintime")
    private String livebegintime;

    @Column(name = "pushurl")
    private String pushurl;

    @Column(name = "pushkey")
    private String pushkey;

    @Column(name = "pullurl")
    private String pullurl;

    @Column(name = "liveendtime")
    private String liveendtime;

    @Column(name = "url")
    private String url;

    @Column(name = "groupid")
    private String groupid;

    @ManyToOne(cascade= CascadeType.REFRESH,fetch= FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "code")
    private UserSecurity userSecurity;

    @ManyToOne(cascade= CascadeType.REFRESH,fetch= FetchType.EAGER)
    @JoinColumn(name = "shop_id",referencedColumnName = "code")
    private Shop shop;

    public String getPushurl() {
        return pushurl;
    }

    public String getPullurl() {
        return pullurl;
    }
}
